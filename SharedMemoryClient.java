import java.io.*;
import java.net.*;

public class SharedMemoryClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            String userInput;
            System.out.println("Conectado al servidor. Escribe 'read' para leer los datos o 'write <data>' para escribir.");

            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);

                String serverResponse = in.readLine();
                if (serverResponse != null) {
                    System.out.println("Respuesta del servidor: " + serverResponse);
                }
            }
        } catch (IOException e) {git add .
git commit -m "Añadir archivos del sistema distribuido"
git push

            e.printStackTrace();
        }
    }
}
