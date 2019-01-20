package by.bsu.converters.service.temperature;


import by.bsu.converters.service.Converter;
import by.bsu.converters.service.ConverterFactory;

public class TemperatureConverterFactory implements ConverterFactory {

    public static final TemperatureConverterFactory INSTANCE = new TemperatureConverterFactory();
    private static final String CELSIUM = "C";
    private static final String KELVIN = "K";
    private static final String FAHRENGEIGHT = "F";

    private TemperatureConverterFactory() {
    }

    public Converter createConverter(String from, String to) {
        if (CELSIUM.equalsIgnoreCase(from) && KELVIN.equalsIgnoreCase(to)) {
            return new CelsiusToKelvin();
        } else if (KELVIN.equalsIgnoreCase(from) && CELSIUM.equalsIgnoreCase(to)) {
            return new KelvinToCelsius();
        } else if (FAHRENGEIGHT.equalsIgnoreCase(from) && CELSIUM.equalsIgnoreCase(to)) {
            return new FahrengeightToCelsius();
        } else if (CELSIUM.equalsIgnoreCase(from) && FAHRENGEIGHT.equalsIgnoreCase(to)) {
            return new CelsiusToFahrengeight();
        } else if (FAHRENGEIGHT.equalsIgnoreCase(from) && KELVIN.equalsIgnoreCase(to)) {
            return new FahrengeightToCelsius(new CelsiusToKelvin());
        } else if (KELVIN.equalsIgnoreCase(from) && FAHRENGEIGHT.equalsIgnoreCase(to)) {
            return new KelvinToCelsius(new CelsiusToFahrengeight());
        }

        return value -> {
            throw new UnsupportedOperationException("Conversion is not supported");
        };
    }
}
