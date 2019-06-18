package marketplaces.backend.backendrestapi.config.exceptions;

public class ApiRequestException extends RuntimeException {

    private  ApiExceptionMessage apiExceptionMessage;
    public ApiRequestException(ApiExceptionMessage apiExceptionMessage) {
        this.apiExceptionMessage = apiExceptionMessage;
    }

    public ApiExceptionMessage getApiExceptionMessage() {
        return apiExceptionMessage;
    }
}
