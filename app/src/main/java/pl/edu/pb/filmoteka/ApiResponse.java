package pl.edu.pb.filmoteka;

public class ApiResponse {
    private final boolean success;
    private final String responseData;
    private final String errorMessage;

    // Konstruktor dla sukcesu
    public ApiResponse(boolean success, String responseData) {
        this.success = success;
        this.responseData = responseData;
        this.errorMessage = null;
    }

    // Konstruktor dla błędu
    public ApiResponse(boolean success, String responseData, String errorMessage) {
        this.success = success;
        this.responseData = responseData;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getResponseData() {
        return responseData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}