package shein.dmitriy.diasoft.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import shein.dmitriy.diasoft.dto.UserDTO;
import shein.dmitriy.diasoft.entitys.User;
import shein.dmitriy.diasoft.interfaces.IUserProvider;

import java.util.List;

@Component
public class UserProvider implements IUserProvider {
    private final Sql2o sql2o;

    @Value("${tokenTimeLimit}")
    private int tokenTimeLimit;

    private static final String FIND_USER = "select tuser.* from tuser where userid = :u_userid";

    private static final String CHECK_USER = "select tuser.* from tuser where name = :u_name and pass = :u_pass";

    private static final String INSERT_USER = "insert into tuser(name, pass, mail) values(:u_name, :u_pass, :u_mail)";

    private static final String AUDIT_REG = "insert into taudit(userid, actiontype) values((select userid from tuser where (name = :u_name and pass = :u_pass)), :u_actionType)";

    private static final String ADD_LOG = "insert into taudit(userid, actiontype) values(:u_userid, :u_actionType)";

    private static final String CHECK_IN_LOG = "SELECT tuser.* FROM tuser join taudit where tuser.UserID = taudit.UserID and taudit.ActionType = :u_actionType and tuser.UserID = :u_userid";

    private static final String CHECK_LOGOUT = "SELECT tuser.* FROM tuser join taudit where tuser.UserID = taudit.UserID and tuser.UserID = :u_userid and (SELECT MAX(taudit.actiontype) FROM taudit" +
            " join tuser where tuser.UserID = taudit.UserID and taudit.ActionType = :u_actionType and tuser.UserID = :u_userid)>(SELECT MAX(taudit.actiontype) FROM taudit join tuser" +
            " where tuser.UserID = taudit.UserID and taudit.ActionType = '3' and tuser.UserID = :u_userid)";

    private static final String ADD_TOKEN = "insert into taccesstoken(auditid, userid, expiredate) values((SELECT taudit.auditid FROM taudit where userid = :u_userid and " +
            "ActionDate = (select max(taudit.ActionDate) from taudit where userid = :u_userid and ActionType = '3')), :u_userid, ( adddate((select max(taudit.ActionDate) from taudit where userid = :u_userid and ActionType = '3'), interval +:u_tokenTimeLimit minute) ))";

    private static final String NOT_CONFIRMED_MAIL = "select * from tuser where userid not in (select tuser.userid from taudit join tuser " +
            "where ActionType = '2' and tuser.userid = taudit.userid)";

    private static final String LESS_TOKEN = "SELECT tuser.* FROM tuser join taccesstoken as tat on tuser.UserID = tat.UserID where ExpireDate = (select max(taccesstoken.ExpireDate) from taccesstoken where UserID = tat.UserID) and ExpireDate < :u_date";


    private static final String NO_LOGIN = "select * from tuser where userid not in (select tuser.userid from taudit join tuser " +
            "where ActionType = '3' and tuser.userid = taudit.userid)";

    private static final String FAIL_LOGIN = "select tuser.* from tuser join taudit where taudit.actiontype = '5' and tuser.userid = taudit.userid group by tuser.userid " +
            "order by count(taudit.actiontype) desc limit 4";

    @Autowired
    public UserProvider(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public boolean findUser(int userID) {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(FIND_USER, false)
                    .addParameter("u_userid", userID)
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetchFirst(User.class) != null;
        }
    }

    @Override
    public boolean checkUser(UserDTO userDTO){
        try (Connection connection = sql2o.open()) {
            if(connection.createQuery(CHECK_USER, false)
                    .addParameter("u_name", userDTO.getName())
                    .addParameter("u_pass", userDTO.getPass())
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetchFirst(User.class) != null){
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean addUser(UserDTO userDTO) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery(INSERT_USER, true)
                    .addParameter("u_name", userDTO.getName())
                    .addParameter("u_pass", userDTO.getPass())
                    .addParameter("u_mail", userDTO.getMail())
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeUpdate();
            return true;
        }
    }

    @Override
    public boolean auditReg(UserDTO userDTO, short aType) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery(AUDIT_REG, true)
                    .addParameter("u_name", userDTO.getName())
                    .addParameter("u_pass", userDTO.getPass())
                    .addParameter("u_actionType", aType)
                    .executeUpdate();
        }
        return true;
    }

    @Override
    public boolean auditCheckMailInLog(int userID, short aType){
        try (Connection connection = sql2o.open()) {
            if(connection.createQuery(CHECK_IN_LOG, false)
                    .addParameter("u_userid", userID)
                    .addParameter("u_actionType", aType)
                    .executeAndFetchFirst(User.class) != null)
                return true;
            return false;
        }
    }

    @Override
    public boolean auditAddMailLog(int userID, short aType) {
        try (Connection connection = sql2o.open()) {
            if (connection.createQuery(ADD_LOG, true)
                        .addParameter("u_userid", userID)
                        .addParameter("u_actionType", aType)
                        .executeUpdate() != null)
                return true;
            }
        return false;
    }

    @Override
    public boolean auditLogInPass(int userID, short aType) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery(ADD_LOG, true)
                    .addParameter("u_userid", userID)
                    .addParameter("u_actionType", aType)
                    .executeUpdate();}
            return true;

    }

    @Override
    public boolean auditLogOutCheck(int userID, short aType) {
        try (Connection connection = sql2o.open()) {
            if (connection.createQuery(CHECK_LOGOUT, false)
                    .addParameter("u_userid", userID)
                    .addParameter("u_actionType", aType)
                    .executeAndFetchFirst(User.class) != null)
                return true;
            return false;
        }
    }

    @Override
    public boolean auditAddLogOutLog(int userID, short aType) {
        try (Connection connection = sql2o.open()) {
            if (connection.createQuery(ADD_LOG, true)
                        .addParameter("u_userid", userID)
                        .addParameter("u_actionType", aType)
                        .executeUpdate() != null)
                return true;
            return false;
        }
    }

    @Override
    public boolean auditLogInFail(int userID, short aType) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery(ADD_LOG, true)
                    .addParameter("u_userid", userID)
                    .addParameter("u_actionType", aType)
                    .executeUpdate();
            return true;
        }
    }

    @Override
    public boolean addToken(int userID) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery(ADD_TOKEN, true)
                    .addParameter("u_userid", userID)
                    .addParameter("u_tokenTimeLimit", tokenTimeLimit)
                    .executeUpdate();
            return true;
        }
    }

    @Override
    public List<User> notConfirmedMail() {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(NOT_CONFIRMED_MAIL, true)
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public List<User> lessToken(String date) {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(LESS_TOKEN, true)
                    .addParameter("u_date", date)
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public List<User> noLogIn() {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(NO_LOGIN, true)
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetch(User.class);
        }
    }

    @Override
    public List<User> failLogin() {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(FAIL_LOGIN, true)
                    .setColumnMappings(User.COLUMN_MAPPINGS)
                    .executeAndFetch(User.class);
        }
    }
}
