import java.net.*;
import java.io.*;

public class basecliente {
    public static void main(String[] args) {
        try {
            int puertoServicio = 7896;
            Socket s = new Socket(args[1], puertoServicio);
            DataInputStream entrada = new DataInputStream(s.getInputStream());
            DataOutputStream salida = new DataOutputStream(s.getOutputStream());
            salida.writeUTF(args[0]);
            String datos = entrada.readUTF();
            System.out.println("Recibido: " + datos);
            s.close();
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}