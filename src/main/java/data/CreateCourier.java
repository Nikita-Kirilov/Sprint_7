package data;

public class CreateCourier {
     private String login;
     private String password;
     private String firstName;

    public CreateCourier withLogin(String login) {
        this.login = login;
        return this;
    }

    public CreateCourier withPassword(String password) {
        this.password = password;
        return this;
    }

    public CreateCourier withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }
}
