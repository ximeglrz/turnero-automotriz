import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DetalleTurno extends JFrame {

    /* ================= COLORES ================= */
    private final Color AZUL_OSCURO   = new Color(18, 44, 80);
    private final Color MARRON_FONDO  = new Color(225, 214, 198);
    private final Color MARRON_CUADRO = new Color(240, 232, 220);
    private final Color BORDE_SUAVE   = new Color(170, 160, 145);

    public DetalleTurno(JFrame parent) {
        setTitle("DETALLE DE TURNO");
        setSize(800, 700); // Aumentado ligeramente para dar aire
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        /* ================= FONDO ================= */
        JPanel fondo = new JPanel(new BorderLayout());
        fondo.setBackground(MARRON_FONDO);
        setContentPane(fondo);

        /* ================= CONTENEDOR ================= */
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setOpaque(false);
        contenedor.setBorder(new EmptyBorder(60, 80, 40, 80));
        fondo.add(contenedor);

        /* ================= TÍTULO CENTRADO ================= */
        JLabel titulo = new JLabel("DETALLE DE TURNO", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(AZUL_OSCURO);

        JSeparator separador = new JSeparator();
        separador.setForeground(AZUL_OSCURO);
        separador.setPreferredSize(new Dimension(200, 2));

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

        /* ================= SOMBRA FAKE ================= */
        JPanel sombra = new JPanel(new BorderLayout());
        sombra.setOpaque(true);
        sombra.setBackground(new Color(200, 190, 175));
        sombra.setBorder(new EmptyBorder(8, 8, 8, 8));

        /* ================= CUADRO DE DATOS ================= */
        // Se aumentó el espaciado vertical (hgap: 25, vgap: 22) para que no se vea amontonado
        JPanel cuadro = new JPanel(new GridLayout(0, 2, 25, 22));
        cuadro.setBackground(MARRON_CUADRO);
        cuadro.setBorder(new CompoundBorder(
                new LineBorder(BORDE_SUAVE, 1, true),
                new EmptyBorder(30, 50, 30, 50)
        ));

        cuadro.add(label("CLIENTE:"));
        cuadro.add(valor("Juan Pérez"));

        cuadro.add(label("TELÉFONO:"));
        cuadro.add(valor("11 2345-6789"));

        cuadro.add(label("PATENTE:"));
        cuadro.add(valor("AB 123 CD"));

        cuadro.add(label("MODELO:"));
        cuadro.add(valor("Toyota Corolla"));

        cuadro.add(label("SERVICIO:"));
        cuadro.add(valor("Cambio de aceite"));

        cuadro.add(label("FECHA:"));
        cuadro.add(valor("15/09/2025"));

        cuadro.add(label("HORA:"));
        cuadro.add(valor("10:30"));

        /* --- CAMBIO SOLICITADO: ESTADO DEL VEHÍCULO --- */
        cuadro.add(label("ESTADO DEL VEHÍCULO:"));

        JComboBox<String> comboEstado = new JComboBox<>(
                new String[]{"EN PROCESO", "COMPLETADO", "CANCELADO"}
        );
        
        // Fuente más grande y negrita para destacar
        comboEstado.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setForeground(AZUL_OSCURO);
        
        // Forzamos una altura mayor para el componente
        comboEstado.setPreferredSize(new Dimension(200, 45));
        
        // Centramos el texto dentro del combo para que se vea mejor visualmente
        ((JLabel)comboEstado.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        
        cuadro.add(comboEstado);
        /* ----------------------------------------------- */

        sombra.add(cuadro);
        contenedor.add(sombra, BorderLayout.CENTER);

        /* ================= BOTONES ================= */
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 35, 25));
        panelBotones.setOpaque(false);

        JButton btnGuardar = crearBoton("GUARDAR");
        JButton btnCerrar  = crearBoton("CERRAR");
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCerrar);

        contenedor.add(panelBotones, BorderLayout.SOUTH);
    }

    /* ================= MÉTODOS AUXILIARES ================= */
    private JLabel label(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Subido a 14 para legibilidad
        lbl.setForeground(AZUL_OSCURO);
        return lbl;
    }

    private JLabel valor(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Subido a 15
        lbl.setForeground(Color.BLACK);
        return lbl;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(180, 50));
        btn.setBackground(AZUL_OSCURO);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new DetalleTurno(null).setVisible(true);
        });
    }
}