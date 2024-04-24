import java.sql.*;

public class Conexiondb {
    private static final String URL = "jdbc:mysql://localhost:3306/diccionario";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    // Método para obtener la conexión a la base de datos
    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el controlador JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión: " + e.getMessage());
        }
        return conexion;
    }
}
