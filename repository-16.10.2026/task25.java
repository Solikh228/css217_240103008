interface PixelFilterStrategy {
    int[] applyFilter(int[] pixels);
}

class InvertStrategy implements PixelFilterStrategy {
    public int[] applyFilter(int[] pixels) {
        int[] res = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            res[i] = 255 - pixels[i];
        }
        return res;
    }
}

class ThresholdStrategy implements PixelFilterStrategy {
    public int[] applyFilter(int[] pixels) {
        int[] res = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] >= 128) {
                res[i] = 255;
            } else {
                res[i] = 0;
            }
        }
        return res;
    }
}

class BrightenStrategy implements PixelFilterStrategy {
    public int[] applyFilter(int[] pixels) {
        int[] res = new int[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            int newPixel = pixels[i] + 30;
            if (newPixel > 255) {
                res[i] = 255;
            } else {
                res[i] = newPixel;
            }
        }
        return res;
    }
}