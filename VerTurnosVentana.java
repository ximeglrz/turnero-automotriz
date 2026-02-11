import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URL;

import src.conexion.conexion;

public class VerTurnosVentana extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtBuscar;
    private JTextField txtFechaFiltro;
    private TableRowSorter<DefaultTableModel> sorter;

    private String rolUsuario;

    private final Color AZUL = new Color(18, 44, 80);
    private final Color AZUL_CLARO = new Color(200, 215, 240);
    private final Color CREMITA = new Color(225, 215, 195); 
    private final Color VERDE = new Color(60, 160, 90);
    private final Color AMARILLO = new Color(220, 170, 60);
    private final Color ROJO = new Color(190, 70, 70);
    private final Color GRIS_ESTADO = new Color(180, 180, 180);

    public VerTurnosVentana(String string) {
        this.rolUsuario = string;

        setTitle("Ver Turnos");
        setSize(1100, 720);
        setMinimumSize(new Dimension(1050, 700)); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        PanelConFondo fondo = new PanelConFondo();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel contenedorSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        contenedorSuperior.setOpaque(false);
        contenedorSuperior.setPreferredSize(new Dimension(1100, 210));

        JPanel recuadro = new JPanel(null);
        recuadro.setPreferredSize(new Dimension(760, 150));
        recuadro.setBackground(CREMITA);
        recuadro.setBorder(BorderFactory.createLineBorder(AZUL, 2));

        Font fuenteLabels = new Font("Segoe UI", Font.BOLD, 16);

        JLabel lblBuscador = new JLabel("Buscador:");
        lblBuscador.setBounds(40, 60, 100, 25);
        lblBuscador.setFont(fuenteLabels);
        lblBuscador.setForeground(AZUL);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(135, 55, 235, 35);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AZUL, 1),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setBounds(390, 60, 60, 25);
        lblFecha.setFont(fuenteLabels);
        lblFecha.setForeground(AZUL);

        txtFechaFiltro = new JTextField();
        txtFechaFiltro.setBounds(455, 55, 110, 35);
        txtFechaFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtFechaFiltro.setBorder(BorderFactory.createLineBorder(AZUL, 1));
        txtFechaFiltro.setHorizontalAlignment(JTextField.CENTER);

        agregarMenuCopiarPegar(txtBuscar);
        agregarMenuCopiarPegar(txtFechaFiltro);

        recuadro.add(lblBuscador);
        recuadro.add(txtBuscar);
        recuadro.add(lblFecha);
        recuadro.add(txtFechaFiltro);

        recuadro.add(crearIndicadorEstado("Terminado", VERDE, 580, 25));
        recuadro.add(crearIndicadorEstado("En proceso", AMARILLO, 580, 65));
        recuadro.add(crearIndicadorEstado("Cancelado", ROJO, 580, 105));

        contenedorSuperior.add(recuadro);
        fondo.add(contenedorSuperior, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Apellido", "Fecha", "Hora", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        cargarTurnosDesdeBD();

        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setBackground(Color.WHITE); 
        tabla.setGridColor(new Color(210, 200, 185)); 
        tabla.setFillsViewportHeight(true);

        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.getTableHeader().setResizingAllowed(false);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER); 

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if (i != 5) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }
        }

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JTableHeader header = tabla.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setFont(new Font("Segoe UI", Font.BOLD, 17));
        header.setBackground(AZUL);
        header.setForeground(Color.WHITE);

        tabla.getColumnModel().getColumn(5).setCellRenderer(new EstadoTablaRenderer());

        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(Color.WHITE); 
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        fondo.add(scroll, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new GridBagLayout());
        panelInferior.setOpaque(false);
        panelInferior.setPreferredSize(new Dimension(1100, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 25, 10, 25); 

        JButton btnVolver = crearBotonBorde("Volver");
        JButton btnContinuar = crearBoton("Continuar");

        btnVolver.addActionListener(e -> {
            if ("Empleado".equalsIgnoreCase(string)) {
                new RegistroTurnos().setVisible(true);
            } else if ("Jefe".equalsIgnoreCase(string)) {
                new MenuPrincipal().setVisible(true);
            }
            dispose();
        });

        btnContinuar.addActionListener(e -> abrirDetalleTurno());

        panelInferior.add(btnVolver, gbc);
        panelInferior.add(btnContinuar, gbc);
        fondo.add(panelInferior, BorderLayout.SOUTH);

        KeyAdapter filtro = new KeyAdapter() {
            public void keyReleased(KeyEvent e) { aplicarFiltro(); }
        };
        txtBuscar.addKeyListener(filtro);
        txtFechaFiltro.addKeyListener(filtro);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) cargarTurnosDesdeBD();
        super.setVisible(b);
    }

    private void agregarMenuCopiarPegar(JTextField campo) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar = new JMenuItem("Pegar");
        cortar.addActionListener(e -> campo.cut());
        copiar.addActionListener(e -> campo.copy());
        pegar.addActionListener(e -> campo.paste());
        menu.add(cortar); menu.add(copiar); menu.add(pegar);
        campo.setComponentPopupMenu(menu);
    }

    private JPanel crearIndicadorEstado(String texto, Color color, int x, int y) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, 160, 30);
        panel.setOpaque(false);
        JPanel circulo = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(color);
                g.fillOval(0, 0, 16, 16);
            }
        };
        circulo.setBounds(0, 7, 16, 16);
        circulo.setOpaque(false);
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(25, 5, 120, 20);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 15)); 
        lbl.setForeground(AZUL);
        panel.add(circulo); panel.add(lbl);
        return panel;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setBackground(AZUL);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton crearBotonBorde(String texto) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(160, 45));
        btn.setForeground(AZUL);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(AZUL, 2));
        return btn;
    }

    private void abrirDetalleTurno() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(this, "Seleccione un turno"); return; }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        int idTurno = (int) modelo.getValueAt(filaModelo, 0);
        new DetalleTurno(modelo, idTurno).setVisible(true);
    }

    private void cargarTurnosDesdeBD() {
        modelo.setRowCount(0);
        String selectSql = "SELECT t.id_turno, c.nombre, c.apellido, t.fecha, t.hora, t.estado " +
                           "FROM turnos t INNER JOIN clientes c ON t.id_cliente = c.id_cliente";
        try (Connection con = conexion.getConexion();
             PreparedStatement psSel = con.prepareStatement(selectSql);
             ResultSet rs = psSel.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id_turno"), 
                    rs.getString("nombre"), 
                    rs.getString("apellido"),
                    rs.getDate("fecha").toString(), 
                    rs.getTime("hora").toString(), 
                    rs.getString("estado")
                });
            }
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage()); 
        }
    }

    private void aplicarFiltro() {
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                String texto = txtBuscar.getText().toLowerCase();
                String fecha = txtFechaFiltro.getText();
                return (e.getStringValue(1).toLowerCase().contains(texto)
                        || e.getStringValue(2).toLowerCase().contains(texto))
                        && (fecha.isEmpty() || e.getStringValue(3).contains(fecha));
            }
        });
    }

    class EstadoTablaRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            String estado = (value != null) ? value.toString().toLowerCase() : "";
            Color color;

            if (estado.equals("cancelado")) {
                color = ROJO;
            } else if (estado.equals("en proceso")) {
                color = AMARILLO;
            } else if (estado.equals("terminado")) {
                color = VERDE;
            } else {
                color = GRIS_ESTADO;
            }

            JPanel panel = new JPanel(new GridBagLayout()) {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(color);
                    int size = 18;
                    g2.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2, size, size);
                }
            };
            panel.setOpaque(true);
            panel.setBackground(isSelected ? AZUL_CLARO : Color.WHITE);
            return panel;
        }
    }

    class PanelConFondo extends JPanel {
        private Image fondo;
        public PanelConFondo() {
            try {
                URL url = getClass().getResource("/Imagenes/fondotabla1.png");
                fondo = (url != null) ? new ImageIcon(url).getImage()
                                      : new ImageIcon("bin/Imagenes/fondotabla1.png").getImage();
            } catch (Exception e) {}
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondo != null) g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new VerTurnosVentana("Empleado").setVisible(true)
        );
    }
}
