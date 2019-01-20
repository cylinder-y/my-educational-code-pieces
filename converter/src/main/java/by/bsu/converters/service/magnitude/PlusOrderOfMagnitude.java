package by.bsu.converters.service.magnitude;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class PlusOrderOfMagnitude extends ChainConverter {

    public PlusOrderOfMagnitude() {
    }

    public PlusOrderOfMagnitude(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value * 10;
    }
}
