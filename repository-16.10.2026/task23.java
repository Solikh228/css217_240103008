interface FormattedText {
    String render();
}

class SimpleText implements FormattedText {
    String text;
    public SimpleText(String text) { this.text = text; }
    public String render() { return text; }
}

class BoldTagDecorator implements FormattedText {
    FormattedText ft;
    public BoldTagDecorator(FormattedText ft) { this.ft = ft; }
    public String render() {
        return "[B]" + ft.render() + "[/B]";
    }
}

class ItalicTagDecorator implements FormattedText {
    FormattedText ft;
    public ItalicTagDecorator(FormattedText ft) { this.ft = ft; }
    public String render() {
        return "[I]" + ft.render() + "[/I]";
    }
}

class CodeTagDecorator implements FormattedText {
    FormattedText ft;
    public CodeTagDecorator(FormattedText ft) { this.ft = ft; }
    public String render() {
        return "[CODE]" + ft.render() + "[/CODE]";
    }
}