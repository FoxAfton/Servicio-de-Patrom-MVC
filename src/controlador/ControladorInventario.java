package controlador;

import modelo.DatosInvalidosException;
import modelo.Inventario;
import modelo.ProductoNoEncontradoException;
import modelo.StockInsuficienteException;
import vista.Mensajes;
import vista.Vista;

/**
 * Interpreta la entrada, invoca al modelo y le dice a la vista que origino la
 * operacion que mostrar. Sirve a cualquier vista: la de menu y la grafica.
 */
public class ControladorInventario {
    private final Inventario inventario;
    private final Vista vista;

    public ControladorInventario(Inventario inventario, Vista vista) {
        this.inventario = inventario;
        this.vista = vista;
    }

    // ---------- Flujo del menu (vista de consola) ----------
    public void iniciar() {
        int op;
        do {
            op = Integer.parseInt(vista.pedirOpcion());
            switch (op) {
                case 1: agregarPorMenu(); break;
                case 2: venderPorMenu();  break;
                case 3: vista.mostrarListado(inventario.listar()); break;
                case 4: vista.mostrarValorTotal(inventario.valorTotal()); break;
                default: break;
            }
        } while (op != 0);
    }

    private void agregarPorMenu() {
        String nombre = vista.pedir("Nombre: ");
        int cantidad = Integer.parseInt(vista.pedir("Cantidad: "));
        double precio = Double.parseDouble(vista.pedir("Precio: "));
        agregar(nombre, cantidad, precio, vista);
    }

    private void venderPorMenu() {
        String nombre = vista.pedir("Producto: ");
        try {
            inventario.buscar(nombre);
        } catch (ProductoNoEncontradoException e) {
            vista.mostrarNoExiste();
            return;
        }
        int cantidad = Integer.parseInt(vista.pedir("Cantidad a vender: "));
        vender(nombre, cantidad, vista);
    }

    // ---------- Operaciones que cualquier vista puede pedir ----------
    public void agregar(String nombre, int cantidad, double precio, Mensajes origen) {
        try {
            inventario.agregar(nombre, cantidad, precio);
            origen.mostrarAgregado();
        } catch (DatosInvalidosException e) {
            origen.mostrarDatosInvalidos();
        }
    }

    public void vender(String nombre, int cantidad, Mensajes origen) {
        try {
            int quedan = inventario.vender(nombre, cantidad);
            origen.mostrarVentaOk(quedan);
        } catch (ProductoNoEncontradoException e) {
            origen.mostrarNoExiste();
        } catch (StockInsuficienteException e) {
            origen.mostrarStockInsuficiente();
        }
    }

    /** Para vistas que entregan texto sin validar (formularios). */
    public void agregarDesdeTexto(String nombre, String cantidad, String precio, Mensajes origen) {
        try {
            agregar(nombre, Integer.parseInt(cantidad.trim()), Double.parseDouble(precio.trim()), origen);
        } catch (NumberFormatException e) {
            origen.mostrarNumeroInvalido();
        }
    }

    public void venderDesdeTexto(String nombre, String cantidad, Mensajes origen) {
        try {
            vender(nombre, Integer.parseInt(cantidad.trim()), origen);
        } catch (NumberFormatException e) {
            origen.mostrarNumeroInvalido();
        }
    }
}
