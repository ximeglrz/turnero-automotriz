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

    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private String destino;
    private Image imagenFondo;

    public Login(String destino) {
        this.destino = (destino == null) ? "AGENDAR" : destino;

        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 650));

        imagenFondo = new ImageIcon(
            System.getProperty("user.dir") + "/bin/Imagenes/fondo-login.png"
        ).getImage();

        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imagenFondo, 0, 0, 
                    getWidth(), getHeight(), this);
            }
        };
        fondoPanel.setLayout(new BorderLayout());
        setContentPane(fondoPanel);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3));
        panel.setPreferredSize(new Dimension(500, 800));

        JPanel contenedorCentro = new JPanel(new GridBagLayout());
        contenedorCentro.setOpaque(false);

        GridBagConstraints gbcPanel = new GridBagConstraints();
        gbcPanel.insets = new Insets(8, 40, 8, 40);
        gbcPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel.weightx = 1;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        contenedorCentro.add(panel, gbc);

        fondoPanel.add(contenedorCentro, BorderLayout.CENTER);

        
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
        gbcPanel.gridx = 0;
        gbcPanel.gridy = 0;
        gbcPanel.anchor = GridBagConstraints.CENTER;
        panel.add(lblLogo, gbcPanel);

        JLabel lblTitulo = new JLabel("Inicia sesión para continuar");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        gbcPanel.gridy = 1;
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, gbcPanel);

        JLabel lblEmail = new JLabel("Correo Electrónico");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 18));
        lblEmail.setForeground(COLOR_AZUL_OSCURO_FONDO);
        gbcPanel.insets = new Insets(10, 40, 5, 40);
        gbcPanel.gridy = 2;
        panel.add(lblEmail, gbcPanel);

        txtEmail = new JTextField();
        txtEmail.addActionListener(e -> txtPassword.requestFocusInWindow());
        gbcPanel.insets = new Insets(0, 40, 15, 40);
        gbcPanel.gridy = 3;
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 18));
        txtEmail.setMargin(new Insets(40, 25, 40, 25));
        txtEmail.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        txtEmail.setPreferredSize(new Dimension(400, 90));
        gbcPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel.weighty = 0;
        panel.add(txtEmail, gbcPanel);
        agregarMenuCopiarPegar(txtEmail);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(new Font("Arial", Font.BOLD, 18));
        lblPass.setForeground(COLOR_AZUL_OSCURO_FONDO);
        gbcPanel.insets = new Insets(5, 40, 5, 40);
        gbcPanel.gridy = 4;
        panel.add(lblPass, gbcPanel);

        gbcPanel.insets = new Insets(0, 40, 15, 40);
        gbcPanel.gridy = 5;
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 18));
        txtPassword.setMargin(new Insets(40, 25, 40, 25));
        txtPassword.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        txtPassword.setPreferredSize(new Dimension(400, 90));
        gbcPanel.fill = GridBagConstraints.HORIZONTAL;
        gbcPanel.weighty = 0;
        panel.add(txtPassword, gbcPanel);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 20));
        panelBotones.setOpaque(false);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(170, 50));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(COLOR_BOTON);
        btnVolver.setFocusPainted(false);
        btnVolver.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnVolver.setBackground(COLOR_BOTON_HOVER); }
            public void mouseExited(MouseEvent e) { btnVolver.setBackground(COLOR_BOTON); }
        });

        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setPreferredSize(new Dimension(170, 50));
        btnIngresar.setFont(new Font("Arial", Font.BOLD, 18));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setBackground(COLOR_BOTON);
        btnIngresar.setFocusPainted(false);

        panelBotones.add(btnVolver);
        panelBotones.add(btnIngresar);

        gbcPanel.gridy = 6;
        gbcPanel.insets = new Insets(10, 40, 10, 40);
        panel.add(panelBotones, gbcPanel);

        btnIngresar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnIngresar.setBackground(COLOR_BOTON_HOVER); }
            public void mouseExited(MouseEvent e) { btnIngresar.setBackground(COLOR_BOTON); }
        });

        btnIngresar.addActionListener(e -> autenticarUsuario());

        getRootPane().setDefaultButton(btnIngresar);

        JLabel lblSistema = new JLabel("Sistema exclusivo para personal autorizado");
        lblSistema.setFont(new Font("Time New Roman", Font.ITALIC, 18));
        lblSistema.setForeground(new Color(15, 45, 90));
        lblSistema.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelInferior = new JPanel();
        panelInferior.setOpaque(false);
        panelInferior.setLayout(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        panelInferior.add(lblSistema, BorderLayout.CENTER);

        fondoPanel.add(panelInferior, BorderLayout.SOUTH);
    }

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

                if (destino.equals("VER_AGENDA")) {

                    JOptionPane.showMessageDialog(this, "Acceso a Agenda autorizado");
                    dispose();

                    if (rol.equalsIgnoreCase("Jefe de Taller")) {
                        new VerTurnoVentana("Jefe").setVisible(true);
                    } else {
                        new VerTurnoVentana("Empleado").setVisible(true);
                    }

                } else if (destino.equals("AGENDAR")) {

                    if (rol.equalsIgnoreCase("Empleado")) {
                        JOptionPane.showMessageDialog(this, "Acceso a Registro autorizado");
                        dispose();
                        new RegistroTurno().setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "ACCESO DENEGADO:\nUsuario no autorizado a registrar turnos",
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

}