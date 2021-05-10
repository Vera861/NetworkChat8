import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class BaseAuthService implements AuthService {
    //    Map<String, User> users;
    private static Connection postgresConnection;
    private static PreparedStatement stmt;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try (Connection postgresConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Users", "postgres", "123");) {
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        users = new HashMap<>();
//        users.put("Evka", new User("Evka", "123", "Evka"));
//        users.put("login2", new User("login2", "pass2", "nick2"));
//        users.put("login3", new User("login3", "pass3", "nick3"));
    }

    @Override
    public String getNickByLoginPass(String loginEnter, String password) {
        try {
            PreparedStatement userResSet = postgresConnection.prepareStatement("SELECT nick FROM Users where login = ? and pass = ?");
            String login = loginEnter;
            String pass = password;
            userResSet.setString(1,loginEnter);
            userResSet.setString(2,password);
            ResultSet nickResSet = userResSet.executeQuery();
            if (nickResSet.next()) {
                return nickResSet.getString(1);
            } else return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
//        User user = users.get(login);
//        if (user != null && user.getPassword().equals(password)) {
//            return user.getNick();
//        }
//        return null;

    @Override
    public void stop() {

        System.out.println("Сервис остановился");
    }
}
