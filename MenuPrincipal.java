import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {

    private final String FONDO = "fondomenu.jpg";
    private final String ICONO_TURNO = "calendar-plus.png";
    private final String ICONO_LISTA = "images (1) (1).png";
    private final String ICONO_SALIR = "salir 2.png";

    private final Color AZUL_BASE = new Color(15, 45, 90);
    private final Color AZUL_HOVER = new Color(25, 70, 140);

    public MenuPrincipal() {

        setTitle("Sistema de Gestión de Turnos");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        FondoPanel fondo = new FondoPanel();
        fondo.setLayout(new BoxLayout(fondo, BoxLayout.Y_AXIS));
        setContentPane(fondo);

        fondo.add(Box.createRigidArea(new Dimension(0, 220)));

        JLabel titulo = new JLabel("SISTEMA DE GESTIÓN DE TURNOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 34));
        titulo.setForeground(AZUL_BASE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setBorder(new EmptyBorder(70, 0, 100, 30));
        fondo.add(titulo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(new EmptyBorder(20, 0, 40, 0));

        panelBotones.add(crearBoton("AGENDAR NUEVO TURNO", ICONO_TURNO));
        panelBotones.add(crearBoton("VER AGENDA DE TURNOS", ICONO_LISTA));
        panelBotones.add(crearBoton("SALIR", ICONO_SALIR));

        fondo.add(panelBotones);

        JSeparator linea = new JSeparator();
        linea.setForeground(AZUL_BASE);
        linea.setMaximumSize(new Dimension(600, 2));
        linea.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(linea);

        JLabel horario = new JLabel("Horario de Atención: Lun - Vie 09:00 - 13:00 / 18:00 - 21:00");
        horario.setFont(new Font("Arial", Font.ITALIC, 13));
        horario.setForeground(AZUL_BASE);
        horario.setBorder(new EmptyBorder(10, 0, 15, 0));
        horario.setAlignmentX(Component.CENTER_ALIGNMENT);
        fondo.add(horario);
    }

    private ImageIcon cargarImagen(String nombre, int size) {

        Image imagen = null;

        URL url = getClass().getResource("/Imagenes/" + nombre);
        if (url != null) {
            imagen = new ImageIcon(url).getImage();
        } else {
            File archivo = new File("bin/Imagenes/" + nombre);
            if (archivo.exists()) {
                imagen = new ImageIcon(archivo.getAbsolutePath()).getImage();
            }
        }

        if (imagen != null) {
            return new ImageIcon(imagen.getScaledInstance(size, size, Image.SCALE_SMOOTH));
        }

        return null;
    }

    private JButton crearBoton(String texto, String iconoNombre) {

        int tamañoIcono = texto.equals("SALIR") ? 32 : 24;
        ImageIcon icono = cargarImagen(iconoNombre, tamañoIcono);

        JButton boton = new JButton(texto, icono) {

            
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }

            
            protected void paintBorder(Graphics g) {
            }
        };

        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_BASE);
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(280, 60));
        boton.setIconTextGap(12);

        boton.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                boton.setBackground(AZUL_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(AZUL_BASE);
            }
        });

        boton.addActionListener(e -> {

            if (texto.equals("AGENDAR NUEVO TURNO")) {
                new Login("AGENDAR").setVisible(true);
                dispose();
            }

            if (texto.equals("VER AGENDA DE TURNOS")) {
                new Login("VER_AGENDA").setVisible(true);
                dispose();
            }

            if (texto.equals("SALIR")) {
                System.exit(0);
            }
        });

        return boton;
    }

    class FondoPanel extends JPanel {

        private Image imagen;

        public FondoPanel() {

            URL url = getClass().getResource("/Imagenes/" + FONDO);
            if (url != null) {
                imagen = new ImageIcon(url).getImage();
            } else {
                File archivo = new File("bin/Imagenes/" + FONDO);
                if (archivo.exists()) {
                    imagen = new ImageIcon(archivo.getAbsolutePath()).getImage();
                }
            }
        }

        
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MenuPrincipal().setVisible(true);
        });
    }
}
