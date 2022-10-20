package API;

public class SuccessLogin {
    private String token;

    public String getToken() {
        return token;
    }

    public SuccessLogin() {
    }

    public SuccessLogin(String token) {
        this.token = token;
    }
}
