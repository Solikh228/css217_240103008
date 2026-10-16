interface CompressionStrategy {
    String compress(String rawData);
}

class HyphenPackStrategy implements CompressionStrategy {
    public String compress(String rawData) {
        return rawData.replace(" ", "-") + ".hpk";
    }
}

class VowelStripStrategy implements CompressionStrategy {
    public String compress(String rawData) {
        
        return rawData.replaceAll("[aeiouAEIOU]", "") + ".vzp";
    }
}

class ReversePackStrategy implements CompressionStrategy {
    public String compress(String rawData) {
        String rev = "";
        for (int i = rawData.length() - 1; i >= 0; i--) {
            rev = rev + rawData.charAt(i); 
        }
        return rev + ".rev";
    }
}