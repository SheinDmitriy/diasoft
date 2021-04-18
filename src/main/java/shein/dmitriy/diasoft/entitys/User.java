package shein.dmitriy.diasoft.entitys;

import lombok.Data;

@Data
public class User {

    private int userID;

    private String name;
    private String pass;
    private String mail;
}
