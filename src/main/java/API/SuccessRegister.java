package API;

public class SuccessRegister {
    private Integer id;
    private String token;

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public SuccessRegister() {
    }
    public SuccessRegister(Integer id, String token) {
        this.id = id;
        this.token = token;
    }
}
