package by.bsu.converters.service;

import by.bsu.converters.service.distance.DistanceConverterFactory;
import by.bsu.converters.service.temperature.TemperatureConverterFactory;
import by.bsu.converters.service.weight.WeightConverterFactory;

public interface ConverterFactory {

    static ConverterFactory getProvider(String type) {
        if ("temperature".equalsIgnoreCase(type)) {
            return TemperatureConverterFactory.INSTANCE;
        } else if ("distance".equalsIgnoreCase(type)) {
            return DistanceConverterFactory.INSTANCE;
        } else if ("weight".equalsIgnoreCase(type)) {
            return WeightConverterFactory.INSTANCE;
        }

        return (from, to) -> {
            throw new UnsupportedOperationException("Converter not found");
        };
    }

    Converter createConverter(String from, String to);
}
