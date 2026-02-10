import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import src.conexion.conexion;

public class DetalleTurno extends JFrame {

    private final Color AZUL_OSCURO   = new Color(18, 44, 80);
    private final Color MARRON_FONDO  = new Color(225, 214, 198);
    private final Color MARRON_CUADRO = new Color(240, 232, 220);
    private final Color BORDE_SUAVE   = new Color(170, 160, 145);

    private final DefaultTableModel modeloTabla;
    private final int idTurno;

    private JLabel lblCliente, lblTelefono, lblCorreo,
                   lblPatente, lblServicio, lblFecha, lblHora;

    private JComboBox<String> comboEstado;

    public DetalleTurno(DefaultTableModel modeloTabla, int idTurno) {
        this.modeloTabla = modeloTabla;
        this.idTurno = idTurno;

        setTitle("DETALLE DE TURNO");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(MARRON_FONDO);
        setContentPane(fondo);

        // --- CABECERA ---
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setOpaque(false);
        panelCabecera.setBorder(new EmptyBorder(30, 80, 10, 80)); 

        JLabel titulo = new JLabel("DETALLE DE TURNO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(AZUL_OSCURO);
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        JSeparator linea = new JSeparator();
        linea.setForeground(AZUL_OSCURO);
        linea.setBackground(AZUL_OSCURO);

        panelCabecera.add(titulo, BorderLayout.NORTH);
        panelCabecera.add(linea, BorderLayout.CENTER);
        fondo.add(panelCabecera, BorderLayout.NORTH);

        // --- CUERPO ---
        JPanel contenedorCentral = new JPanel(new GridBagLayout());
        contenedorCentral.setOpaque(false);

        JPanel cuadro = new JPanel(new GridLayout(0, 2, 20, 12)); 
        cuadro.setBackground(MARRON_CUADRO);
        cuadro.setBorder(new CompoundBorder(
                new LineBorder(BORDE_SUAVE, 1, true),
                new EmptyBorder(30, 50, 30, 50)
        ));
        cuadro.setPreferredSize(new Dimension(750, 480));

        lblCliente  = valor("");
        lblTelefono = valor("");
        lblCorreo   = valor("");
        lblPatente  = valor("");
        lblServicio = valor("");
        lblFecha    = valor("");
        lblHora     = valor("");

        cuadro.add(label("Cliente:"));   cuadro.add(lblCliente);
        cuadro.add(label("Teléfono:"));  cuadro.add(lblTelefono);
        cuadro.add(label("Correo:"));    cuadro.add(lblCorreo);
        cuadro.add(label("Patente:"));   cuadro.add(lblPatente);
        cuadro.add(label("Fecha:"));     cuadro.add(lblFecha);
        cuadro.add(label("Hora:"));      cuadro.add(lblHora);
        cuadro.add(label("Servicio:"));  cuadro.add(lblServicio);

        cuadro.add(label("Estado:"));
        comboEstado = new JComboBox<>(new String[]{"Terminado", "En proceso", "Cancelado"});
        comboEstado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        cuadro.add(comboEstado);

        contenedorCentral.add(cuadro);
        fondo.add(contenedorCentral, BorderLayout.CENTER);

        // --- BOTONES ---
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 25));
        botones.setOpaque(false);

        JButton btnGuardar = crearBotonRelleno("Guardar");
        JButton btnCerrar  = crearBotonBorde("Cerrar");

        btnGuardar.addActionListener(e -> guardarEstado());
        btnCerrar.addActionListener(e -> dispose());

        botones.add(btnGuardar);
        botones.add(btnCerrar);
        fondo.add(botones, BorderLayout.SOUTH);

        cargarDatosDesdeBD();
    }

    private void cargarDatosDesdeBD() {
        String sql = "SELECT c.nombre, c.apellido, c.telefono, c.correo, v.patente, s.nombre_servicio, " +
                     "t.fecha, t.hora, t.estado FROM turnos t " +
                     "INNER JOIN clientes c ON t.id_cliente = c.id_cliente " +
                     "INNER JOIN vehiculos v ON t.id_vehiculo = v.id_vehiculo " +
                     "INNER JOIN servicios s ON t.id_servicio = s.id_servicio " +
                     "WHERE t.id_turno = ?";
        try (Connection con = conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTurno);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblCliente.setText(rs.getString("nombre") + " " + rs.getString("apellido"));
                lblTelefono.setText(rs.getString("telefono"));
                lblCorreo.setText(rs.getString("correo"));
                lblPatente.setText(rs.getString("patente"));
                lblServicio.setText(rs.getString("nombre_servicio"));
                lblFecha.setText(rs.getDate("fecha").toString());
                lblHora.setText(rs.getTime("hora").toString());
                
                String est = rs.getString("estado");
                if (est == null) est = "En proceso";
                
                if (est.equalsIgnoreCase("Confirmado") || est.equalsIgnoreCase("Terminado")) {
                    comboEstado.setSelectedItem("Terminado");
                } else if (est.equalsIgnoreCase("Cancelado")) {
                    comboEstado.setSelectedItem("Cancelado");
                } else {
                    comboEstado.setSelectedItem("En proceso");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }

    private void guardarEstado() {
        String ui = comboEstado.getSelectedItem().toString();
        
        try (Connection con = conexion.getConexion()) {

            String sql = "UPDATE turnos SET estado=? WHERE id_turno=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, ui);
                ps.setInt(2, idTurno);
                ps.executeUpdate();
                actualizarTabla(ui);
            }

            if (ui.equalsIgnoreCase("Cancelado")) {
                JOptionPane.showMessageDialog(this, "Turno cancelado.");
            } else {
                JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.");
            }

            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar en BD: " + e.getMessage());
        }
    }

    private void eliminarDeTabla() {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((int) modeloTabla.getValueAt(i, 0) == idTurno) {
                modeloTabla.removeRow(i);
                break;
            }
        }
    }

    private void actualizarTabla(String est) {
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if ((int) modeloTabla.getValueAt(i, 0) == idTurno) {
                modeloTabla.setValueAt(est, i, 5);
                break;
            }
        }
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(AZUL_OSCURO);
        return l;
    }

    private JLabel valor(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        return l;
    }

    private JButton crearBotonRelleno(String t) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(180, 45));
        b.setBackground(AZUL_OSCURO);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton crearBotonBorde(String t) {
        JButton b = new JButton(t);
        b.setPreferredSize(new Dimension(180, 45));
        b.setForeground(AZUL_OSCURO);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(false);
        b.setBorder(new LineBorder(AZUL_OSCURO, 2, true));
        return b;
    }
}
