import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class InstantMessenger {

    private static final int SERVER_PORT = 4567;

    private String sender;
    private final List<MessageListener> listeners = new ArrayList<MessageListener>();

    public InstantMessenger() {
        startServer();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void addMessageListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void sendMessage(String destinationAddress, String message)
            throws UnknownHostException, IOException {
        final Socket socket = new Socket(destinationAddress, SERVER_PORT);
        final DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(sender);
        out.writeUTF(message);
        socket.close();
    }

    private void notifyListeners(Peer sender, String message) {
        synchronized (listeners) {
            for (MessageListener listener : listeners) {
                listener.messageReceived(sender, message);
            }
        }
    }

    private void startServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
                    while (!Thread.interrupted()) {
                        final Socket socket = serverSocket.accept();
                        final DataInputStream in = new DataInputStream(socket.getInputStream());
                        final String senderName = in.readUTF();
                        final String message = in.readUTF();
                        final String address = ((InetSocketAddress) socket.getRemoteSocketAddress())
                                .getAddress()
                                .getHostAddress();
                        socket.close();
                        notifyListeners(new Peer(senderName, address), message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}