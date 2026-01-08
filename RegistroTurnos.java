import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegistroTurnos extends JFrame {

    // --- COLORES ---
    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    // --- CAMPOS ---
    private JTextField txtNombre, txtApellido, txtTelefono, txtCorreo;
    private JTextField txtPatente, txtFecha, txtHora;
    private JComboBox<String> cmbServicio;

    public RegistroTurnos() {

        setTitle("Registro de Turnos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel fondo = new JPanel(null);
        fondo.setBackground(COLOR_AZUL_OSCURO_FONDO);
        setContentPane(fondo);

        // ---------------- MEDIDAS PANTALLA ----------------
        int anchoPantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
        int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;

        int anchoPanel = 700;
        int altoPanel = 650;

        int xPanel = (anchoPantalla - anchoPanel) / 2;
        int yPanel = (altoPantalla - altoPanel) / 2;

        // ---------------- PANEL CENTRAL ----------------
        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBounds(xPanel, yPanel, anchoPanel, altoPanel);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        fondo.add(panel);

        // ---------------- TÍTULO ----------------
        JLabel lblTitulo = new JLabel("Registro de Turno");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblTitulo.setBounds(0, 25, anchoPanel, 40);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo);

        int y = 100;

        txtNombre   = crearCampo(panel, "Nombre del Cliente", 50, y);
        txtApellido = crearCampo(panel, "Apellido del Cliente", 370, y);

        y += 80;
        txtTelefono = crearCampo(panel, "Teléfono", 50, y);
        txtCorreo   = crearCampo(panel, "Correo Electrónico", 370, y);

        y += 80;
        txtPatente  = crearCampo(panel, "Patente del Vehículo", 50, y);

        y += 80;
        txtFecha = crearCampo(panel, "Fecha del Turno (YYYY-MM-DD)", 50, y);
        txtHora  = crearCampo(panel, "Hora del Turno (HH:MM)", 370, y);

        y += 80;
        JLabel lblServicio = new JLabel("Tipo de Servicio");
        lblServicio.setBounds(50, y, 300, 25);
        lblServicio.setFont(new Font("Arial", Font.BOLD, 16));
        lblServicio.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblServicio);

        cmbServicio = new JComboBox<>(new String[]{
                "Seleccione una opción",
                "Polarizado",
                "Instalación de Audio",
                "Ambos"
        });
        cmbServicio.setBounds(50, y + 30, 300, 40);
        panel.add(cmbServicio);

        // ---------------- BOTONES ----------------
        int yBotones = altoPanel - 90;

        JButton btnLimpiar   = crearBoton("Limpiar",   70,  yBotones);
        JButton btnCancelar  = crearBoton("Cancelar", 230, yBotones);
        JButton btnAgenda    = crearBoton("Agenda",   390, yBotones);
        JButton btnSiguiente = crearBoton("Siguiente", 550, yBotones);

        panel.add(btnLimpiar);
        panel.add(btnCancelar);
        panel.add(btnAgenda);
        panel.add(btnSiguiente);

        // ---------------- ACCIONES ----------------
        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnCancelar.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        btnAgenda.addActionListener(e -> {
            new VerTurnosVentana().setVisible(true);
            dispose();
        });

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtNombre.getText().isEmpty() ||
                    txtApellido.getText().isEmpty() ||
                    txtTelefono.getText().isEmpty() ||
                    txtCorreo.getText().isEmpty() ||
                    txtPatente.getText().isEmpty() ||
                    txtFecha.getText().isEmpty() ||
                    txtHora.getText().isEmpty() ||
                    cmbServicio.getSelectedIndex() == 0) {

                    JOptionPane.showMessageDialog(
                        RegistroTurnos.this,
                        "Debe completar todos los campos",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                new ResumenTurno(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    txtPatente.getText(),
                    txtFecha.getText(),
                    txtHora.getText(),
                    cmbServicio.getSelectedItem().toString()
                ).setVisible(true);

                dispose();
            }
        });


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

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPatente.setText("");
        txtFecha.setText("");
        txtHora.setText("");
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
}


