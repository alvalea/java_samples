package alem.java_samples.nio.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerImpl {

    static String HOST = "localhost";
    static int PORT = 5454;
    static int BUFFER_SIZE = 256;

    static ExecutorService pool = Executors.newFixedThreadPool(4);

    static void configServerChannel(ServerSocketChannel serverChannel,
            Selector selector)
            throws IOException {
        serverChannel.bind(new InetSocketAddress(HOST, PORT));
        serverChannel.configureBlocking(false);
        int ops = serverChannel.validOps();
        serverChannel.register(selector, ops, null);

        Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
                "Server channel configured");
    }

    static void acceptClient(ServerSocketChannel serverChannel, Selector selector)
            throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);

        Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
                "Accepted new client: {0}", client);
    }
    
    static boolean checkClient(String input, SocketChannel client) {
        boolean result = true;
                
        if (input.isEmpty() && client.isOpen()) {
            result = false;
            
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
                    "[Thread: {0}] Client disconnected", Thread.currentThread().getName());
        }
        
        return result;
    }
    
    static void worker(SocketChannel client) {
        try (RandomAccessFile file = new RandomAccessFile("test.txt", "r");
             FileChannel channel = file.getChannel();)
        {
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            buffer.load();
            
            client.write(buffer);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String threadName = Thread.currentThread().getName();
        Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
            "[Thread: {0}] Response sent", threadName);
    }

    static void processData(String input, SocketChannel client) {
        pool.submit(() -> {
            if(checkClient(input, client)) {
                worker(client);
            }
        });
    }

    static void readData(SelectionKey ky) throws IOException {
        SocketChannel client = (SocketChannel) ky.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        try {
            client.read(buffer);
            String input = new String(buffer.array()).trim();
        
            Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
                    "[Thread: {0}] Message read from client: {1}",
                    new Object[]{Thread.currentThread().getName(), input});

            processData(input, client);
        } catch (IOException ex) {
            client.close();
            Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO, 
                    "[Thread: {0}] Client disconnected",
                    Thread.currentThread().getName());
        }
    }

    static void select(ServerSocketChannel serverChannel, Selector selector)
            throws IOException {
        while (true) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.INFO,
                    "Selecting: {0}", selector.keys().size());

            selector.select();
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey ky = iter.next();
                if (ky.isAcceptable()) {
                    acceptClient(serverChannel, selector);
                } else if (ky.isReadable()) {
                    readData(ky);
                }
                iter.remove();
            }
        }
    }

    static void process(ServerSocketChannel serverChannel, Selector selector)
            throws IOException {
        configServerChannel(serverChannel, selector);

        select(serverChannel, selector);
    }

    static void run() throws IOException {
        try (ServerSocketChannel serverChannel = SelectorProvider.provider().openServerSocketChannel();
                Selector selector = SelectorProvider.provider().openSelector();) {
            process(serverChannel, selector);
        }
    }
}

public class Server {

    public static void main(String[] args) {
        try {
            ServerImpl.run();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
