import java.net.*;
import java.io.*;

public class ServidorTCP {
    public static void main(String args[]) {
        try {
            int puertoServicio = 7896;
            ServerSocket escuchandoSocket = new ServerSocket(puertoServicio);
            while (true) {
                Socket socketCliente = escuchandoSocket.accept();
                Conexion c = new Conexion(socketCliente);
                System.out.println("Conexion Thread: " + c);
            }
        } catch (IOException e) {
            System.out.println("Escuchando:" + e.getMessage());
        }
    }
}

class Conexion extends Thread {
    DataInputStream entrada;
    DataOutputStream salida;
    Socket socketCliente;

    public Conexion(Socket unSocketCliente) {
        try {
            socketCliente = unSocketCliente;
            entrada = new DataInputStream(socketCliente.getInputStream());
            salida = new DataOutputStream(socketCliente.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Conexión:" + e.getMessage());
        }
    }

    public void run() {
        try {
            String datos = entrada.readUTF();
            salida.writeUTF(datos);
            socketCliente.close();
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}