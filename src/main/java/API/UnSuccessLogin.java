package API;

public class UnSuccessLogin {
    private String error;

    public String getError() {
        return error;
    }

    public UnSuccessLogin() {
    }

    public UnSuccessLogin(String error) {
        this.error = error;
    }
}
