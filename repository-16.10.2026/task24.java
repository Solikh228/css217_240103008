interface AppLogger {
    void log(String level, String message);
}

class ConsoleWriter {
    public void writeLine(String fullText) {
        System.out.println(fullText);
    }
}

class LoggerAdapter implements AppLogger {
    ConsoleWriter cw;

    public LoggerAdapter(ConsoleWriter cw) {
        this.cw = cw;
    }

    public void log(String level, String message) {
        
        String formatted = "[" + level.toUpperCase() + "] " + message;
        cw.writeLine(formatted);
    }
}