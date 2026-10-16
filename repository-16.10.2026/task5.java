interface TextComponent {
    String format(String input);
}

class PlainText implements TextComponent {
    public String format(String input) {
        return input != null ? input : "";
    }
}

abstract class TextDecorator implements TextComponent {
    protected TextComponent wrapper;
    public TextDecorator(TextComponent wrapper) { this.wrapper = wrapper; }
}

class UpperCaseDecorator extends TextDecorator {
    public UpperCaseDecorator(TextComponent w) { super(w); }
    public String format(String input) {
        return wrapper.format(input).toUpperCase();
    }
}

class BracketDecorator extends TextDecorator {
    public BracketDecorator(TextComponent w) { super(w); }
    public String format(String input) {
        return "[" + wrapper.format(input) + "]";
    }
}
class  TrimDecoratorextends TexDecorator {
    public BracketDecorator(TextComponent w) {super(w;)}
    public String format(String input) {
        return "[" + wrapper.format(input) + "]";

    }
}
