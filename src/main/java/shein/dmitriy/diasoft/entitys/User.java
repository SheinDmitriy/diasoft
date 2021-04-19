package shein.dmitriy.diasoft.entitys;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    private int userID;

    private String name;
    private String pass;
    private String mail;

    public static final Map<String, String> COLUMN_MAPPINGS = new HashMap<>();

    static {
        COLUMN_MAPPINGS.put("UserID", "userID");
        COLUMN_MAPPINGS.put("Name", "name");
        COLUMN_MAPPINGS.put("Pass", "pass");
        COLUMN_MAPPINGS.put("Mail", "mail");
    }
}
