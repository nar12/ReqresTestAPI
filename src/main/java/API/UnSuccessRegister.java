package API;

public class UnSuccessRegister {
    private String error;

    public String getError() {
        return error;
    }

    public UnSuccessRegister() {
    }

    public UnSuccessRegister(String error) {
        this.error = error;
    }
}
