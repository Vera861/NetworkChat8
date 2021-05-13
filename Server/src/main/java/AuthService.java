public interface AuthService {
    void start();

    String getNickByLoginPass(String login, String password);

    void chageNickByLoginPass(String login, String password);

    void stop();
}
