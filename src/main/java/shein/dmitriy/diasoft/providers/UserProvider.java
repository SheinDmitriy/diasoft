package shein.dmitriy.diasoft.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;
import shein.dmitriy.diasoft.interfaces.IUserProvider;

@Component
public class UserProvider implements IUserProvider {
    private final Sql2o sql2o;
    private short actionType;

    private static final String CHECK_USER = "select name, pass from tuser where name = :u_name and pass = :u_pass";

    private static final String INSERT_USER = "insert into tuser(name, pass, mail) values(:u_name, :u_pass, :u_mail)";

    private static final String AUDIT_REG = "insert into taudit(userid, actiontype) values((select userid from tuser where (name = :u_name and pass = :u_pass)), :u_actionType)";

    @Autowired
    public UserProvider(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public boolean addUser(UserDTO userDTO) {
        try (Connection connection = sql2o.open()) {
            if(connection.createQuery(CHECK_USER, false)
                    .addParameter("u_name", userDTO.getName())
                    .addParameter("u_pass", userDTO.getPass())
                    .setColumnMappings(User.COLUMN_MAPPINGS).executeAndFetchFirst(User.class) != null){
                return false;
            }else {
                connection.createQuery(INSERT_USER, true)
                        .addParameter("u_name", userDTO.getName())
                        .addParameter("u_pass", userDTO.getPass())
                        .addParameter("u_mail", userDTO.getMail())
                        .setColumnMappings(User.COLUMN_MAPPINGS)
                        .executeUpdate();
                return true;
            }
        }
    }

    @Override
    public void auditReg(UserDTO userDTO) {
        actionType = 1;
        try (Connection connection = sql2o.open()) {
            connection.createQuery(AUDIT_REG, true)
                    .addParameter("u_name", userDTO.getName())
                    .addParameter("u_pass", userDTO.getPass())
                    .addParameter("u_actionType", actionType)
                    .executeUpdate();
        }
    }
}
