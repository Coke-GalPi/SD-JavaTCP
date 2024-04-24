import java.net.*;
import java.io.*;
import java.sql.*;

public class ServidorTCP {
    public static void main(String args[]) {
        try {
            int puertoServicio = 7896;
            ServerSocket escuchandoSocket = new ServerSocket(puertoServicio);

            while (true) {
                Socket socketCliente = escuchandoSocket.accept();
                Conexion c = new Conexion(socketCliente);
                System.out.println("Conexion Thread: " + c);

                DataInputStream entrada = new DataInputStream(socketCliente.getInputStream());
                String mensaje = entrada.readUTF();

                // Verificar los datos recibidos del cliente
                System.out.println("Datos recibidos del cliente:");
                System.out.println("Mensaje: " + mensaje);

                // Separar los valores usando el delimitador
                String[] valores = mensaje.split("\\|");
                String nombreArchivo = valores[0];
                String temaArchivo = valores[1];
                String rutaArchivo = valores[2];

                // Mostrar los valores recibidos
                System.out.println("Nombre del archivo: " + nombreArchivo);
                System.out.println("Tema del archivo: " + temaArchivo);
                System.out.println("Ruta del archivo: " + rutaArchivo);

                // Realizar la consulta SQL
                guardarArchivo(nombreArchivo, temaArchivo, rutaArchivo);

                // Cerrar conexión
                socketCliente.close();
                System.out.println("Conexión cerrada con el cliente.");

            }
        } catch (IOException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void guardarArchivo(String nombreArchivo, String temaArchivo, String rutaArchivo)
            throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = Conexiondb.obtenerConexion();
            String sql = "INSERT INTO documentos (NOMBRE, TEMA, URL) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreArchivo);
            pstmt.setString(2, temaArchivo);
            pstmt.setString(3, rutaArchivo);
            pstmt.executeUpdate();

            System.out.println("Información del archivo guardada en la base de datos.");
        } finally {
            // Cerrar recursos
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
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
