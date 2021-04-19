package shein.dmitriy.diasoft.utilits;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CurrDate {
    public String getDate() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
