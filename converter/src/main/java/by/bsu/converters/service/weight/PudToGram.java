package by.bsu.converters.service.weight;


import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class PudToGram extends ChainConverter {
    public PudToGram() {
    }

    public PudToGram(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value * 16.3804815 * 1000;
    }
}
