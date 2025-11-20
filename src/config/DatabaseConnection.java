package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/tfi_programacion2";
    private static final String USER = "admin";  // Cambiá según tu configuración
    private static final String PASSWORD = "";  // Cambiá según tu configuración

    /**
     * Obtiene una conexión a la base de datos.
     * 
     * @return Connection objeto de conexión a MySQL
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado. Asegurate de tener el conector en el classpath.", e);
        }
    }

    /**
     * Cierra una conexión de forma segura.
     * 
     * @param conn la conexión a cerrar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}