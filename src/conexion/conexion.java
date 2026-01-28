package src.Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/turnero_automotriz"
        + "?useSSL=false"
        + "&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConexion() {
        Connection con = null;

        try {
            // FORZAMOS LA CARGA DEL DRIVER
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa");

        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver MySQL no encontrado");
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }

        return con;
    }
}

