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
                System.out.println("Conexion: " + c);
                System.out.println("Escuchando: " + escuchandoSocket);
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
            System.out.println("Conexión :" + e.getMessage());
        }
    }

    public void run() {
        try {
            String datos = entrada.readUTF();
            if (datos.startsWith("buscarPalabra:")) {
                String palabra = datos.substring(14);
                buscarPalabraEnBD(palabra);
            } else if (datos.startsWith("agregarPalabra:")) {
                String contenido = datos.substring(15); // O ajusta el índice según necesites
                String[] partes = contenido.split(",", 2);
                if (partes.length < 2) {
                    salida.writeUTF("Error: formato incorrecto para agregar.");
                } else {
                    String palabra = partes[0];
                    String significado = partes[1];
                    agregarPalabraEnBD(palabra, significado);
                }
            }
            socketCliente.close();
        } catch (EOFException e) {
            System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }

    private void buscarPalabraEnBD(String palabra) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dsjavatcp",
                    "root",
                    "");
            String consulta = "SELECT significado FROM palabras WHERE palabra = ?";
            PreparedStatement pstmt = conn.prepareStatement(consulta);
            pstmt.setString(1, palabra);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                salida.writeUTF("El significado de '" + palabra + "' es: " + rs.getString("significado"));
            } else {
                salida.writeUTF("La palabra '" + palabra + "' no está almacenada en la base de datos");
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al buscar en la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al enviar datos al cliente: " + e.getMessage());
        }
    }

    private void agregarPalabraEnBD(String palabra, String significado) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dsjavatcp",
                    "root",
                    "");
            String consulta = "INSERT INTO palabras (PALABRA, SIGNIFICADO) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(consulta);
            statement.setString(1, palabra);
            statement.setString(2, significado);
            statement.executeUpdate();
            salida.writeUTF("El significado de " + palabra + " fue ingresada con exito.");
            statement.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error al agregar palabra en la base de datos: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error al enviar datos al cliente: " + e.getMessage());
        }
    }
}
