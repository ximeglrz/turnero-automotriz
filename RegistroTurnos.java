import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import src.dao.TurnoDAO;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroTurnos extends JFrame {

    // --- COLORES ---
    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    // --- CAMPOS ---
    private JTextField txtNombre, txtApellido, txtTelefono, txtCorreo;
    private JTextField txtPatente;
    private JDateChooser dateFecha;
    private JComboBox<String> cmbHora;
    private JComboBox<String> cmbServicio;

    public RegistroTurnos() {

        setTitle("Registro de Turnos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon img = new ImageIcon(getClass().getResource("bin/Imagenes/fondo-login.png"));
                g.drawImage(img.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondo.setLayout(null);
        setContentPane(fondo);

        // ---------------- MEDIDAS PANTALLA ----------------
        int anchoPantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
        int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;

        int anchoPanel = 800;
        int altoPanel = 510;

        int xPanel = (anchoPantalla - anchoPanel) / 2;
        int yPanel = (altoPantalla - altoPanel) / 2 - 20;

        // ---------------- PANEL CENTRAL ----------------
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBounds(xPanel, yPanel, anchoPanel, altoPanel);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        fondo.add(panel);

        // ---- TITULO FUERA DEL PANEL -----
        JLabel lblTitulo = new JLabel("REGISTRO DE CLIENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);

        // Posición: centrado respecto al panel
        lblTitulo.setBounds(
            xPanel,
            yPanel - 50,
            anchoPanel,
            45
        );

        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        fondo.add(lblTitulo);
        // ACÁ TERMINA EL TITULO NUEVO

        int y = 60;

        txtNombre   = crearCampo(panel, "Nombre", 70, y);
        txtApellido = crearCampo(panel, "Apellido", 420, y);

        y += 80;
        txtTelefono = crearCampo(panel, "Teléfono", 70, y);
        txtCorreo   = crearCampo(panel, "Correo Electrónico", 420, y);

        y += 80;
        txtPatente  = crearCampo(panel, "Patente del Vehículo", 70, y);
        txtPatente.setSize(630, 40);

        y += 80;
        // ---------- FECHA ----------
        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setBounds(70, y, 300, 25);
        lblFecha.setFont(new Font("Arial", Font.BOLD, 16));
        lblFecha.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblFecha);

        dateFecha = new JDateChooser();
        dateFecha.setBounds(70, y + 30, 280, 40);
        dateFecha.setDateFormatString("yyyy-MM-dd");
        dateFecha.setDate(null);

        JTextField txtFecha = (JTextField) dateFecha.getDateEditor().getUiComponent();
        txtFecha.setText("Seleccione fecha");
        txtFecha.setForeground(Color.BLACK);
        txtFecha.setEditable(false); // 🔒 NO permite escribir

        panel.add(dateFecha);

        dateFecha.getDateEditor().addPropertyChangeListener("date", evt -> {
            if (dateFecha.getDate() != null) {
                cmbHora.setEnabled(true);
                cargarHorariosDisponibles();
            }
        });                

        ((JTextField) dateFecha.getDateEditor().getUiComponent())
        .addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField txt = (JTextField) e.getSource();
                if (txt.getText().equals("Seleccione fecha")) {
                    txt.setText("");
                    txt.setForeground(Color.BLACK);
                }
            }
        });

        // ---------- HORA ----------
        JLabel lblHora = new JLabel("Hora");
        lblHora.setBounds(420, y, 300, 25);
        lblHora.setFont(new Font("Arial", Font.BOLD, 16));
        lblHora.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblHora);

        cmbHora = new JComboBox<>();
        cmbHora.setBounds(420, y + 30, 280, 40);
        cmbHora.setEnabled(false);

        // Horarios (ajustables)
        cmbHora.addItem("Seleccione horario");

        // Mañana: 09:00 a 13:00
        for (int h = 9; h < 13; h++) {
            cmbHora.addItem(String.format("%02d:00", h));
            cmbHora.addItem(String.format("%02d:30", h));
        }

        // Tarde: 18:00 a 21:00
        for (int h = 18; h < 21; h++) {
            cmbHora.addItem(String.format("%02d:00", h));
            cmbHora.addItem(String.format("%02d:30", h));
        }

        panel.add(cmbHora);

        y += 80;
        JLabel lblServicio = new JLabel("Tipo de Servicio");
        lblServicio.setBounds(70, y, 300, 25);
        lblServicio.setFont(new Font("Arial", Font.BOLD, 16));
        lblServicio.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblServicio);

        cmbServicio = new JComboBox<>(new String[]{
                "Seleccione una opción",
                "Polarizado",
                "Instalación de Audio",
                "Ambos"
        });
        cmbServicio.setBounds(70, y + 30, 630, 40);
        panel.add(cmbServicio);

        // ---------------- BOTONES ----------------
        int anchoBoton = 130;
        int espacio = 20;
        int totalBotones = (anchoBoton * 4) + (espacio * 3);

        // Posición X inicial para centrar
        int xInicioBotones = xPanel + (anchoPanel - totalBotones) / 2;

        // Posición Y (un poco más cerca del panel)
        int yBotones = yPanel + altoPanel + 20;

        JButton btnLimpiar   = crearBotonSecundario("Limpiar",   xInicioBotones, yBotones);
        JButton btnCancelar  = crearBotonSecundario("Cancelar",  xInicioBotones + (anchoBoton + espacio), yBotones);
        JButton btnAgenda    = crearBotonSecundario("Agenda",    xInicioBotones + 2 * (anchoBoton + espacio), yBotones);
        JButton btnSiguiente = crearBoton("Siguiente", xInicioBotones + 3 * (anchoBoton + espacio), yBotones);

        fondo.add(btnLimpiar);
        fondo.add(btnCancelar);
        fondo.add(btnAgenda);
        fondo.add(btnSiguiente);

        // ---------------- ACCIONES ----------------
        btnLimpiar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere limpiar todos los campos?",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                limpiarCampos();
            }
        });


        btnCancelar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere cancelar?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                new MenuPrincipal().setVisible(true);
                dispose();
            }
        });


        btnAgenda.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere ir a la agenda?",
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                new VerTurnosVentana().setVisible(true);
                dispose();
            }
        });


        btnSiguiente.addActionListener(new ActionListener() {
            TurnoDAO dao = new TurnoDAO();
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtNombre.getText().isEmpty() ||
                    txtApellido.getText().isEmpty() ||
                    txtTelefono.getText().isEmpty() ||
                    txtCorreo.getText().isEmpty() ||
                    txtPatente.getText().isEmpty() ||
                    dateFecha.getDate() == null ||
                    cmbHora.getSelectedIndex() == 0 ||
                    cmbServicio.getSelectedIndex() == 0) {

                    JOptionPane.showMessageDialog(
                        RegistroTurnos.this,
                        "Debe completar todos los campos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                if (!fechaNoEsPasada(dateFecha.getDate())) {
                    JOptionPane.showMessageDialog(
                        RegistroTurnos.this,
                        "No se puede seleccionar una fecha pasada",
                        "Fecha inválida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (!esDiaHabil(dateFecha.getDate())) {
                    JOptionPane.showMessageDialog(
                        RegistroTurnos.this,
                        "Solo se permiten turnos de lunes a viernes",
                        "Día no habilitado",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(dateFecha.getDate());
                String hora = cmbHora.getSelectedItem().toString();

                new ResumenTurno(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    txtPatente.getText(),
                    fecha,
                    hora,
                    cmbServicio.getSelectedItem().toString()
                ).setVisible(true);

                dispose();
            }
        });


    }

    public RegistroTurnos(
            String nombre,
            String apellido,
            String telefono,
            String correo,
            String patente,
            String fecha,
            String hora,
            String servicio
    ) {
        // 1️⃣ Construye toda la pantalla
        this();

        // 2️⃣ Carga los datos en los campos
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtTelefono.setText(telefono);
        txtCorreo.setText(correo);
        txtPatente.setText(patente);

        // 3️⃣ Fecha (String → Date)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(fecha);
            dateFecha.setDate(d);
            cmbHora.setEnabled(true);
        } catch (Exception e) {
            // no rompe la pantalla si algo falla
        }

        // 4️⃣ Hora
        cmbHora.setSelectedItem(hora);

        // 5️⃣ Servicio
        cmbServicio.setSelectedItem(servicio);
    }

    // ================= MÉTODOS =================

    private JTextField crearCampo(JPanel panel, String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(x, y, 300, 25);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(x, y + 30, 280, 40);
        txt.setFont(new Font("Arial", Font.PLAIN, 15));
        txt.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        panel.add(txt);

        return txt;
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 130, 50);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private JButton crearBotonSecundario(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setBounds(x, y, 130, 50);
        btn.setFont(new Font("Arial", Font.BOLD, 16));

        btn.setForeground(COLOR_AZUL_OSCURO_FONDO);
        btn.setBackground(COLOR_FONDO_TARJETA);

        btn.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 🔥 ESTA LÍNEA ES LA CLAVE
        btn.setUI(new BasicButtonUI());

        return btn;
    }

    private boolean fechaNoEsPasada(Date fechaSeleccionada) {
    Date hoy = new Date();

        // Elimina horas para comparar solo fechas
        hoy.setHours(0);
        hoy.setMinutes(0);
        hoy.setSeconds(0);

        return !fechaSeleccionada.before(hoy);
    }

    private boolean esDiaHabil(Date fecha) {
    @SuppressWarnings("deprecation")
    int diaSemana = fecha.getDay(); 
    // 0 = domingo, 6 = sábado
    return diaSemana != 0 && diaSemana != 6;
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPatente.setText("");
        dateFecha.setDate(null);
        JTextField txtFecha = (JTextField) dateFecha.getDateEditor().getUiComponent();
        txtFecha.setText("Seleccione fecha");
        txtFecha.setForeground(Color.GRAY);
        cmbHora.setSelectedIndex(0);
        cmbServicio.setSelectedIndex(0);
    }

    private void mostrarResumen() {
        JOptionPane.showMessageDialog(
                this,
                "Aquí se abrirá la ventana de Resumen del Turno.",
                "Resumen del Turno",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void cargarHorariosDisponibles() {

        cmbHora.removeAllItems();
        cmbHora.addItem("Seleccione horario");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dateFecha.getDate());

        TurnoDAO dao = new TurnoDAO();

        // Mañana: 09:00 a 13:00
        for (int h = 9; h < 13; h++) {
            verificarYAgregarHora(dao, fecha, String.format("%02d:00", h));
            verificarYAgregarHora(dao, fecha, String.format("%02d:30", h));
        }

        // Tarde: 18:00 a 21:00
        for (int h = 18; h < 21; h++) {
            verificarYAgregarHora(dao, fecha, String.format("%02d:00", h));
            verificarYAgregarHora(dao, fecha, String.format("%02d:30", h));
        }

        if (cmbHora.getItemCount() == 1) { // solo "Seleccione horario"
            JOptionPane.showMessageDialog(
                this,
                "No hay horarios disponibles para la fecha seleccionada",
                "Sin disponibilidad",
                JOptionPane.INFORMATION_MESSAGE
            );
            cmbHora.setEnabled(false);
            }

    }

    private void verificarYAgregarHora(TurnoDAO dao, String fecha, String hora) {
        if (dao.horarioDisponible(fecha, hora)) {
            cmbHora.addItem(hora);
        }
    }
  

}


