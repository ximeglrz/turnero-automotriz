import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;
import src.dao.TurnoDAO;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import javax.swing.text.JTextComponent;

public class RegistroTurno extends JFrame {

    private static final Color COLOR_AZUL_OSCURO_FONDO = new Color(0x052659);
    private static final Color COLOR_FONDO_TARJETA = new Color(0xE1D4C2);
    private static final Color COLOR_BOTON = new Color(0x052659);
    private static final Color COLOR_BOTON_HOVER = new Color(0x0A3A85);

    private JTextField txtNombre, txtApellido, txtTelefono, txtCorreo;
    private JTextField txtPatente;
    private JDateChooser dateFecha;
    private JComboBox<String> cmbHora;
    private JComboBox<String> cmbServicio;

    public RegistroTurno() {

        setTitle("Registro de Turnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));

        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image img = new ImageIcon(
                    System.getProperty("user.dir") + "/bin/Imagenes/fondo-login.png"
                ).getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };

        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_TARJETA);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JPanel contenedorCentro = new JPanel(new GridBagLayout());
        contenedorCentro.setOpaque(false);

        GridBagConstraints gbcCentro = new GridBagConstraints();
        gbcCentro.gridx = 0;
        gbcCentro.gridy = 0;
        gbcCentro.weightx = 1;
        gbcCentro.weighty = 1;
        gbcCentro.anchor = GridBagConstraints.CENTER;
        gbcCentro.insets = new Insets(0, 0, 0, 0);
        contenedorCentro.add(panel, gbcCentro);

        JScrollPane scroll = new JScrollPane(contenedorCentro);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setViewportBorder(null);
        fondo.add(scroll, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel("REGISTRO DE CLIENTE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitulo.setForeground(COLOR_AZUL_OSCURO_FONDO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        panelSuperior.add(lblTitulo, BorderLayout.CENTER);
        fondo.add(panelSuperior, BorderLayout.NORTH);

        int fila = 0;

        txtNombre   = crearCampo(panel, "Nombre", 0, fila);
        txtApellido = crearCampo(panel, "Apellido", 1, fila);
        soloLetras(txtNombre);
        soloLetras(txtApellido);
        fila += 2;

        txtTelefono = crearCampo(panel, "Teléfono", 0, fila);
        soloNumeros(txtTelefono);
        txtCorreo   = crearCampo(panel, "Correo Electrónico", 1, fila);
        fila += 2;

        txtPatente  = crearCampo(panel, "Patente del Vehículo", 0, fila);
        fila += 2;

        moverConEnter(txtNombre, txtApellido);
        moverConEnter(txtApellido, txtTelefono);
        moverConEnter(txtTelefono, txtCorreo);
        moverConEnter(txtCorreo, txtPatente);

        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 20, 5, 20);
        gbc1.gridx = 0;
        gbc1.gridy = fila;
        gbc1.anchor = GridBagConstraints.WEST;
        JLabel lblFecha = new JLabel("Fecha");
        lblFecha.setFont(new Font("Arial", Font.BOLD, 16));
        lblFecha.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblFecha, gbc1);

        gbc1 = new GridBagConstraints();
        gbc1.insets = new Insets(10, 20, 5, 20);
        gbc1.gridx = 1;
        gbc1.gridy = fila;
        gbc1.anchor = GridBagConstraints.WEST;
        JLabel lblHora = new JLabel("Hora");
        lblHora.setFont(new Font("Arial", Font.BOLD, 16));
        lblHora.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblHora, gbc1);

        fila++;

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        dateFecha = new JDateChooser();
        dateFecha.setDateFormatString("yyyy-MM-dd");
        panel.add(dateFecha, gbc);

        JTextField editorFecha = (JTextField) dateFecha.getDateEditor().getUiComponent();
        editorFecha.setEditable(false);
        editorFecha.setFocusable(false);

        dateFecha.addPropertyChangeListener("date", evt -> {
            if (dateFecha.getDate() != null) {
                cmbHora.setEnabled(true);
                cargarHorariosDisponibles();
            }
        });

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        cmbHora = new JComboBox<>();
        cmbHora.setEnabled(false);
        cmbHora.addItem("Seleccione horario");
        panel.add(cmbHora, gbc);

        fila++;

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblServicio = new JLabel("Tipo de Servicio");
        lblServicio.setFont(new Font("Arial", Font.BOLD, 16));
        lblServicio.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lblServicio, gbc);

        fila++;

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        cmbServicio = new JComboBox<>(new String[]{
            "Seleccione una opción",
            "Polarizado",
            "Instalación de Audio",
            "Ambos"
        });
        panel.add(cmbServicio, gbc);

        fila++;

        JButton btnCancelar  = crearBoton("Cancelar", 0, 0);
        JButton btnLimpiar   = crearBotonSecundario("Limpiar", 0, 0);
        JButton btnAgenda    = crearBotonSecundario("Agenda", 0, 0);
        JButton btnSiguiente = crearBoton("Siguiente", 0, 0);

        JPanel panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAgenda);
        panelBotones.add(btnSiguiente);

        getRootPane().setDefaultButton(btnSiguiente);

        JPanel contenedorSur = new JPanel(new BorderLayout());
        contenedorSur.setOpaque(false);
        contenedorSur.add(panelBotones, BorderLayout.CENTER);
        contenedorSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        fondo.add(contenedorSur, BorderLayout.SOUTH);

        // CAMBIO: WindowFocusListener para recargar horarios al volver desde la agenda.
        // Si el empleado canceló un turno en VerTurnoVentana y vuelve aquí,
        // la fecha ya estaba seleccionada: recargamos el combo para mostrar
        // el horario recién liberado.
        this.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                if (dateFecha.getDate() != null) {
                    cargarHorariosDisponibles();
                }
            }
            @Override
            public void windowLostFocus(WindowEvent e) {}
        });

        btnLimpiar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere limpiar todos los campos?",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                limpiarCampos();
            }
        });

        btnCancelar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere cancelar?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                new MenuPrincipal().setVisible(true);
                dispose();
            }
        });

        btnAgenda.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro quiere ir a la agenda?",
                "Confirmar acción",
                JOptionPane.YES_NO_OPTION
            );
            if (opcion == JOptionPane.YES_OPTION) {
                new VerTurnoVentana("Empleado").setVisible(true);
                dispose();
            }
        });

        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtNombre.getText().isEmpty() ||
                    txtApellido.getText().isEmpty() ||
                    txtTelefono.getText().isEmpty() ||
                    txtCorreo.getText().isEmpty() ||
                    txtPatente.getText().isEmpty() ||
                    dateFecha.getDate() == null ||
                    cmbHora.getSelectedIndex() == 0 ||
                    cmbServicio.getSelectedIndex() == 0) {

                    JOptionPane.showMessageDialog(
                        RegistroTurno.this,
                        "Solo se permiten turnos de lunes a viernes.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (!emailValido(txtCorreo.getText())) {
                    JOptionPane.showMessageDialog(
                        RegistroTurno.this,
                        "Ingrese un correo electrónico válido",
                        "Email inválido",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (txtTelefono.getText().length() < 8) {
                    JOptionPane.showMessageDialog(
                        RegistroTurno.this,
                        "El teléfono debe tener al menos 8 dígitos",
                        "Teléfono inválido",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (!fechaNoEsPasada(dateFecha.getDate())) {
                    JOptionPane.showMessageDialog(
                        RegistroTurno.this,
                        "No se puede seleccionar una fecha pasada",
                        "Fecha inválida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (!esDiaHabil(dateFecha.getDate())) {
                    JOptionPane.showMessageDialog(
                        RegistroTurno.this,
                        "Solo se permiten turnos de lunes a viernes",
                        "Día no habilitado",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(dateFecha.getDate());
                String hora = cmbHora.getSelectedItem().toString();

                new ResumenTurno(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtCorreo.getText(),
                    txtPatente.getText(),
                    fecha,
                    hora,
                    cmbServicio.getSelectedItem().toString()
                ).setVisible(true);

                dispose();
            }
        });

        setSize(1100, 750);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public RegistroTurno(
            String nombre,
            String apellido,
            String telefono,
            String correo,
            String patente,
            String fecha,
            String hora,
            String servicio
    ) {
        this();
        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtTelefono.setText(telefono);
        txtCorreo.setText(correo);
        txtPatente.setText(patente);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(fecha);
            dateFecha.setDate(d);
            cmbHora.setEnabled(true);
        } catch (Exception e) {}

        cmbHora.setSelectedItem(hora);
        cmbServicio.setSelectedItem(servicio);
    }

    private JTextField crearCampo(JPanel panel, String texto, int col, int fila) {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 5, 20);
        gbc.gridx = col;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(COLOR_AZUL_OSCURO_FONDO);
        panel.add(lbl, gbc);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 20, 10, 20);
        gbc.gridx = col;
        gbc.gridy = fila + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField txt = new JTextField();
        txt.setFont(new Font("Arial", Font.PLAIN, 15));
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        agregarMenuCopiarPegar(txt);
        panel.add(txt, gbc);

        return txt;
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(130, 50));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_BOTON);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(COLOR_BOTON_HOVER); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(COLOR_BOTON); }
        });
        return btn;
    }

    private JButton crearBotonSecundario(String texto, int x, int y) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(130, 50));
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(COLOR_AZUL_OSCURO_FONDO);
        btn.setBackground(COLOR_FONDO_TARJETA);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_AZUL_OSCURO_FONDO, 2));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new BasicButtonUI());
        return btn;
    }

    private void moverConEnter(JComponent actual, JComponent siguiente) {
        if (actual instanceof JTextField) {
            ((JTextField) actual).addActionListener(e -> siguiente.requestFocusInWindow());
        }
    }

    private boolean fechaNoEsPasada(Date fechaSeleccionada) {
        Date hoy = new Date();
        hoy.setHours(0); hoy.setMinutes(0); hoy.setSeconds(0);
        return !fechaSeleccionada.before(hoy);
    }

    private boolean esDiaHabil(Date fecha) {
        int diaSemana = fecha.getDay();
        return diaSemana != 0 && diaSemana != 6;
    }

    private boolean emailValido(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    private void agregarMenuCopiarPegar(JTextComponent campo) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem cortar = new JMenuItem("Cortar");
        JMenuItem copiar = new JMenuItem("Copiar");
        JMenuItem pegar  = new JMenuItem("Pegar");
        cortar.addActionListener(e -> campo.cut());
        copiar.addActionListener(e -> campo.copy());
        pegar.addActionListener(e -> campo.paste());
        menu.add(cortar); menu.add(copiar); menu.add(pegar);
        campo.setComponentPopupMenu(menu);
    }

    private void soloLetras(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != ' ' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }

    private void soloNumeros(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtPatente.setText("");
        dateFecha.setDate(null);
        JTextField txtFecha = (JTextField) dateFecha.getDateEditor().getUiComponent();
        txtFecha.setText("Seleccione fecha");
        txtFecha.setForeground(Color.GRAY);
        cmbHora.setSelectedIndex(0);
        cmbServicio.setSelectedIndex(0);
    }

    private void cargarHorariosDisponibles() {
        cmbHora.removeAllItems();
        cmbHora.addItem("Seleccione horario");

        if (dateFecha.getDate() == null) {
            cmbHora.setEnabled(false);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdf.format(dateFecha.getDate());

        SimpleDateFormat sdfHoy = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = sdfHoy.format(new Date());
        boolean esHoy = fecha.equals(hoy);

        Calendar ahora = Calendar.getInstance();
        int horaActual   = ahora.get(Calendar.HOUR_OF_DAY);
        int minutoActual = ahora.get(Calendar.MINUTE);

        TurnoDAO dao = new TurnoDAO();

        for (int h = 9; h < 13; h++) {
            verificarYAgregarHora(dao, fecha, String.format("%02d:00", h), esHoy, horaActual, minutoActual);
            verificarYAgregarHora(dao, fecha, String.format("%02d:30", h), esHoy, horaActual, minutoActual);
        }

        for (int h = 18; h < 21; h++) {
            verificarYAgregarHora(dao, fecha, String.format("%02d:00", h), esHoy, horaActual, minutoActual);
            verificarYAgregarHora(dao, fecha, String.format("%02d:30", h), esHoy, horaActual, minutoActual);
        }

        if (cmbHora.getItemCount() == 1) {
            JOptionPane.showMessageDialog(this, "No hay horarios disponibles", "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
            cmbHora.setEnabled(false);
        }
    }

    private void verificarYAgregarHora(TurnoDAO dao, String fecha, String hora,
                                        boolean esHoy, int horaActual, int minutoActual) {
        if (esHoy) {
            String[] partes = hora.split(":");
            int horaTurno   = Integer.parseInt(partes[0]);
            int minutoTurno = Integer.parseInt(partes[1]);
            if (horaTurno < horaActual || (horaTurno == horaActual && minutoTurno <= minutoActual)) {
                return;
            }
        }

        if (dao.horarioDisponible(fecha, hora)) {
            cmbHora.addItem(hora);
        }
    }
}