import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MenuPrincipal extends JFrame {

    // 🔥 NUEVA RUTA DE FONDO (INCLUYE LOGO)
    private final String RUTA_FONDO =
            "C:\\Users\\yenif\\OneDrive\\Documentos\\2025\\programar\\autostore\\turnero-automotriz\\bin\\Imagenes\\fondomenu.jpg";

    private final String ICONO_TURNO =
            "C:\\Users\\yenif\\OneDrive\\Documentos\\2025\\programar\\autostore\\turnero-automotriz\\bin\\Imagenes\\calendar-plus.png";

    private final String ICONO_LISTA =
            "C:\\Users\\yenif\\OneDrive\\Documentos\\2025\\programar\\autostore\\turnero-automotriz\\bin\\Imagenes\\images (1) (1).png";

    private final String ICONO_SALIR =
            "C:\\Users\\yenif\\OneDrive\\Documentos\\2025\\programar\\autostore\\turnero-automotriz\\bin\\Imagenes\\salir 2.png";

    public MenuPrincipal() {

        setTitle("Sistema de Gestión de Turnos");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        setContentPane(fondo);

        /* 🔽 ESPACIADOR SUPERIOR (BAJA TODO EL CONTENIDO) 🔽 */
        fondo.add(Box.createRigidArea(new Dimension(0, 220)));

        /* ───── TÍTULO ───── */
        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN DE TURNOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 34));
        titulo.setForeground(new Color(10, 40, 90));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(70, 0, 100, 30));
        fondo.add(titulo);

        /* ───── BOTONES ───── */
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 0, 40, 0));

        panelBotones.add(crearBoton("AGENDAR NUEVO TURNO", ICONO_TURNO));
        panelBotones.add(crearBoton("VER AGENDA DE TURNOS", ICONO_LISTA));
        panelBotones.add(crearBoton("SALIR", ICONO_SALIR));

        fondo.add(panelBotones);

        /* ───── LÍNEA ───── */
        JSeparator linea = new JSeparator();
        linea.setForeground(Color.WHITE);
        linea.setMaximumSize(new Dimension(600, 1));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(linea);

        /* ───── HORARIO ───── */
        JLabel horario = new JLabel(
                "Horario de Atención: Lun - Vie 09:00 am - 01:00 p.m / 18:00 pm - 21:00 pm"
        );
        horario.setFont(new Font("Arial", Font.ITALIC, 13));
        horario.setForeground(new Color(15, 45, 90));
        horario.setBorder(new EmptyBorder(10, 0, 15, 0));
        horario.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(horario);
    }

    /* ───── CREAR BOTÓN ───── */
    private JButton crearBoton(String texto, String rutaIcono) {

        int sizeIcono = texto.equals("SALIR") ? 32 : 24;

        ImageIcon icono = new ImageIcon(
                new ImageIcon(rutaIcono)
                        .getImage()
                        .getScaledInstance(sizeIcono, sizeIcono, Image.SCALE_SMOOTH)
        );

        JButton boton = new JButton(texto, icono) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
            }
            protected void paintBorder(Graphics g) {}
        };

        Color normal = new Color(15, 45, 90);
        Color hover = new Color(25, 70, 140);

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(normal);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(280, 60));
        boton.setIconTextGap(12);

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { boton.setBackground(hover); }
            public void mouseExited(MouseEvent e) { boton.setBackground(normal); }
        });

        boton.addActionListener(e -> {
            if (!texto.equals("SALIR")) {
                new Login().setVisible(true);
                dispose();
            } else {
                System.exit(0);
            }
        });

        return boton;
    }

    /* ───── PANEL DE FONDO ───── */
    class FondoPanel extends JPanel {
        private Image imagen = new ImageIcon(RUTA_FONDO).getImage();

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
