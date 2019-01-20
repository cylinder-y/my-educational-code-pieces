package by.bsu.converters.controller.result;

public class ErrorResult {
    private final String reason;
    private final String url;

    public ErrorResult(final String reason, final String url) {
        this.reason = reason;
        this.url = url;
    }

    public String getReason() {
        return reason;
    }

    public String getUrl() {
        return url;
    }
}
