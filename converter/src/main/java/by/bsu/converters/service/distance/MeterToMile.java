package by.bsu.converters.service.distance;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class MeterToMile extends ChainConverter {
    public MeterToMile() {
    }

    public MeterToMile(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value / 1609.344;
    }
}
