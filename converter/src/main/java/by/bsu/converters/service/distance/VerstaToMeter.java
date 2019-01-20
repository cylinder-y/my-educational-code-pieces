package by.bsu.converters.service.distance;


import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class VerstaToMeter extends ChainConverter {
    public VerstaToMeter() {
    }

    public VerstaToMeter(final Converter next) {
        super(next);
    }

    @Override
    protected double preConversion(final double value) {
        return value * 1066.8;
    }
}
