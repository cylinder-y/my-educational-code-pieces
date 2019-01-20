package by.bsu.converters.service.distance;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class MileToMeter extends ChainConverter {
    public MileToMeter() {
    }

    public MileToMeter(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value * 1.609344d * 1000;
    }
}
