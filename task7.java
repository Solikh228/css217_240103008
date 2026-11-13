package task07;

class SpellChecker {
    public String check(String text) {
        System.out.println("[SpellChecker] Scanning text for typos...");
        return text.trim();
    }
}

class PageFormatter {
    public String formatA4(String text) {
        System.out.println("[PageFormatter] Applying standard A4 margins.");
        return "[A4 Layout]\n" + text;
    }
}

class PdfConverter {
    public byte[] convert(String formattedText) {
        System.out.println("[PdfConverter] Rendering PDF byte stream.");
        return formattedText.getBytes();
    }
}

class PrinterSpooler {
    public void printJob(String formattedText) {
        System.out.println("[PrinterSpooler] Sent to physical printer:\n" + formattedText);
    }
}

class DocumentFacade {
    private final SpellChecker spellChecker;
    private final PageFormatter formatter;
    private final PdfConverter pdfConverter;
    private final PrinterSpooler spooler;

    public DocumentFacade(SpellChecker spellChecker, PageFormatter formatter, PdfConverter pdfConverter, PrinterSpooler spooler) {
        this.spellChecker = spellChecker;
        this.formatter = formatter;
        this.pdfConverter = pdfConverter;
        this.spooler = spooler;
    }

    public void printCleanDocument(String text) {
        System.out.println("--- Printing Document ---");
        String clean = spellChecker.check(text);
        String formatted = formatter.formatA4(clean);
        spooler.printJob(formatted);
    }

    public void exportAsPdf(String text, String fileName) {
        System.out.println("\n--- Exporting PDF ---");
        String clean = spellChecker.check(text);
        String formatted = formatter.formatA4(clean);
        byte[] data = pdfConverter.convert(formatted);
        System.out.println("Saved " + fileName + " (" + data.length + " bytes).");
    }
}

public class Main {
    public static void main(String[] args) {
        DocumentFacade docFacade = new DocumentFacade(
                new SpellChecker(), new PageFormatter(), new PdfConverter(), new PrinterSpooler()
        );
        docFacade.printCleanDocument("  Quarterly project report draft.  ");
        docFacade.exportAsPdf("Final laboratory notes", "lab_notes.pdf");
    }
}