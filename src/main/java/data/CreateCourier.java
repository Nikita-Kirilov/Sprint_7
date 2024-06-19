package data;

public class CreateCourier {
    public final String login;
    public final String password;

    public final String firstName;

    public CreateCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
}
