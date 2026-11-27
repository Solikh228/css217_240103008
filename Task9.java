interface BitrateStrategy {
    String selectResolution(double bandwidthMbps);
}

class DataSaverStrategy implements BitrateStrategy {
    public String selectResolution(double bw) { return "480p"; }
}

class BalancedAutoStrategy implements BitrateStrategy {
    public String selectResolution(double bw) {
        if (bw >= 10.0) return "1080p";
        if (bw >= 4.0) return "720p";
        return "480p";
    }
}

class UltraHdStrategy implements BitrateStrategy {
    public String selectResolution(double bw) { return bw >= 25.0 ? "2160p" : "1440p"; }
}

class BufferMonitor {
    double currentBandwidthMbps() { return 14.0; }
}

class VideoDecoder {
    void configureStream(String url, String res) { System.out.println("Video " + res + " -> " + url); }
}

class AudioSyncEngine {
    void lockSync(String url) { System.out.println("Audio sync locked -> " + url); }
}

class SubtitleRenderer {
    void attachTrack(String url) { System.out.println("Subtitles active -> " + url); }
}

class MediaPlayerFacade {
    private BitrateStrategy strategy;
    private final BufferMonitor buffer = new BufferMonitor();
    private final VideoDecoder decoder = new VideoDecoder();
    private final AudioSyncEngine audio = new AudioSyncEngine();
    private final SubtitleRenderer subtitles = new SubtitleRenderer();
    private String activeUrl;

    MediaPlayerFacade(BitrateStrategy strategy) { this.strategy = strategy; }

    void playStream(String streamUrl) {
        this.activeUrl = streamUrl;
        String res = strategy.selectResolution(buffer.currentBandwidthMbps());
        audio.lockSync(streamUrl);
        decoder.configureStream(streamUrl, res);
        subtitles.attachTrack(streamUrl);
    }

    void changeQualityMode(BitrateStrategy newStrategy) {
        this.strategy = newStrategy;
        if (activeUrl != null) {
            decoder.configureStream(activeUrl, strategy.selectResolution(buffer.currentBandwidthMbps()));
        }
    }
}