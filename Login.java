import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import src.conexion.conexion;

public class Login extends JFrame {

    // --- COLORES ---
    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private String destino; // Variable para saber a qué botón del menú viene

    public Login(String destino) {
        this.destino = (destino == null) ? "AGENDAR" : destino;

        // -------- CONFIG GENERAL --------
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 650));

        // === FONDO COMPLETO ===
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image img = new ImageIcon(
                    System.getProperty("user.dir") + "/bin/Imagenes/fondo-login.png"
                ).getImage();

                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        fondoPanel.setLayout(null);
        setContentPane(fondoPanel);

        // ----------------------------------------------------------------------------------------
        // PANEL CENTRAL DONDE VA EL LOGIN (FORMULARIO)
        // ----------------------------------------------------------------------------------------

        int anchoPantalla = Toolkit.getDefaultToolkit().getScreenSize().width;
        int altoPantalla = Toolkit.getDefaultToolkit().getScreenSize().height;

        int anchoPanel = 500;
        int altoPanel = 520;

        int xPanel = (anchoPantalla - anchoPanel) / 2;
        int yPanel = (altoPantalla - altoPanel) / 2 - 60;

        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBounds(xPanel, yPanel, anchoPanel, altoPanel);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        fondoPanel.add(panel);

        // ----------------------------------------------------------------------------------------
        // LOGO CENTRADO ARRIBA 
        // ----------------------------------------------------------------------------------------

        String rutaLogo = System.getProperty("user.dir") + "/bin/Imagenes/logo-login.png";

        ImageIcon originalIcon = new ImageIcon(rutaLogo);

        if (originalIcon.getIconWidth() == -1) {
            System.out.println("ERROR: No se pudo cargar el logo desde " + rutaLogo);
        }

        Image originalImage = originalIcon.getImage();

        int maxHeight = 160;
        int newWidth = (originalIcon.getIconWidth() * maxHeight) / originalIcon.getIconHeight();

        Image scaledImage = originalImage.getScaledInstance(newWidth, maxHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel lblLogo = new JLabel(scaledIcon);
        lblLogo.setBounds((anchoPanel - newWidth) / 2, 20, newWidth, maxHeight);
        panel.add(lblLogo);

        // ----------------------------------------------------------------------------------------
        // TÍTULO CENTRADO DEBAJO DEL LOGO
        // ----------------------------------------------------------------------------------------
        JLabel lblTitulo = new JLabel("Inicia sesión para continuar");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblTitulo.setBounds(0, 190, anchoPanel, 40);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo);

        // ======= EMAIL =======
        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18));
        lblEmail.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblEmail.setBounds(40, 240, 300, 30);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(40, 275, 420, 45);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        panel.add(txtEmail);
        agregarMenuCopiarPegar(txtEmail);

        // ======= CONTRASEÑA =======
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Arial", Font.BOLD, 18));
        lblPass.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblPass.setBounds(40, 325, 300, 30);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 360, 420, 45);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        panel.add(txtPassword);

        // ======= OLVIDÉ CONTRASEÑA =======
        JLabel lblOlvido = new JLabel("Olvidé mi contraseña");
        lblOlvido.setFont(new Font("Arial", Font.BOLD, 14));
        lblOlvido.setForeground(new Color(15, 45, 90));
        lblOlvido.setBounds(300, 405, 200, 20);
        lblOlvido.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(lblOlvido);

        lblOlvido.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Comunicate con: turneroautomotriz@gmail.com",
                        "Recuperación de contraseña",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        // ======= BOTÓN VOLVER =======
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(80, 440, 150, 55);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setFocusPainted(false);
        panel.add(btnVolver);

        btnVolver.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnVolver.setBackground(COLOR_BOTON_HOVER); }
            public void mouseExited(MouseEvent e) { btnVolver.setBackground(COLOR_BOTON); }
        });

        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        // ======= BOTÓN INGRESAR =======
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(260, 440, 150, 55);
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(COLOR_BOTON);
        btnIngresar.setFocusPainted(false);
        panel.add(btnIngresar);

        btnIngresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnIngresar.setBackground(COLOR_BOTON_HOVER); }
            public void mouseExited(MouseEvent e) { btnIngresar.setBackground(COLOR_BOTON); }
        });

        btnIngresar.addActionListener(e -> autenticarUsuario());

        // ----------------------------------------------------------------------------------------
        // TEXTO INFERIOR 
        // ----------------------------------------------------------------------------------------

        JLabel lblSistema = new JLabel("Sistema exclusivo para personal autorizado");
        lblSistema.setFont(new Font("Time New Roman", Font.ITALIC, 18));
        lblSistema.setForeground(new Color(15, 45, 90));

        int anchoTexto = 500;
        int altoTexto = 40;

        int xTexto = (anchoPantalla - anchoTexto) / 2;
        int yTexto = altoPantalla - 140;

        lblSistema.setBounds(xTexto, yTexto, anchoTexto, altoTexto);
        lblSistema.setHorizontalAlignment(SwingConstants.CENTER);

        fondoPanel.add(lblSistema);
    }

    // ================= MENÚ COPIAR / PEGAR / CORTAR =================
    private void agregarMenuCopiarPegar(JTextComponent campo) {

        JPopupMenu menu = new JPopupMenu();

        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");

        cortar.addActionListener(e -> campo.cut());
        copiar.addActionListener(e -> campo.copy());
        pegar.addActionListener(e -> campo.paste());

        menu.add(cortar);
        menu.add(copiar);
        menu.add(pegar);

        campo.setComponentPopupMenu(menu);
    }

    // ==================== AUTENTICAR USUARIO ====================
    private void autenticarUsuario() {

        String email = txtEmail.getText().trim();
        String passIngresada = String.valueOf(txtPassword.getPassword()).trim();

        if (email.isEmpty() || passIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        Connection con = conexion.getConexion();

        if (con == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo conectar con la base de datos.\nVerifique que MySQL esté encendido.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT rol FROM usuarios WHERE correo = ? AND contraseña = ?"
            );

            ps.setString(1, email);
            ps.setString(2, passIngresada);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String rol = rs.getString("rol");

                // --- LÓGICA DE ACCESO SEGÚN ROL Y DESTINO ---

                // 🔹 CASO: VER AGENDA (ENTRAN AMBOS USUARIOS)
                if (destino.equals("VER_AGENDA")) {

                    JOptionPane.showMessageDialog(this, "Acceso a agenda autorizado.");
                    dispose();

                    if (rol.equalsIgnoreCase("Jefe de Taller")) {
                        new VerTurnosVentana("Jefe").setVisible(true);
                    } else {
                        new VerTurnosVentana("Empleado").setVisible(true);
                    }

                // 🔹 CASO: AGENDAR NUEVO TURNO (SOLO EMPLEADO)
                } else if (destino.equals("AGENDAR")) {

                    if (rol.equalsIgnoreCase("Empleado")) {
                        JOptionPane.showMessageDialog(this, "Acceso a registro autorizado.");
                        dispose();
                        new RegistroTurnos().setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "ACCESO DENEGADO:\nUsuario no autorizado a registrar turnos.",
                            "Restricción",
                            JOptionPane.ERROR_MESSAGE
                        );
                        dispose();
                        new MenuPrincipal().setVisible(true);
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos");
            }
            con.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al autenticar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login("AGENDAR").setVisible(true));
    }
}