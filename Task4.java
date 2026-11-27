import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.Deflater;

interface CompressionStrategy {
    byte[] compress(byte[] data);
}

class RunLengthCompression implements CompressionStrategy {
    public byte[] compress(byte[] data) {
        if (data.length == 0) return new byte[0];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int count = 1;
        byte prev = data[0];
        for (int i = 1; i < data.length; i++) {
            if (data[i] == prev && count < 255) {
                count++;
            } else {
                out.write(count);
                out.write(prev);
                prev = data[i];
                count = 1;
            }
        }
        out.write(count);
        out.write(prev);
        return out.toByteArray();
    }
}

class DictionaryCompression implements CompressionStrategy {
    public byte[] compress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        while (!deflater.finished()) {
            int n = deflater.deflate(buf);
            out.write(buf, 0, n);
        }
        deflater.end();
        return out.toByteArray();
    }
}

class NoCompression implements CompressionStrategy {
    public byte[] compress(byte[] data) { return data; }
}

class FileReader {
    Map<String, byte[]> readFiles(List<String> paths) {
        Map<String, byte[]> map = new LinkedHashMap<>();
        for (String p : paths) map.put(p, ("data_" + p).getBytes(StandardCharsets.UTF_8));
        return map;
    }
}

class ChecksumGenerator {
    String sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class ArchivePackager {
    void packageArchive(String destination, int entryCount) {
        System.out.println("Archive '" + destination + "' created with " + entryCount + " entries");
    }
}

class ArchiveFacade {
    private CompressionStrategy strategy;
    private final FileReader reader = new FileReader();
    private final ChecksumGenerator checksum = new ChecksumGenerator();
    private final ArchivePackager packager = new ArchivePackager();

    ArchiveFacade(CompressionStrategy strategy) { this.strategy = strategy; }
    void setStrategy(CompressionStrategy strategy) { this.strategy = strategy; }

    void createArchive(List<String> sourceFiles, String destination) {
        Map<String, byte[]> files = reader.readFiles(sourceFiles);
        for (Map.Entry<String, byte[]> entry : files.entrySet()) {
            checksum.sha256(entry.getValue());
            strategy.compress(entry.getValue());
        }
        packager.packageArchive(destination, files.size());
    }
}