package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Estado y reglas de negocio del inventario. No imprime ni lee nada. */
public class Inventario {
    private final List<Producto> productos = new ArrayList<>();
    private final List<ObservadorInventario> observadores = new CopyOnWriteArrayList<>();

    // ---- Observadores ----
    public void agregarObservador(ObservadorInventario observador) {
        observadores.add(observador);
    }

    public void quitarObservador(ObservadorInventario observador) {
        observadores.remove(observador);
    }

    private void notificar() {
        for (ObservadorInventario o : observadores) {
            o.inventarioCambio(this);
        }
    }

    // ---- Operaciones ----
    public void agregar(String nombre, int cantidad, double precio) throws DatosInvalidosException {
        Producto nuevo = new Producto(nombre, cantidad, precio);
        synchronized (this) {
            productos.add(nuevo);
        }
        notificar();
    }

    /** Devuelve el primer producto con ese nombre, o lanza excepción si no existe. */
    public synchronized Producto buscar(String nombre) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No existe");
    }

    /** Vende unidades de un producto y devuelve cuántas quedan. Notifica solo si la venta se hizo. */
    public int vender(String nombre, int unidades)
            throws ProductoNoEncontradoException, StockInsuficienteException {
        int quedan = buscar(nombre).vender(unidades);
        notificar();
        return quedan;
    }

    /** Copia de solo lectura del estado actual (segura si otro hilo modifica el inventario). */
    public synchronized List<Producto> listar() {
        return Collections.unmodifiableList(new ArrayList<>(productos));
    }

    public double valorTotal() {
        double total = 0;
        for (Producto p : listar()) {
            total += p.getValor();
        }
        return total;
    }
}
