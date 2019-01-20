package by.bsu.converters.service.weight;


import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class LbToGram extends ChainConverter {
    public LbToGram() {
    }

    public LbToGram(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value * 453.59237;
    }
}
