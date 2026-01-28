package src.dao;
import java.sql.*;
import src.Conexion.Conexion;

public class TurnoDAO {

    public boolean horarioDisponible(String fecha, String hora) {

        String sql = "SELECT COUNT(*) FROM turnos WHERE fecha = ? AND hora = ?";

        try (
            Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, fecha);
            ps.setString(2, hora);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) == 0; // 0 = libre
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}


