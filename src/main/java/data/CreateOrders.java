package data;

import java.util.List;

public class CreateOrders {
    public final String firstName;
    public final String lastName;
    public final String address;
    public final String metroStation;
    public final String phone;
    public final Number rentTime;
    public final String deliveryDate;
    public final String comment;
    public final List<String> color;

    public CreateOrders(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

}
