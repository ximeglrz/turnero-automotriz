import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

public class MenuPrincipal extends JFrame {

    // ✅ Nombres de archivos para carga portable (Híbrida)
    private final String NOMBRE_FONDO = "fondomenu.jpg";
    private final String NOMBRE_ICONO_TURNO = "calendar-plus.png";
    private final String NOMBRE_ICONO_LISTA = "images (1) (1).png";
    private final String NOMBRE_ICONO_SALIR = "salir 2.png";

    public MenuPrincipal() {
        setTitle("Sistema de Gestión de Turnos");

        // ✅ Configuración de pantalla completa
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 650));

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        setContentPane(fondo);

        /* 🔽 ESPACIADOR SUPERIOR 🔽 */
        fondo.add(Box.createRigidArea(new Dimension(0, 220)));

        /* ───── TÍTULO ───── */
        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN DE TURNOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 34));
        titulo.setForeground(new Color(10, 40, 90));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(70, 0, 100, 30));
        fondo.add(titulo);

        /* ───── PANEL DE BOTONES ───── */
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 0, 40, 0));

        panelBotones.add(crearBoton("AGENDAR NUEVO TURNO", NOMBRE_ICONO_TURNO));
        panelBotones.add(crearBoton("VER AGENDA DE TURNOS", NOMBRE_ICONO_LISTA));
        panelBotones.add(crearBoton("SALIR", NOMBRE_ICONO_SALIR));

        fondo.add(panelBotones);

        /* ───── LÍNEA DIVISORIA ───── */
        JSeparator linea = new JSeparator();
        linea.setForeground(Color.WHITE);
        linea.setMaximumSize(new Dimension(600, 1));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(linea);

        /* ───── HORARIO ───── */
        JLabel horario = new JLabel("Horario de Atención: Lun - Vie 09:00 am - 01:00 p.m / 18:00 pm - 21:00 pm");
        horario.setFont(new Font("Arial", Font.ITALIC, 13));
        horario.setForeground(new Color(15, 45, 90));
        horario.setBorder(new EmptyBorder(10, 0, 15, 0));
        horario.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(horario);
    }

    /* ───── LÓGICA DE CARGA DE IMÁGENES ───── */
    private ImageIcon intentarCargarImagen(String nombre, int size) {
        URL url = getClass().getResource("/Imagenes/" + nombre);
        Image img = null;
        if (url != null) {
            img = new ImageIcon(url).getImage();
        } else {
            File file = new File("bin/Imagenes/" + nombre);
            if (file.exists()) {
                img = new ImageIcon(file.getAbsolutePath()).getImage();
            }
        }
        if (img != null) {
            return new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        }
        return null;
    }

    /* ───── CREAR BOTÓN ───── */
    private JButton crearBoton(String texto, String nombreIcono) {
        int sizeIcono = texto.equals("SALIR") ? 32 : 24;
        ImageIcon icono = intentarCargarImagen(nombreIcono, sizeIcono);

        JButton boton = new JButton(texto, icono) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {}
        };

        Color azulNormal = new Color(15, 45, 90);
        Color azulHover = new Color(25, 70, 140);

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(azulNormal);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(280, 60));
        boton.setIconTextGap(12);

        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { boton.setBackground(azulHover); }
            public void mouseExited(MouseEvent e) { boton.setBackground(azulNormal); }
        });

        // ✅ ÚNICA MODIFICACIÓN: Se envía el destino al constructor del Login
        boton.addActionListener(e -> {
            if (texto.equals("AGENDAR NUEVO TURNO")) {
                new Login("AGENDAR").setVisible(true);
                dispose();
            } else if (texto.equals("VER AGENDA DE TURNOS")) {
                new Login("VER_AGENDA").setVisible(true);
                dispose();
            } else if (texto.equals("SALIR")) {
                System.exit(0);
            }
        });

        return boton;
    }

    /* ───── PANEL DE FONDO ───── */
    class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel() {
            URL url = getClass().getResource("/Imagenes/" + NOMBRE_FONDO);
            if (url != null) {
                imagen = new ImageIcon(url).getImage();
            } else {
                File file = new File("bin/Imagenes/" + NOMBRE_FONDO);
                if (file.exists()) {
                    imagen = new ImageIcon(file.getAbsolutePath()).getImage();
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}