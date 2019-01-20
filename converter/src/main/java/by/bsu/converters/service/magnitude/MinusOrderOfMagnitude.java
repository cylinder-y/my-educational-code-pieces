package by.bsu.converters.service.magnitude;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class MinusOrderOfMagnitude extends ChainConverter {

    public MinusOrderOfMagnitude() {
    }

    public MinusOrderOfMagnitude(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value / 10;
    }
}
