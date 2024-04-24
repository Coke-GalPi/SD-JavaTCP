import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nIngrese la ip a la que se va a conectar: ");
        String ipService = scanner.nextLine();
        System.out.print("Ingrese el mensaje: ");
        String mensaje = scanner.nextLine();
        try {
            int puertoServicio = 7896;
            Socket s = new Socket(ipService, puertoServicio);
            DataInputStream entrada = new DataInputStream(s.getInputStream());
            DataOutputStream salida = new DataOutputStream(s.getOutputStream());
            salida.writeUTF(mensaje);
            String datos = entrada.readUTF();
            System.out.println("\nRecibido: " + datos);
            s.close();
        } catch (UnknownHostException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        scanner.close();
    }
}