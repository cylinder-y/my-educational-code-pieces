package by.bsu.converters.service.distance;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class MeterToVersta extends ChainConverter {
    public MeterToVersta() {
    }

    public MeterToVersta(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value / 1066.8;
    }
}
