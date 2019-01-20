package by.bsu.converters.service.temperature;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class CelsiusToFahrengeight extends ChainConverter {
    CelsiusToFahrengeight(final Converter decoratorConverter) {
        super(decoratorConverter);
    }

    CelsiusToFahrengeight() {
    }

    protected double preConversion(double value) {
        return value * 9 / 5 + 32;
    }

}
