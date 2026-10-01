package controlador;

import modelo.DatosInvalidosException;
import modelo.Inventario;
import modelo.ProductoNoEncontradoException;
import modelo.StockInsuficienteException;
import vista.VistaConsola;

/** Interpreta la entrada, invoca al modelo y le dice a la vista qué mostrar. */
public class ControladorInventario {
    private final Inventario inventario;
    private final VistaConsola vista;

    public ControladorInventario(Inventario inventario, VistaConsola vista) {
        this.inventario = inventario;
        this.vista = vista;
    }

    public void iniciar() {
        int op;
        do {
            op = Integer.parseInt(vista.pedirOpcion());
            switch (op) {
                case 1: agregar(); break;
                case 2: vender();  break;
                case 3: vista.mostrarListado(inventario.listar()); break;
                case 4: vista.mostrarValorTotal(inventario.valorTotal()); break;
                default: break;
            }
        } while (op != 0);
    }

    private void agregar() {
        String nombre = vista.pedir("Nombre: ");
        int cantidad = Integer.parseInt(vista.pedir("Cantidad: "));
        double precio = Double.parseDouble(vista.pedir("Precio: "));
        try {
            inventario.agregar(nombre, cantidad, precio);
            vista.mostrarAgregado();
        } catch (DatosInvalidosException e) {
            vista.mostrarDatosInvalidos();
        }
    }

    private void vender() {
        String nombre = vista.pedir("Producto: ");
        try {
            inventario.buscar(nombre);
            int cantidad = Integer.parseInt(vista.pedir("Cantidad a vender: "));
            int quedan = inventario.vender(nombre, cantidad);
            vista.mostrarVentaOk(quedan);
        } catch (ProductoNoEncontradoException e) {
            vista.mostrarNoExiste();
        } catch (StockInsuficienteException e) {
            vista.mostrarStockInsuficiente();
        }
    }
}
