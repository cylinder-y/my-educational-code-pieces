package by.bsu.converters.controller.result;

public class ConverterResult {
    private final String converter;
    private final String from;
    private final String to;
    private final double input;
    private final double result;

    public ConverterResult(final String converter, final String from, final String to, final double input,
                           final double result) {
        this.converter = converter;
        this.from = from;
        this.to = to;
        this.input = input;
        this.result = result;
    }

    public String getConverter() {
        return converter;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getInput() {
        return input;
    }

    public double getResult() {
        return result;
    }
}
