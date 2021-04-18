package shein.dmitriy.diasoft.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sql2o.Sql2o;
import shein.dmitriy.diasoft.interfaces.IUserProvider;

@Component
public class UserProvider implements IUserProvider {
    private final Sql2o sql2o;

    @Autowired
    public UserProvider(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
}
