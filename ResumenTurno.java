import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import src.conexion.conexion;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.io.IOException;

public class ResumenTurno extends JFrame {

    // COLORES
    private static final Color AZUL_OSCURO   = new Color(18, 44, 80);
    private static final Color MARRON_FONDO  = new Color(225, 214, 198);
    private static final Color MARRON_CUADRO = new Color(240, 232, 220);
    private static final Color BORDE_SUAVE   = new Color(170, 160, 145);

    // BOTONES
    private JButton btnGuardar;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 650));

        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(MARRON_FONDO);
        setContentPane(fondo);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(60, 40, 40, 40));
        fondo.add(contenedor, BorderLayout.CENTER);

        JPanel contenedorCentral = new JPanel(new GridBagLayout());
        contenedorCentral.setOpaque(false);
        contenedorCentral.setPreferredSize(new Dimension(800, 520));

        contenedor.add(contenedorCentral, BorderLayout.CENTER);

        JPanel panel = new JPanel(null);
        panel.setBackground(MARRON_CUADRO);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDE_SUAVE, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JPanel sombra = new JPanel(new BorderLayout());
        sombra.setBackground(new Color(200, 190, 175));
        sombra.setBorder(new EmptyBorder(8, 8, 8, 8));

        sombra.add(panel);

        int anchoTotal = 520 + 40 + 240; // tarjeta + espacio + acciones
        int inicioX = (contenedorCentral.getPreferredSize().width - anchoTotal) / 2;

        // ===== PANEL INFERIOR (Volver / Guardar) =====
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelInferior.setOpaque(false);

        JPanel panelAcciones = new JPanel();
        panelAcciones.setLayout(new BoxLayout(panelAcciones, BoxLayout.Y_AXIS));
        panelAcciones.setOpaque(false);
        panelAcciones.setBounds(inicioX + 560, 0, 240, 420);

        JPanel bloqueCentral = new JPanel();
        bloqueCentral.setOpaque(false);
        bloqueCentral.setLayout(new BorderLayout(40, 20));

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));

        panelIzquierdo.add(sombra);
        panelIzquierdo.add(Box.createVerticalStrut(15));
        panelIzquierdo.add(panelInferior);

        panelAcciones.setAlignmentY(Component.TOP_ALIGNMENT);

        bloqueCentral.add(panelIzquierdo, BorderLayout.WEST);
        bloqueCentral.add(panelAcciones, BorderLayout.EAST);

        contenedorCentral.add(bloqueCentral, new GridBagConstraints());

        JLabel titulo = new JLabel("RESUMEN DEL TURNO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titulo.setForeground(AZUL_OSCURO);

        JSeparator separador = new JSeparator();
        separador.setForeground(AZUL_OSCURO);
        separador.setPreferredSize(new Dimension(220, 2));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));

        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        separador.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(10));
        panelTitulo.add(separador);
        panelTitulo.add(Box.createVerticalStrut(25));

        contenedor.add(panelTitulo, BorderLayout.NORTH);

        int y = 5;
        agregarDato(panel, "Cliente:", nombre + " " + apellido, y); y += 40;
        agregarDato(panel, "Teléfono:", telefono, y); y += 40;
        agregarDato(panel, "Correo:", correo, y); y += 40;
        agregarDato(panel, "Patente:", patente, y); y += 40;
        agregarDato(panel, "Fecha:", fecha, y); y += 40;
        agregarDato(panel, "Hora:", hora, y); y += 40;
        agregarDato(panel, "Servicio:", servicio, y);

        // BOTONES
        JButton btnVolver  = crearBoton("Volver", 0, 0);
        btnGuardar = crearBoton("Guardar", 0, 0);
        JButton btnImprimir = crearBoton("Imprimir", 0, 0);
        JButton btnEnviar = crearBoton("Enviar", 0, 0);
        JButton btnAgenda = crearBoton("Agenda", 0, 0);
        JButton btnCerrar = crearBoton("Cerrar sesión", 0, 0);
        JButton btnSalir  = crearBoton("Salir del sistema", 0, 0);

        panelInferior.add(btnVolver);
        panelInferior.add(btnGuardar);

        // PANEL ACCIONES
        panelAcciones.add(btnAgenda);
        panelAcciones.add(Box.createVerticalStrut(20));
        panelAcciones.add(btnEnviar);
        panelAcciones.add(Box.createVerticalStrut(15));
        panelAcciones.add(btnImprimir);
        panelAcciones.add(Box.createVerticalStrut(15));
        panelAcciones.add(btnCerrar);
        panelAcciones.add(Box.createVerticalStrut(40));
        panelAcciones.add(btnSalir);

      // ACCIONES
        btnVolver.addActionListener(e -> {

            Object[] opciones = {"Mantener datos", "Borrar datos", "Cancelar"};

            int op = JOptionPane.showOptionDialog(
                this,
                "Al volver, ¿qué desea hacer con los datos del cliente?",
                "Confirmar regreso",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
            );

            if (op == 0) {
                // Mantener datos
                new RegistroTurnos(
                    nombre,
                    apellido,
                    telefono,
                    correo,
                    patente,
                    fecha,
                    hora,
                    servicio
                ).setVisible(true);
                dispose();

            } else if (op == 1) {
                // Borrar datos → segunda confirmación
                int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que desea borrar los datos del cliente?",
                    "Confirmar borrado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    new RegistroTurnos().setVisible(true);
                    dispose();
                }
            }
        });

        btnGuardar.addActionListener(e -> guardarTurnoCompleto());
        
        btnImprimir.addActionListener(e -> {
            try {
                File pdf = generarPDF();
                Desktop.getDesktop().open(pdf);

                JOptionPane.showMessageDialog(
                    this,
                    "Comprobante generado correctamente",
                    "PDF",
                    JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Error al generar comprobante: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        btnEnviar.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "Correo enviado (simulado)") 
        );

        btnAgenda.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea ir a la agenda?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
            );

            if (op == JOptionPane.YES_OPTION) {
                new VerTurnosVentana(nombre).setVisible(true);
                dispose();
            }
        });

        btnCerrar.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
            );

            if (op == JOptionPane.YES_OPTION) {
                new Login("null").setVisible(true);
                dispose();
            }
        });

        btnSalir.addActionListener(e -> {
            int op = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea salir del sistema?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
            );

            if (op == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnSalir.setBackground(null);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setOpaque(false);
        btnSalir.setForeground(AZUL_OSCURO);
        btnSalir.setBorder(new LineBorder(AZUL_OSCURO, 2, true));
        btnSalir.setFocusPainted(false);
        btnSalir.setBorderPainted(true);
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

        boolean turnoManiana = !h.isBefore(LocalTime.of(9, 0)) 
                                && h.isBefore(LocalTime.of(13, 0));

        boolean turnoTarde = !h.isBefore(LocalTime.of(18, 0)) 
                              && h.isBefore(LocalTime.of(21, 0));

        return turnoManiana || turnoTarde;
    }


    private boolean existeTurno() throws Exception {

        Connection con = conexion.getConexion();

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

    private Integer obtenerIdClienteExistente() throws Exception {

        Connection con = conexion.getConexion();

        String sql = "SELECT id_cliente FROM clientes WHERE telefono = ? OR correo = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, telefono);
        ps.setString(2, correo);

        ResultSet rs = ps.executeQuery();

        Integer idCliente = null;
        if (rs.next()) {
            idCliente = rs.getInt("id_cliente");
        }

        rs.close();
        ps.close();
        con.close();

        return idCliente;
    }

    /* ================= GUARDADO ================= */

    private int guardarCliente() throws Exception {

        Connection con = conexion.getConexion();

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

        Connection con = conexion.getConexion();

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

        Connection con = conexion.getConexion();

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

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que desea guardar el turno?",
                    "Confirmar guardado",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }

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
                JOptionPane.showMessageDialog(
                    this,
                    "Horario permitido:\n09:00 a 13:00 y 18:00 a 21:00"
                );
                return;
            }            

            if (existeTurno()) {
                JOptionPane.showMessageDialog(this, "Ya existe un turno para esa fecha y hora");
                return;
            }

            Integer idCliente = obtenerIdClienteExistente();

            if (idCliente == null) {
                idCliente = guardarCliente();
            }
            
            int idVehiculo = guardarVehiculo(idCliente);
            int idServicio = obtenerIdServicio();

            Connection con = conexion.getConexion();

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

            JOptionPane.showMessageDialog(
                this,
                "Turno registrado correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
            );

            btnGuardar.setEnabled(false);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar turno: " + e.getMessage());
        }
    }

    private void agregarDato(JPanel panel, String titulo, String valor, int y) {
        JLabel lblT = new JLabel(titulo);
        lblT.setBounds(60, y, 170, 30);
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblT.setForeground(AZUL_OSCURO);
        panel.add(lblT);

        JLabel lblV = new JLabel(valor);
        lblV.setBounds(240, y, 360, 30);
        lblV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblV.setForeground(Color.BLACK);
        panel.add(lblV);
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(200, 55));
        btn.setMaximumSize(new Dimension(200, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(AZUL_OSCURO.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(AZUL_OSCURO);
            }
        });

        return btn;
    }

    private File generarPDF() throws IOException {

        String carpetaUsuario = System.getProperty("user.home") + File.separator + "Documents";
        File carpetaComprobantes = new File(carpetaUsuario, "ComprobantesTurnos");

        if (!carpetaComprobantes.exists()) {
            carpetaComprobantes.mkdirs();
        }

        File pdf = new File(carpetaComprobantes, "comprobante_turno.pdf");

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        content.beginText();
        content.setFont(PDType1Font.HELVETICA_BOLD, 18);
        content.setLeading(22f);
        content.newLineAtOffset(100, 700);

        content.showText("COMPROBANTE DE TURNO");
        content.newLine();
        content.newLine();

        content.setFont(PDType1Font.HELVETICA, 12);
        content.showText("Cliente: " + nombre + " " + apellido);
        content.newLine();
        content.showText("Teléfono: " + telefono);
        content.newLine();
        content.showText("Correo: " + correo);
        content.newLine();
        content.showText("Patente: " + patente);
        content.newLine();
        content.showText("Fecha: " + fecha);
        content.newLine();
        content.showText("Hora: " + hora);
        content.newLine();
        content.showText("Servicio: " + servicio);

        content.endText();
        content.close();

        document.save(pdf);
        document.close();

        return pdf;
    }

}

