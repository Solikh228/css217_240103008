interface DataSource {
    String readData();
}

class RawStringSource implements DataSource {
    public String readData() { return "confidential_payload"; }
}

class ReverseCipherDecorator implements DataSource {
    DataSource ds;
    public ReverseCipherDecorator(DataSource ds) { this.ds = ds; }
    public String readData() {
        return new StringBuilder(ds.readData()).reverse().toString(); 
    }
}

class ShiftCipherDecorator implements DataSource {
    DataSource ds;
    public ShiftCipherDecorator(DataSource ds) { this.ds = ds; }
    public String readData() {
        String orig = ds.readData();
        String res = "";
        for (int i = 0; i < orig.length(); i++) {
            res += (char)(orig.charAt(i) + 1); 
        }
        return res;
    }
}

class HeaderDecorator implements DataSource {
    DataSource ds;
    public HeaderDecorator(DataSource ds) { this.ds = ds; }
    public String readData() {
        return "SEC_HEADER:" + ds.readData();
    }
}