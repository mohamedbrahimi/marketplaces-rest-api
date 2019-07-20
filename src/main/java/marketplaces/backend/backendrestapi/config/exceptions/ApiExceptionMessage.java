package marketplaces.backend.backendrestapi.config.exceptions;

import marketplaces.backend.backendrestapi.config.global.ApiMessageBody;

public class ApiExceptionMessage {
    private String code;
    private ApiMessageBody body;
    private String additionalMessage;

    public ApiExceptionMessage(String code, ApiMessageBody body) {
        this.code = code;
        this.body = body;
    }

    public ApiExceptionMessage(String code, ApiMessageBody body, String additionalMessage) {
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

    public ApiMessageBody getBody() {
        return body;
    }

    public void setBody(ApiMessageBody body) {
        this.body = body;
    }

    public String getAdditionalMessage() {
        return additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }
}
