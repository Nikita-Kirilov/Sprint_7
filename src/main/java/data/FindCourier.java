package data;

public class FindCourier {

    private String login;
    private String password;

    public FindCourier withLogin(String login) {
        this.login = login;
        return this;
    }

    public FindCourier withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
