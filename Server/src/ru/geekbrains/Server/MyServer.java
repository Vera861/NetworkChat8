package ru.geekbrains.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private static MyServer server;

    public static MyServer getServer() {
        return server;
    }

    private final int PORT = 8189;
    private List<ClientHandler> clients;
    private AuthService authService;

    public MyServer() {
        server = this;
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Ждем подключения клиента");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                socket.setSoTimeout(120000);
                while (true) {
                    new ClientHandler(socket);
                }
            }
        } catch (IOException e) {
            try {
                if (e instanceof SocketTimeoutException) {
                    throw new SocketTimeoutException();
                } else {
                    e.printStackTrace();
                }
            } catch (SocketTimeoutException ste) {
                System.out.println("Время для авторизации истекло");
            }
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public synchronized void broadcastMsg(String msg) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMsg(msg);
        }
    }

    public synchronized void sendMsgToClient(ClientHandler fromClient, String nickTo, String msg) {
        for (ClientHandler client : clients) {
            if (client.getName().equals(nickTo)) {
                client.sendMsg("от пользователя " + fromClient.getName() + ": " + msg);
                fromClient.sendMsg("клиенту " + nickTo + ": " + msg);
                return;
            }
        }
        fromClient.sendMsg("Участник с ником \"" + nickTo + "\" не найден");
    }

    public synchronized void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clients");
        for (ClientHandler client : clients) {
            sb.append(" ").append(client.getName());
        }
        broadcastMsg(sb.toString());
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public AuthService getAuthService() {
        return authService;
    }
}
