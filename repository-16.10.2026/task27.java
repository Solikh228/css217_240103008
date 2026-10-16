interface StaffMember {
    String getFullName();
    double getAnnualSalary();
}

class ContractorRecord {
    public String getFirstName() { return "Ivan"; }
    public String getLastName() { return "Ivanov"; }
    public double getMonthlyPay() { return 2000.0; }
}

class ContractorAdapter implements StaffMember {
    ContractorRecord cr;

    public ContractorAdapter(ContractorRecord cr) {
        this.cr = cr;
    }

    public String getFullName() {
        return cr.getFirstName() + " " + cr.getLastName();
    }

    public double getAnnualSalary() {
        return cr.getMonthlyPay() * 12.0;
    }
}