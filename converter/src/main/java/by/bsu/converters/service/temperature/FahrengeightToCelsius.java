package by.bsu.converters.service.temperature;


import by.bsu.converters.service.ChainConverter;
import by.bsu.converters.service.Converter;

public class FahrengeightToCelsius  extends ChainConverter {

    FahrengeightToCelsius(final Converter decoratorConverter) {
        super(decoratorConverter);
    }

    FahrengeightToCelsius() {

    }

    protected double preConversion(final double value) {
        return (value - 32) * 5 / 9;
    }
}
