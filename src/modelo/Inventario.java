package modelo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Estado y reglas de negocio del inventario. No imprime ni lee nada,
 * y tampoco sabe donde se guardan los datos: eso lo resuelve el ProductoDAO.
 */
public class Inventario {
    private final ProductoDAO dao;
    private final List<ObservadorInventario> observadores = new CopyOnWriteArrayList<>();

    public Inventario(ProductoDAO dao) {
        this.dao = dao;
    }

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
            dao.guardar(nuevo);
        }
        notificar();
    }

    /** Devuelve el primer producto con ese nombre, o lanza excepción si no existe. */
    public synchronized Producto buscar(String nombre) throws ProductoNoEncontradoException {
        Optional<Producto> encontrado = dao.buscarPorNombre(nombre);
        if (!encontrado.isPresent()) {
            throw new ProductoNoEncontradoException("No existe");
        }
        return encontrado.get();
    }

    /** Vende unidades de un producto y devuelve cuántas quedan. Notifica solo si la venta se hizo. */
    public int vender(String nombre, int unidades)
            throws ProductoNoEncontradoException, StockInsuficienteException {
        int quedan;
        synchronized (this) {
            Producto producto = buscar(nombre);
            quedan = producto.vender(unidades);
            dao.actualizar(producto);
        }
        notificar();
        return quedan;
    }

    /** Copia de solo lectura del estado actual (segura si otro hilo modifica el inventario). */
    public synchronized List<Producto> listar() {
        return Collections.unmodifiableList(dao.listarTodos());
    }

    public double valorTotal() {
        double total = 0;
        for (Producto p : listar()) {
            total += p.getValor();
        }
        return total;
    }
}
