package task09;

import java.util.*;

class MemberValidator {
    private final Set<String> activeStudents = new HashSet<>(Arrays.asList("240103008", "240103015"));

    public boolean isActive(String studentId) {
        return activeStudents.contains(studentId);
    }
}

class CatalogService {
    private final Map<String, Boolean> books = new HashMap<>();

    public CatalogService() {
        books.put("978-0134685991", true); 
        books.put("978-0596007126", false); 
    }

    public boolean isAvailable(String isbn) {
        return books.getOrDefault(isbn, false);
    }

    public void setAvailable(String isbn, boolean status) {
        if (books.containsKey(isbn)) {
            books.put(isbn, status);
        }
    }
}

class FineCalculator {
    private final Map<String, Double> fines = new HashMap<>();

    public FineCalculator() {
        fines.put("240103008", 0.0);
        fines.put("240103015", 12.50);
    }

    public double getFineBalance(String studentId) {
        return fines.getOrDefault(studentId, 0.0);
    }
}

class LoanRecorder {
    public void recordLoan(String studentId, String isbn) {
        System.out.println("[LoanRecorder] Loan created: Student " + studentId + " -> ISBN " + isbn);
    }

    public void closeLoan(String studentId, String isbn) {
        System.out.println("[LoanRecorder] Loan closed: Student " + studentId + " returned ISBN " + isbn);
    }
}

class LibraryCheckoutFacade {
    private final MemberValidator validator;
    private final CatalogService catalog;
    private final FineCalculator fineCalc;
    private final LoanRecorder recorder;

    public LibraryCheckoutFacade(MemberValidator validator, CatalogService catalog, FineCalculator fineCalc, LoanRecorder recorder) {
        this.validator = validator;
        this.catalog = catalog;
        this.fineCalc = fineCalc;
        this.recorder = recorder;
    }

    public boolean borrowBook(String studentId, String isbn) {
        if (!validator.isActive(studentId)) {
            System.out.println("Checkout denied: Student ID " + studentId + " is not active.");
            return false;
        }
        double fine = fineCalc.getFineBalance(studentId);
        if (fine > 0) {
            System.out.printf("Checkout denied: Student %s has unpaid fines ($%.2f).%n", studentId, fine);
            return false;
        }
        if (!catalog.isAvailable(isbn)) {
            System.out.println("Checkout denied: Book " + isbn + " is currently unavailable.");
            return false;
        }

        catalog.setAvailable(isbn, false);
        recorder.recordLoan(studentId, isbn);
        return true;
    }

    public void returnBook(String studentId, String isbn) {
        catalog.setAvailable(isbn, true);
        recorder.closeLoan(studentId, isbn);
    }
}

public class Main {
    public static void main(String[] args) {
        LibraryCheckoutFacade library = new LibraryCheckoutFacade(
                new MemberValidator(), new CatalogService(), new FineCalculator(), new LoanRecorder()
        );

        library.borrowBook("240103015", "978-0134685991"); 
        library.borrowBook("240103008", "978-0596007126"); 
        library.borrowBook("240103008", "978-0134685991"); 
        library.returnBook("240103008", "978-0134685991"); 
    }
}