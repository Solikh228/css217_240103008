import java.nio.charset.StandardCharsets;
import java.util.*;

interface BackupStrategy {
    Map<String, String> filterFiles(Map<String, String> all, Map<String, String> lastFull, Map<String, String> lastAny);
}

class FullBackupStrategy implements BackupStrategy {
    public Map<String, String> filterFiles(Map<String, String> all, Map<String, String> lf, Map<String, String> la) {
        return new LinkedHashMap<>(all);
    }
}

class IncrementalBackupStrategy implements BackupStrategy {
    public Map<String, String> filterFiles(Map<String, String> all, Map<String, String> lf, Map<String, String> la) {
        Map<String, String> res = new LinkedHashMap<>();
        all.forEach((k, v) -> { if (!v.equals(la.get(k))) res.put(k, v); });
        return res;
    }
}

class DifferentialBackupStrategy implements BackupStrategy {
    public Map<String, String> filterFiles(Map<String, String> all, Map<String, String> lf, Map<String, String> la) {
        Map<String, String> res = new LinkedHashMap<>();
        all.forEach((k, v) -> { if (!v.equals(lf.get(k))) res.put(k, v); });
        return res;
    }
}

class FileScanner {
    Map<String, String> scan(String dir) {
        return Map.of(dir + "/a.db", "h1", dir + "/b.log", "h2_new");
    }
}

class AesEncryptor {
    byte[] encryptPayload(Map<String, String> files) {
        return ("ENC:" + files.keySet()).getBytes(StandardCharsets.UTF_8);
    }
}

class CloudStorageClient {
    String upload(byte[] data) {
        System.out.println("Uploaded " + data.length + " bytes");
        return "SNAP_01";
    }
}

class RetentionCleaner {
    void purgeExpired() { System.out.println("Purged expired snapshots"); }
}

class BackupFacade {
    private BackupStrategy strategy;
    private final FileScanner scanner = new FileScanner();
    private final AesEncryptor encryptor = new AesEncryptor();
    private final CloudStorageClient cloud = new CloudStorageClient();
    private final RetentionCleaner retention = new RetentionCleaner();
    private Map<String, String> lastFull = new HashMap<>(Map.of("/app/a.db", "h1", "/app/b.log", "h2"));
    private Map<String, String> lastAny = new HashMap<>(lastFull);

    BackupFacade(BackupStrategy strategy) { this.strategy = strategy; }
    void setStrategy(BackupStrategy strategy) { this.strategy = strategy; }

    String runBackupJob(String directory) {
        Map<String, String> scanned = scanner.scan(directory);
        Map<String, String> selected = strategy.filterFiles(scanned, lastFull, lastAny);
        byte[] enc = encryptor.encryptPayload(selected);
        String snapId = cloud.upload(enc);
        lastAny.putAll(selected);
        if (strategy instanceof FullBackupStrategy) lastFull = new HashMap<>(scanned);
        retention.purgeExpired();
        return snapId;
    }
}