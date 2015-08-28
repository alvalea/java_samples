package alem.java_samples.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientImpl {
    
    static String HOST      = "localhost";
    static int PORT         = 5454;
    static int BUFFER_SIZE  = 256;

    static void sendRequest(SocketChannel client) throws IOException {
        String message = "Hello from client";
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        client.write(buffer);
        Logger.getLogger(ClientImpl.class.getName()).log(Level.INFO, message);
    }
    
    static void receiveResponse(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        client.read(buffer);
        String response = new String(buffer.array()).trim();
        Logger.getLogger(ClientImpl.class.getName()).log(Level.INFO,
                "Response: {0}", response);
    }
    
    static void process(SocketChannel client) throws IOException, InterruptedException {
        client.connect(new InetSocketAddress(HOST, PORT));

        Logger.getLogger(ClientImpl.class.getName()).log(Level.INFO,
                "Connected to server");
        
        sendRequest(client);
        receiveResponse(client);
    }

    static void run() throws IOException, InterruptedException {
        try (SocketChannel client = SelectorProvider.provider().openSocketChannel()) {
            process(client);
        }
    }
}

public class Client {

    public static void main(String[] args) {
        try {
            ClientImpl.run();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
