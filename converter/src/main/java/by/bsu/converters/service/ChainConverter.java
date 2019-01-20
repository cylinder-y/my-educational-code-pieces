package by.bsu.converters.service;

public abstract class ChainConverter implements Converter {
    private Converter next;

    public ChainConverter() {
    }

    public ChainConverter(final Converter next) {
        this.next = next;
    }

    public double convert(double value) {
        value = preConversion(value);

        if (next != null) {
            value = next.convert(value);
        }

        return value;
    }

    protected abstract double preConversion(final double value);


}
