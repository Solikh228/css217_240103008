interface ModernPrinter {
    void printDocument(String text);
}

class DotMatrixPrinter {
    public void printCharArray(char[] buffer) {
        for (char c : buffer) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}

class PrinterAdapter implements ModernPrinter {
    private DotMatrixPrinter legacyPrinter;

    public PrinterAdapter(DotMatrixPrinter legacy) {
        this.legacyPrinter = legacy;
    }

    @Override
    public void printDocument(String text) {
        if (text != null && !text.isEmpty()) {
            legacyPrinter.printCharArray(text.toCharArray());
        }
    }
}