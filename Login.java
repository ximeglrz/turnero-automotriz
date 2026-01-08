import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import src.conexion.conexion;

public class Login extends JFrame {

    // --- COLORES ---
    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login() {

        // -------- CONFIG GENERAL --------
        setTitle("Inicio de Sesión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // === FONDO COMPLETO ===
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image img = new ImageIcon(
                    System.getProperty("user.dir") + "/src/Imagenes/fondo-login.png"
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
        int yPanel = (altoPantalla - altoPanel) / 2;

        JPanel panel = new JPanel(null);
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBounds(xPanel, yPanel, anchoPanel, altoPanel);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        fondoPanel.add(panel);

        // ----------------------------------------------------------------------------------------
        // LOGO CENTRADO ARRIBA (SIN DEFORMAR) — CARGA DIRECTA DESDE ARCHIVO
        // ----------------------------------------------------------------------------------------

        String rutaLogo = System.getProperty("user.dir") + "/src/Imagenes/logo_turno.png";

        ImageIcon originalIcon = new ImageIcon(rutaLogo);

        if (originalIcon.getIconWidth() == -1) {
            System.out.println("ERROR: No se pudo cargar el logo desde " + rutaLogo);
        }

        Image originalImage = originalIcon.getImage();

        // Altura máxima deseada
        int maxHeight = 100;

        // Mantener proporción
        int newWidth = (originalIcon.getIconWidth() * maxHeight) / originalIcon.getIconHeight();

        Image scaledImage = originalImage.getScaledInstance(newWidth, maxHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel lblLogo = new JLabel(scaledIcon);
        lblLogo.setBounds((anchoPanel - newWidth) / 2, 10, newWidth, maxHeight);

        panel.add(lblLogo);


        // ----------------------------------------------------------------------------------------
        // TÍTULO CENTRADO DEBAJO DEL LOGO
        // ----------------------------------------------------------------------------------------
        JLabel lblTitulo = new JLabel("Inicia sesión para continuar");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblTitulo.setBounds(0, 110, anchoPanel, 40);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo);

        // ======= EMAIL =======
        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18));
        lblEmail.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblEmail.setBounds(40, 160, 300, 30);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(40, 195, 420, 45);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 16));
        txtEmail.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        panel.add(txtEmail);

        // ======= CONTRASEÑA =======
        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Arial", Font.BOLD, 18));
        lblPass.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblPass.setBounds(40, 250, 300, 30);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 285, 420, 45);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        panel.add(txtPassword);

        // ======= OLVIDÉ CONTRASEÑA =======
        JLabel lblOlvido = new JLabel("Olvidé mi contraseña");
        lblOlvido.setFont(new Font("Arial", Font.BOLD, 14));
        lblOlvido.setForeground(new Color(0x0A73B8));
        lblOlvido.setBounds(300, 335, 200, 20);
        lblOlvido.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(lblOlvido);

        // Al hacer clic muestra un aviso simple
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
        btnVolver.setBounds(80, 380, 150, 55);
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
        btnIngresar.setBounds(260, 380, 150, 55);
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
        // TEXTO INFERIOR CENTRADO EN CURSIVA
        // ----------------------------------------------------------------------------------------

        JLabel lblSistema = new JLabel("Sistema exclusivo para personal autorizado");
        lblSistema.setFont(new Font("Times New Roman", Font.ITALIC, 18));
        lblSistema.setForeground(Color.BLACK);

        int anchoTexto = 500;
        int altoTexto = 40;

        int xTexto = (anchoPantalla - anchoTexto) / 2;
        int yTexto = altoPantalla - 80;

        lblSistema.setBounds(xTexto, yTexto, anchoTexto, altoTexto);
        lblSistema.setHorizontalAlignment(SwingConstants.CENTER);

        fondoPanel.add(lblSistema);
    }

    // ==================== AUTENTICAR USUARIO ====================
    private void autenticarUsuario() {

        String email = txtEmail.getText().trim();
        String passIngresada = String.valueOf(txtPassword.getPassword()).trim();

        if (email.isEmpty() || passIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos");
            return;
        }

        Connection con = conexion.conectar();

        // 🔴 VALIDACIÓN CLAVE (evita errores)
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

                JOptionPane.showMessageDialog(this, "Inicio exitoso");
                dispose();

                if (rol.equalsIgnoreCase("Empleado")) {
                    new RegistroTurnos().setVisible(true);
                } else if (rol.equalsIgnoreCase("Jefe de Taller")) {
                    new VerTurnosVentana().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Rol desconocido: " + rol
                    );
                }

            } else {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos");
            }

            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al autenticar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
