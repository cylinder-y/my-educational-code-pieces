package by.bsu.converters.service.distance;


import by.bsu.converters.service.Converter;
import by.bsu.converters.service.ConverterFactory;
import by.bsu.converters.service.magnitude.MinusOrderOfMagnitude;
import by.bsu.converters.service.magnitude.PlusOrderOfMagnitude;

public class DistanceConverterFactory implements ConverterFactory {

    public static final ConverterFactory INSTANCE = new DistanceConverterFactory();

    private static final String M = "m";
    private static final String DM = "dm";
    private static final String MM = "mm";
    private static final String HM = "hm";
    private static final String KM = "km";
    private static final String MILE = "mile";
    private static final String VERSTA = "versta";

    private DistanceConverterFactory() {
    }

    @Override
    public Converter createConverter(final String from, final String to) {
        if (M.equalsIgnoreCase(from) && DM.equalsIgnoreCase(to)) {
            return new PlusOrderOfMagnitude();
        } else if (M.equalsIgnoreCase(from) && MM.equalsIgnoreCase(to)) {
            return new PlusOrderOfMagnitude(new PlusOrderOfMagnitude());
        } else if (M.equalsIgnoreCase(from) && HM.equalsIgnoreCase(to)) {
            return new MinusOrderOfMagnitude();
        } else if (M.equalsIgnoreCase(from) && KM.equalsIgnoreCase(to)) {
            return new MinusOrderOfMagnitude(new MinusOrderOfMagnitude());
        } else if (KM.equalsIgnoreCase(from) && MILE.equalsIgnoreCase(to)) {
            return new PlusOrderOfMagnitude(new PlusOrderOfMagnitude(new PlusOrderOfMagnitude(new MeterToMile())));
        } else if (MILE.equalsIgnoreCase(from) && KM.equalsIgnoreCase(to)) {
            return new MileToMeter(new MinusOrderOfMagnitude(new MinusOrderOfMagnitude(new MinusOrderOfMagnitude())));
        } else if (VERSTA.equalsIgnoreCase(from) && M.equalsIgnoreCase(to)) {
            return new VerstaToMeter();
        } else if (M.equalsIgnoreCase(from) && VERSTA.equalsIgnoreCase(to)) {
            return new MeterToVersta();
        } else if (MILE.equalsIgnoreCase(from) && VERSTA.equalsIgnoreCase(to)) {
            new MileToMeter(new MeterToVersta());
        } else if (VERSTA.equalsIgnoreCase(from) && MILE.equalsIgnoreCase(to)) {
            new VerstaToMeter(new MeterToMile());
        }

        return value -> {
            throw new UnsupportedOperationException("Conversion is not supported");
        };
    }
}
