interface UsdPaymentProcessor {
    void payInUsd(double dollars);
}

class InrBankGateway {
    public void processRupees(double rupees) {
        System.out.println("Paid " + rupees + " INR");
    }
}

class CurrencyAdapter implements UsdPaymentProcessor {
    InrBankGateway inrGateway;

    public CurrencyAdapter(InrBankGateway inrGateway) {
        this.inrGateway = inrGateway;
    }

    public void payInUsd(double dollars) {
        double rupees = dollars * 84.0; // курс по заданию
        inrGateway.processRupees(rupees);
    }
}