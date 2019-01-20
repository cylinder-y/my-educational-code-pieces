package by.bsu.converters.service.temperature;

import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class CelsiusToKelvin extends ChainConverter {

    CelsiusToKelvin() {
    }

    CelsiusToKelvin(final Converter decoratorConverter) {
        super(decoratorConverter);
    }

    protected double preConversion(final double value) {
        return value + 273.15d;
    }
}
