package marketplaces.backend.backendrestapi.config.exceptions.unknown;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;

public class ApiRequestUnknownException extends RuntimeException {

    private ApiExceptionMessage apiExceptionMessage;
    public ApiRequestUnknownException(ApiExceptionMessage apiExceptionMessage) {
        this.apiExceptionMessage = apiExceptionMessage;
    }

    public ApiExceptionMessage getApiExceptionMessage() {
        return apiExceptionMessage;
    }
}
