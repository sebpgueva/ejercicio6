import java.io.*;
import java.net.*;
import java.util.concurrent.locks.ReentrantLock;

public class SharedMemoryServer {
    private static String sharedData = "Initial Data";
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String clientInput;

                while ((clientInput = in.readLine()) != null) {
                    if (clientInput.startsWith("read")) {
                        lock.lock();
                        try {
                            out.println("Shared Data: " + sharedData);
                        } finally {
                            lock.unlock();
                        }
                    } else if (clientInput.startsWith("write")) {
                        String newData = clientInput.substring(6); 
                        lock.lock();
                        try {
                            sharedData = newData;
                            out.println("Data Written: " + sharedData);
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        out.println("Invalid Command");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
