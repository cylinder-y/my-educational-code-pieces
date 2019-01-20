package by.bsu.converters.controller.result;

public class AvailableConverterTypes {
    private final String info;
    private final String[] converters;

    public AvailableConverterTypes(final String info, final String[] converters) {
        this.info = info;
        this.converters = converters;
    }

    public String getInfo() {
        return info;
    }

    public String[] getConverters() {
        return converters;
    }
}
