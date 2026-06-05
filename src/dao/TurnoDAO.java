package src.dao;
import java.sql.*;
import src.conexion.conexion;

public class TurnoDAO {

    public boolean horarioDisponible(String fecha, String hora) {

        // CAMBIO: se agrega AND estado != 'Cancelado' para que los turnos cancelados
        // vuelvan a aparecer como disponibles en el combo de horarios de RegistroTurno.
        String sql = "SELECT COUNT(*) FROM turnos WHERE fecha = ? AND hora = ? AND estado != 'Cancelado'";

        try (
            Connection con = conexion.getConexion();
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


