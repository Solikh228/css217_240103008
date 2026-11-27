import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

interface ExportStrategy {
    String serialize(List> records, String metadata);
}

class PlainTextExport implements ExportStrategy {
    public String serialize(List> records, String metadata) {
        String body = records.stream()
            .map(r -> r.entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining(" | ")))
            .collect(Collectors.joining("\n"));
        return metadata + "\n" + body;
    }
}

class CsvExport implements ExportStrategy {
    public String serialize(List> records, String metadata) {
        if (records.isEmpty()) return metadata;
        List<String> keys = new ArrayList<>(records.get(0).keySet());
        String header = String.join(",", keys);
        String rows = records.stream()
            .map(r -> keys.stream().map(k -> String.valueOf(r.get(k))).collect(Collectors.joining(",")))
            .collect(Collectors.joining("\n"));
        return "# " + metadata + "\n" + header + "\n" + rows;
    }
}

class MarkdownExport implements ExportStrategy {
    public String serialize(List> records, String metadata) {
        if (records.isEmpty()) return "*" + metadata + "*";
        List<String> keys = new ArrayList<>(records.get(0).keySet());
        String header = "| " + String.join(" | ", keys) + " |";
        String sep = "| " + keys.stream().map(k -> "---").collect(Collectors.joining(" | ")) + " |";
        String rows = records.stream()
            .map(r -> "| " + keys.stream().map(k -> String.valueOf(r.get(k))).collect(Collectors.joining(" | ")) + " |")
            .collect(Collectors.joining("\n"));
        return "### " + metadata + "\n\n" + header + "\n" + sep + "\n" + rows;
    }
}

class ContentValidator {
    void validate(List> doc) {
        if (doc == null || doc.isEmpty()) throw new IllegalArgumentException("Empty document");
    }
}

class MetadataStamp {
    String generateStamp() { return "Exported at " + Instant.now(); }
}

class DiskWriter {
    void write(String path, String content) {
        System.out.println("Wrote " + content.length() + " chars to " + path);
    }
}

class DocumentExportFacade {
    private ExportStrategy strategy;
    private final ContentValidator validator = new ContentValidator();
    private final MetadataStamp stamper = new MetadataStamp();
    private final DiskWriter writer = new DiskWriter();

    DocumentExportFacade(ExportStrategy strategy) { this.strategy = strategy; }
    void setStrategy(ExportStrategy strategy) { this.strategy = strategy; }

    void exportReport(List> document, String outputPath) {
        validator.validate(document);
        String stamp = stamper.generateStamp();
        String formatted = strategy.serialize(document, stamp);
        writer.write(outputPath, formatted);
    }
}