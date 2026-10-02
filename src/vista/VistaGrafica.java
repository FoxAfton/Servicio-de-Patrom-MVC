package vista;

import controlador.ControladorInventario;
import modelo.Inventario;
import modelo.ObservadorInventario;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Ventana Swing. Captura datos y se los entrega al controlador; se redibuja sola
 * cuando el modelo avisa que cambio (sin importar quien lo cambio).
 */
public class VistaGrafica implements ObservadorInventario, Mensajes {
    private final ControladorInventario controlador;

    final JFrame ventana = new JFrame("Inventario - vista grafica");
    final DefaultTableModel tabla = new DefaultTableModel(new Object[]{"Producto", "Cantidad", "Precio"}, 0) {
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    final JTable jtabla = new JTable(tabla);
    final JTextField txtNombre = new JTextField(10);
    final JTextField txtCantidad = new JTextField(5);
    final JTextField txtPrecio = new JTextField(6);
    final JButton btnAgregar = new JButton("Agregar");
    final JButton btnVender = new JButton("Vender");
    final JLabel lblTotal = new JLabel("Valor total: $0.0");
    final JLabel lblEstado = new JLabel(" ");

    public VistaGrafica(ControladorInventario controlador, Inventario inventario) {
        this.controlador = controlador;
        construir();
        refrescar(inventario.listar(), inventario.valorTotal());
    }

    private void construir() {
        JPanel entrada = new JPanel(new FlowLayout(FlowLayout.LEFT));
        entrada.add(new JLabel("Nombre:"));   entrada.add(txtNombre);
        entrada.add(new JLabel("Cantidad:")); entrada.add(txtCantidad);
        entrada.add(new JLabel("Precio:"));   entrada.add(txtPrecio);
        entrada.add(btnAgregar);
        entrada.add(btnVender);

        JPanel pie = new JPanel(new GridLayout(2, 1));
        pie.add(lblTotal);
        pie.add(lblEstado);

        ventana.setLayout(new BorderLayout(5, 5));
        ventana.add(new JScrollPane(jtabla), BorderLayout.CENTER);
        ventana.add(entrada, BorderLayout.NORTH);
        ventana.add(pie, BorderLayout.SOUTH);
        ventana.setSize(640, 320);
        ventana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jtabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = jtabla.getSelectedRow();
            if (!e.getValueIsAdjusting() && fila >= 0) {
                txtNombre.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            }
        });
        btnAgregar.addActionListener(e ->
                controlador.agregarDesdeTexto(txtNombre.getText(), txtCantidad.getText(), txtPrecio.getText(), this));
        btnVender.addActionListener(e ->
                controlador.venderDesdeTexto(txtNombre.getText(), txtCantidad.getText(), this));
    }

    public void mostrar() {
        ventana.setVisible(true);
    }

    // ---- ObservadorInventario: puede llamarse desde cualquier hilo ----
    @Override
    public void inventarioCambio(Inventario inventario) {
        List<Producto> productos = inventario.listar();
        double total = inventario.valorTotal();
        SwingUtilities.invokeLater(() -> refrescar(productos, total));
    }

    private void refrescar(List<Producto> productos, double total) {
        tabla.setRowCount(0);
        for (Producto p : productos) {
            tabla.addRow(new Object[]{p.getNombre(), p.getCantidad(), String.format("$%.2f", p.getPrecio())});
        }
        lblTotal.setText("Valor total: $" + total);
    }

    // ---- Mensajes del controlador ----
    @Override public void mostrarDatosInvalidos()    { lblEstado.setText("Datos invalidos"); }
    @Override public void mostrarAgregado()          { lblEstado.setText("Agregado."); }
    @Override public void mostrarNoExiste()          { lblEstado.setText("No existe"); }
    @Override public void mostrarStockInsuficiente() { lblEstado.setText("Stock insuficiente"); }
    @Override public void mostrarVentaOk(int quedan) { lblEstado.setText("Venta ok. Quedan " + quedan); }
    @Override public void mostrarNumeroInvalido()    { lblEstado.setText("Numero invalido"); }
}
