record Rgb(int r, int g, int b) {}

interface FilterStrategy {
    Rgb[][] apply(Rgb[][] pixels);
}

class GrayscaleFilter implements FilterStrategy {
    public Rgb[][] apply(Rgb[][] pixels) {
        Rgb[][] out = new Rgb[pixels.length][pixels[0].length];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                Rgb p = pixels[y][x];
                int gray = (int) (0.299 * p.r() + 0.587 * p.g() + 0.114 * p.b());
                out[y][x] = new Rgb(gray, gray, gray);
            }
        }
        return out;
    }
}

class SepiaToneFilter implements FilterStrategy {
    public Rgb[][] apply(Rgb[][] pixels) {
        Rgb[][] out = new Rgb[pixels.length][pixels[0].length];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                Rgb p = pixels[y][x];
                int nr = Math.min(255, (int) (p.r() * 0.393 + p.g() * 0.769 + p.b() * 0.189));
                int ng = Math.min(255, (int) (p.r() * 0.349 + p.g() * 0.686 + p.b() * 0.168));
                int nb = Math.min(255, (int) (p.r() * 0.272 + p.g() * 0.534 + p.b() * 0.131));
                out[y][x] = new Rgb(nr, ng, nb);
            }
        }
        return out;
    }
}

class HighContrastFilter implements FilterStrategy {
    private int clamp(double v) { return Math.max(0, Math.min(255, (int) ((v - 128) * 1.5 + 128))); }
    public Rgb[][] apply(Rgb[][] pixels) {
        Rgb[][] out = new Rgb[pixels.length][pixels[0].length];
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                Rgb p = pixels[y][x];
                out[y][x] = new Rgb(clamp(p.r()), clamp(p.g()), clamp(p.b()));
            }
        }
        return out;
    }
}

class ImageDecoder {
    Rgb[][] decode(String path) { return new Rgb[][] {{new Rgb(100, 150, 200)}}; }
}

class ThumbnailResizer {
    Rgb[][] resize(Rgb[][] pixels, int w, int h) {
        Rgb[][] out = new Rgb[h][w];
        for (int y = 0; y < h; y++) Arrays.fill(out[y], pixels[0][0]);
        return out;
    }
}

class ImageEncoder {
    String encodeAndSave(Rgb[][] pixels, String outPath) {
        System.out.println("Saved " + pixels[0].length + "x" + pixels.length + " to " + outPath);
        return outPath;
    }
}

class ImagePipelineFacade {
    private FilterStrategy strategy;
    private final ImageDecoder decoder = new ImageDecoder();
    private final ThumbnailResizer resizer = new ThumbnailResizer();
    private final ImageEncoder encoder = new ImageEncoder();

    ImagePipelineFacade(FilterStrategy strategy) { this.strategy = strategy; }
    void setStrategy(FilterStrategy strategy) { this.strategy = strategy; }

    String processImage(String inputPath, int width, int height) {
        Rgb[][] raw = decoder.decode(inputPath);
        Rgb[][] thumb = resizer.resize(raw, width, height);
        Rgb[][] filtered = strategy.apply(thumb);
        return encoder.encodeAndSave(filtered, inputPath + ".thumb.png");
    }
}