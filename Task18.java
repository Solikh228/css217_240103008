import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

interface LogParserStrategy {
    Map<String, Object> parse(String rawEvent);
}

class JsonLogParser implements LogParserStrategy {
    public Map<String, Object> parse(String raw) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("raw_json", raw);
        m.put("user_email", "alice@example.com");
        return m;
    }
}

class SyslogParser implements LogParserStrategy {
    public Map<String, Object> parse(String raw) {
        String[] parts = raw.split(" ", 3);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("facility", parts[0]);
        m.put("user_email", parts.length > 2 ? parts[2] : "unknown");
        return m;
    }
}

interface RedactionStrategy {
    String redact(String value);
}

class MaskPiiStrategy implements RedactionStrategy {
    public String redact(String v) { return v.length() > 2 ? v.substring(0, 2) + "***" : "***"; }
}

class HashIdentifierStrategy implements RedactionStrategy {
    public String redact(String v) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest(v.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(h).substring(0, 16);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

class StreamReader {
    List<String> readLines(List<String> raw) { return raw.stream().map(String::trim).filter(s -> !s.isEmpty()).toList(); }
}

class SchemaValidator {
    boolean isValid(Map<String, Object> rec) { return rec != null && !rec.isEmpty(); }
}

class BatchBuffer {
    private final List> buf = new ArrayList<>();
    void push(Map<String, Object> r) { buf.add(r); }
    List> flush() { List> copy = new ArrayList<>(buf); buf.clear(); return copy; }
}

class SiemForwarder {
    boolean sendWithRetry(List> batch, int maxRetries) {
        for (int i = 1; i <= maxRetries; i++) {
            System.out.println("SIEM batch sent (" + batch.size() + ") attempt " + i);
            return true;
        }
        return false;
    }
}

class LogIngestionFacade {
    private LogParserStrategy parser;
    private RedactionStrategy redactor;
    private final StreamReader reader = new StreamReader();
    private final SchemaValidator validator = new SchemaValidator();
    private final BatchBuffer buffer = new BatchBuffer();
    private final SiemForwarder siem = new SiemForwarder();

    LogIngestionFacade(LogParserStrategy parser, RedactionStrategy redactor) {
        this.parser = parser;
        this.redactor = redactor;
    }

    void setStrategies(LogParserStrategy parser, RedactionStrategy redactor) {
        this.parser = parser;
        this.redactor = redactor;
    }

    boolean ingestBatch(List<String> rawEvents) {
        for (String line : reader.readLines(rawEvents)) {
            Map<String, Object> rec = parser.parse(line);
            if (!validator.isValid(rec)) continue;
            if (rec.containsKey("user_email")) rec.put("user_email", redactor.redact(String.valueOf(rec.get("user_email"))));
            buffer.push(rec);
        }
        return siem.sendWithRetry(buffer.flush(), 3);
    }
}
//vibe code