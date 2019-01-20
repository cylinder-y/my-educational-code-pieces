package by.bsu.converters.service.weight;

import by.bsu.converters.service.ChainConverter;

public class GramToPud extends ChainConverter {
    @Override
    protected double preConversion(final double value) {
        return value / (16.3804815 * 1000);
    }
}
