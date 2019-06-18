package marketplaces.backend.backendrestapi.config.exceptions.custom;

import marketplaces.backend.backendrestapi.config.exceptions.ApiExceptionMessage;

public class ApiRequestException extends RuntimeException {

    private ApiExceptionMessage apiExceptionMessage;
    public ApiRequestException(ApiExceptionMessage apiExceptionMessage) {
        this.apiExceptionMessage = apiExceptionMessage;
    }

    public ApiExceptionMessage getApiExceptionMessage() {
        return apiExceptionMessage;
    }
}
