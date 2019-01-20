package by.bsu.converters.service.weight;


import by.bsu.converters.service.Converter;
import by.bsu.converters.service.ConverterFactory;
import by.bsu.converters.service.magnitude.MinusOrderOfMagnitude;
import by.bsu.converters.service.magnitude.PlusOrderOfMagnitude;

public class WeightConverterFactory implements ConverterFactory {
    public static final ConverterFactory INSTANCE = new WeightConverterFactory();

    private static final String G = "g";
    private static final String LB = "lb";
    private static final String PUD = "pud";
    private static final String KG = "kg";

    @Override
    public Converter createConverter(final String from, final String to) {
        if (G.equalsIgnoreCase(from) && LB.equalsIgnoreCase(to)) {
            return new GramToLb();
        } else if (LB.equalsIgnoreCase(from) && G.equalsIgnoreCase(to)) {
            return new LbToGram();
        } else if (PUD.equalsIgnoreCase(from) && G.equalsIgnoreCase(to)) {
            return new PudToGram();
        } else if (G.equalsIgnoreCase(from) && PUD.equalsIgnoreCase(to)) {
            return new GramToPud();
        } else if (KG.equalsIgnoreCase(from) && G.equalsIgnoreCase(to)) {
            return new MinusOrderOfMagnitude(new MinusOrderOfMagnitude(new MinusOrderOfMagnitude()));
        } else if (G.equalsIgnoreCase(from) && KG.equalsIgnoreCase(to)) {
            return new PlusOrderOfMagnitude(new PlusOrderOfMagnitude(new PlusOrderOfMagnitude()));
        } else if (LB.equalsIgnoreCase(from) && PUD.equalsIgnoreCase(to)) {
            return new LbToGram(new GramToPud());
        } else if (PUD.equalsIgnoreCase(from) && LB.equalsIgnoreCase(to)) {
            return new PudToGram(new GramToLb());
        } else if (PUD.equalsIgnoreCase(from) && KG.equalsIgnoreCase(to)) {
            return new PudToGram(new MinusOrderOfMagnitude(new MinusOrderOfMagnitude(new MinusOrderOfMagnitude())));
        } else if (KG.equalsIgnoreCase(from) && PUD.equalsIgnoreCase(to)) {
            return new PlusOrderOfMagnitude(new PlusOrderOfMagnitude(new PlusOrderOfMagnitude(new GramToPud())));
        }

        return value -> {
            throw new UnsupportedOperationException("Conversion is not supported");
        };
    }
}
