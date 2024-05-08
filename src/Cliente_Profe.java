import java.io.*;
import java.net.*;

public class Cliente_Profe {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 5001;
        String filePath = "src/archivo.pdf"; // Ruta del archivo que se enviará

        try {
            Socket socket = new Socket(serverAddress, port);
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // Enviar el nombre y tamaño del archivo
            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, bytesRead);
            }

            fis.close();
            dos.close();
            socket.close();
            System.out.println("Archivo enviado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}