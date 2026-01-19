import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class VerTurnosVentana extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JTextField txtFechaFiltro;
    private TableRowSorter<DefaultTableModel> sorter;

    private final Color AZUL = new Color(18, 44, 80);
    private final Color AZUL_CLARO = new Color(200, 215, 240);
    private final Color MARRON_CLARO = new Color(232, 221, 206);
    private final Color GRIS_ESTADO = new Color(160, 160, 160);

    public VerTurnosVentana() {

        setTitle("Ver Turnos");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PanelConFondo fondo = new PanelConFondo();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        /* ================= PANEL SUPERIOR ================= */
        JPanel panelSuperior = new JPanel(null);
        panelSuperior.setPreferredSize(new Dimension(1100, 210));
        panelSuperior.setOpaque(false);

        /* ===== RECUADRO MARRÓN ===== */
        JPanel recuadro = new JPanel(null);
        recuadro.setBounds(300, 30, 760, 150);
        recuadro.setBackground(MARRON_CLARO);
        recuadro.setBorder(BorderFactory.createLineBorder(
                new Color(200, 190, 175), 1));
        panelSuperior.add(recuadro);

        /* ===== BUSCADOR ===== */
        JLabel lblBuscador = new JLabel("Buscador:");
        lblBuscador.setBounds(40, 60, 80, 25);

        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setBounds(120, 60, 30, 25);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(150, 55, 220, 35);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(390, 60, 50, 25);

        txtFechaFiltro = new JTextField();
        txtFechaFiltro.setBounds(440, 55, 120, 35);
        txtFechaFiltro.setToolTipText("dd/MM/yyyy");

        recuadro.add(lblBuscador);
        recuadro.add(lblLupa);
        recuadro.add(txtBuscar);
        recuadro.add(lblFecha);
        recuadro.add(txtFechaFiltro);

        /* ===== ESTADOS (REFERENCIA COLOREADA) ===== */
        recuadro.add(crearIndicadorEstado("COMPLETADO", new Color(60, 160, 90), 600, 25));
        recuadro.add(crearIndicadorEstado("EN PROCESO", new Color(220, 170, 60), 600, 65));
        recuadro.add(crearIndicadorEstado("CANCELADO", new Color(190, 70, 70), 600, 105));

        fondo.add(panelSuperior, BorderLayout.NORTH);

        /* ================= TABLA ================= */
        modelo = new DefaultTableModel(
                new Object[]{"Nombre", "Apellido", "Fecha", "Hora", "Estado"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cargarDatosPrueba();

        tabla = new JTable(modelo);
        tabla.setRowHeight(42);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setSelectionBackground(AZUL_CLARO);

        JTableHeader header = tabla.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.setBackground(AZUL);
        header.setForeground(Color.WHITE);

        tabla.getColumnModel().getColumn(4)
                .setCellRenderer(new EstadoTablaRenderer());

        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUI(
                new ScrollPersonalizado(AZUL, MARRON_CLARO));

        fondo.add(scroll, BorderLayout.CENTER);

        /* ================= PANEL INFERIOR ================= */
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        panelInferior.setOpaque(false);

        panelInferior.add(crearBoton("Volver"));
        panelInferior.add(crearBoton("Continuar"));

        fondo.add(panelInferior, BorderLayout.SOUTH);

        /* ===== FILTROS ===== */
        KeyAdapter filtro = new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                aplicarFiltro();
            }
        };
        txtBuscar.addKeyListener(filtro);
        txtFechaFiltro.addKeyListener(filtro);
    }

    /* ================= INDICADOR DE ESTADO ================= */
    private JPanel crearIndicadorEstado(String texto, Color color, int x, int y) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, 150, 25);
        panel.setOpaque(false);

        JPanel circulo = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                g.fillOval(0, 0, 14, 14);
            }
        };
        circulo.setBounds(0, 5, 14, 14);
        circulo.setOpaque(false);

        JLabel lbl = new JLabel(texto);
        lbl.setBounds(20, 0, 120, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        panel.add(circulo);
        panel.add(lbl);

        return panel;
    }

    /* ================= BOTÓN ================= */
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 42));
        btn.setBackground(AZUL);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(25, 65, 120));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(AZUL);
            }
        });
        return btn;
    }

    /* ================= DATOS PRUEBA ================= */
    private void cargarDatosPrueba() {
        for (int i = 1; i <= 60; i++) {
            modelo.addRow(new Object[]{
                    "Cliente " + i,
                    "Apellido " + i,
                    "15/09/2025",
                    "10:" + String.format("%02d", i % 60),
                    "PENDIENTE"
            });
        }
    }

    private void aplicarFiltro() {
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                String texto = txtBuscar.getText().toLowerCase();
                String fecha = txtFechaFiltro.getText();

                return (e.getStringValue(0).toLowerCase().contains(texto) ||
                        e.getStringValue(1).toLowerCase().contains(texto)) &&
                        (fecha.isEmpty() || e.getStringValue(2).contains(fecha));
            }
        });
    }

    /* ================= RENDER ESTADO GRIS (TABLA) ================= */
    class EstadoTablaRenderer extends DefaultTableCellRenderer {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(GRIS_ESTADO);
            g.fillOval(getWidth() / 2 - 7, getHeight() / 2 - 7, 14, 14);
        }

        public void setValue(Object value) {
            setText("");
        }
    }

    /* ================= SCROLL PERSONALIZADO ================= */
    class ScrollPersonalizado extends javax.swing.plaf.basic.BasicScrollBarUI {
        Color thumb, track;

        ScrollPersonalizado(Color thumb, Color track) {
            this.thumb = thumb;
            this.track = track;
        }

        protected void configureScrollBarColors() {
            thumbColor = thumb;
            trackColor = track;
        }
    }

    /* ================= FONDO ================= */
    class PanelConFondo extends JPanel {
        Image fondo = new ImageIcon(
                "C:/Users/yenif/OneDrive/Documentos/2025/programar/autostore/turnero-automotriz/bin/Imagenes/fondo de los turnos lista.jpg"
        ).getImage();

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VerTurnosVentana().setVisible(true));
    }
}
