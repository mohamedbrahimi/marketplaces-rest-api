package marketplaces.backend.backendrestapi.config.exceptions;

public class ApiExceptionMessage {
    private String code;
    private ApiExceptionMessageBody body;
    private String additionalMessage;

    public ApiExceptionMessage(String code, ApiExceptionMessageBody body) {
        this.code = code;
        this.body = body;
    }

    public ApiExceptionMessage(String code, ApiExceptionMessageBody body, String additionalMessage) {
        this.code = code;
        this.body = body;
        this.additionalMessage = additionalMessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ApiExceptionMessageBody getBody() {
        return body;
    }

    public void setBody(ApiExceptionMessageBody body) {
        this.body = body;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
