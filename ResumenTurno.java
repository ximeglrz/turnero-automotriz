import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import src.conexion.conexion;

public class ResumenTurno extends JFrame {

    // COLORES
    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    // DATOS
    private String nombre, apellido, telefono, correo;
    private String patente, fecha, hora, servicio;

    public ResumenTurno(
            String nombre, String apellido, String telefono, String correo,
            String patente, String fecha, String hora, String servicio
    ) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.patente = patente;
        this.fecha = fecha;
        this.hora = hora;
        this.servicio = servicio;

        setTitle("Resumen del Turno");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel fondo = new JPanel(null);
        fondo.setBackground(COLOR_AZUL_OSCURO_FONDO);
        setContentPane(fondo);

        int anchoPantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
        int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;

        int anchoPanel = 650;
        int altoPanel = 600;

        int xPanel = (anchoPantalla - anchoPanel) / 2;
        int yPanel = (altoPantalla - altoPanel) / 2;

        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBounds(xPanel, yPanel, anchoPanel, altoPanel);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        fondo.add(panel);

        JLabel lblTitulo = new JLabel("Resumen del Turno");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblTitulo.setBounds(0, 20, anchoPanel, 35);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo);

        int y = 90;
        agregarDato(panel, "Cliente:", nombre + " " + apellido, y); y += 40;
        agregarDato(panel, "Teléfono:", telefono, y); y += 40;
        agregarDato(panel, "Correo:", correo, y); y += 40;
        agregarDato(panel, "Patente:", patente, y); y += 40;
        agregarDato(panel, "Fecha:", fecha, y); y += 40;
        agregarDato(panel, "Hora:", hora, y); y += 40;
        agregarDato(panel, "Servicio:", servicio, y);

        // BOTONES 
        int yBot = altoPanel - 100;
        JButton btnVolver = crearBoton("Volver", 30, yBot);
        JButton btnGuardar = crearBoton("Guardar", 170, yBot);
        JButton btnImprimir = crearBoton("Imprimir", 310, yBot);
        JButton btnEnviar = crearBoton("Enviar", 450, yBot);
        
        panel.add(btnVolver);
        panel.add(btnGuardar);
        panel.add(btnImprimir);
        panel.add(btnEnviar);
        
        JButton btnAgenda = crearBoton("Agenda", 90, yBot + 60);
        JButton btnCerrar = crearBoton("Cerrar sesión", 250, yBot + 60);
        JButton btnSalir = crearBoton("Salir", 430, yBot + 60);
        
        panel.add(btnAgenda);
        panel.add(btnCerrar);
        panel.add(btnSalir);
        
        // ACCIONES
        btnVolver.addActionListener(e -> {
            new RegistroTurnos().setVisible(true);
            dispose();
        });
        
        btnGuardar.addActionListener(e -> guardarTurnoCompleto());
        
        btnImprimir.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Comprobante impreso (simulado)") 
        ); 
        
        btnEnviar.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Correo enviado (simulado)") 
        );
        
        btnAgenda.addActionListener(e -> {
            new VerTurnosVentana().setVisible(true); dispose();
            });
        
        btnCerrar.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });
        
        btnSalir.addActionListener(e -> System.exit(0));
    }

    /* ================= VALIDACIONES ================= */

    private boolean validarCampos() {
        return !(nombre.isBlank() || apellido.isBlank() || telefono.isBlank()
                || correo.isBlank() || patente.isBlank());
    }

    private boolean validarTelefono() {
        return telefono.matches("\\d+");
    }

    private boolean validarCorreo() {
        return correo.matches("^[^@]+@[^@]+\\.[a-zA-Z]{2,}$");
    }

    private boolean validarFecha() {
        LocalDate hoy = LocalDate.now();
        LocalDate fechaTurno = LocalDate.parse(fecha);
        return !fechaTurno.isBefore(hoy);
    }

    private boolean validarHora() {
        LocalTime h = LocalTime.parse(hora);
        return !h.isBefore(LocalTime.of(8, 0)) && !h.isAfter(LocalTime.of(18, 0));
    }

    private boolean existeTurno() throws Exception {

        Connection con = conexion.conectar();

        String sql = "SELECT COUNT(*) FROM turnos WHERE fecha = ? AND hora = ? AND estado <> 'Cancelado'";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, fecha);
        ps.setString(2, hora);

        ResultSet rs = ps.executeQuery();
        rs.next();

        boolean existe = rs.getInt(1) > 0;

        rs.close();
        ps.close();
        con.close();

        return existe;
    }

    /* ================= GUARDADO ================= */

    private int guardarCliente() throws Exception {

        Connection con = conexion.conectar();

        String sql = "INSERT INTO clientes (nombre, apellido, telefono, correo) VALUES (?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, nombre);
        ps.setString(2, apellido);
        ps.setString(3, telefono);
        ps.setString(4, correo);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int idCliente = rs.getInt(1);

        rs.close();
        ps.close();
        con.close();

        return idCliente;
    }

    private int guardarVehiculo(int idCliente) throws Exception {

        Connection con = conexion.conectar();

        String sql = "INSERT INTO vehiculos (patente, id_cliente) VALUES (?, ?)";

        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, patente);
        ps.setInt(2, idCliente);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int idVehiculo = rs.getInt(1);

        rs.close();
        ps.close();
        con.close();

        return idVehiculo;
    }

    private int obtenerIdServicio() throws Exception {

        Connection con = conexion.conectar();

        String sql = "SELECT id_servicio FROM servicios WHERE nombre_servicio = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, servicio);

        ResultSet rs = ps.executeQuery();
        rs.next();
        int idServicio = rs.getInt("id_servicio");

        rs.close();
        ps.close();
        con.close();

        return idServicio;
    }

    private void guardarTurnoCompleto() {

        try {
            if (!validarCampos()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios");
                return;
            }

            if (!validarTelefono()) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener solo números");
                return;
            }

            if (!validarCorreo()) {
                JOptionPane.showMessageDialog(this, "Correo electrónico inválido");
                return;
            }

            if (!validarFecha()) {
                JOptionPane.showMessageDialog(this, "La fecha no puede ser anterior a hoy");
                return;
            }

            if (!validarHora()) {
                JOptionPane.showMessageDialog(this, "Horario permitido: 08:00 a 18:00");
                return;
            }

            if (existeTurno()) {
                JOptionPane.showMessageDialog(this, "Ya existe un turno para esa fecha y hora");
                return;
            }

            int idCliente = guardarCliente();
            int idVehiculo = guardarVehiculo(idCliente);
            int idServicio = obtenerIdServicio();

            Connection con = conexion.conectar();

            String sql = "INSERT INTO turnos (id_cliente, id_vehiculo, id_servicio, fecha, hora, estado) " +
                         "VALUES (?, ?, ?, ?, ?, 'Confirmado')";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, idCliente);
            ps.setInt(2, idVehiculo);
            ps.setInt(3, idServicio);
            ps.setString(4, fecha);
            ps.setString(5, hora);

            ps.executeUpdate();
            ps.close();
            con.close();

            JOptionPane.showMessageDialog(this, "Turno registrado correctamente");

            new MenuPrincipal().setVisible(true);
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar turno: " + e.getMessage());
        }
    }

    private void agregarDato(JPanel panel, String titulo, String valor, int y) {
        JLabel lblT = new JLabel(titulo);
        lblT.setBounds(60, y, 150, 25);
        lblT.setFont(new Font("Arial", Font.BOLD, 16));
        lblT.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblT);

        JLabel lblV = new JLabel(valor);
        lblV.setBounds(220, y, 350, 25);
        lblV.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lblV);
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 130, 45);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_BOTON_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_BOTON);
            }
        });
        return btn;
    }
}

