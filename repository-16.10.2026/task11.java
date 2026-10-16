interface Notifier {
    String send(String msg);
}

class EmailNotifier implements Notifier {
    public String send(String msg) {
        return "EMAIL: " + msg;
    }
}

class SMSDecorator implements Notifier {
    Notifier n;
    public SMSDecorator(Notifier n) { this.n = n; }
    public String send(String msg) {
        return n.send(msg) + " | SMS: " + msg;
    }
}

class SlackDecorator implements Notifier {
    Notifier n;
    public SlackDecorator(Notifier n) { this.n = n; }
    public String send(String msg) {
        return n.send(msg) + " | SLACK: " + msg;
    }
}

class WhatsAppDecorator implements Notifier {
    Notifier n;
    public WhatsAppDecorator(Notifier n) { this.n = n; }
    public String send(String msg) {
        return n.send(msg) + " | WA: " + msg;
    }
}