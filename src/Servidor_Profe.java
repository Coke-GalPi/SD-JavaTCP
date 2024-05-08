import java.io.*;
import java.net.*;

public class Servidor_Profe {
    public static void main(String[] args) {
        int port = 5001;
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor escuchando en el puerto " + port);

            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado");

                // Crear flujo de entrada para recibir el archivo
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                String fileName = dis.readUTF(); // Leer el nombre del archivo

                fileName = "ruta\\" + fileName;
                long fileSize = dis.readLong(); // Leer el tamaño del archivo

                FileOutputStream fos = new FileOutputStream(fileName);

                byte[] buffer = new byte[4096];
                int bytesRead;
                long bytesRemaining = fileSize;

                while ((bytesRead = dis.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) > 0) {
                    fos.write(buffer, 0, bytesRead);
                    bytesRemaining -= bytesRead;
                }

                fos.close();
                dis.close();
                System.out.println("Archivo " + fileName + " recibido");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
