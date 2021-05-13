public interface AuthService {
    void start();

    String getNickByLoginPass(String login, String password);

    String changNick(String login, String nick);

    void stop();
}
