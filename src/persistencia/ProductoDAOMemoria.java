package persistencia;

import modelo.Producto;
import modelo.ProductoDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Mantiene los datos en una lista; se pierden al cerrar el programa. */
public class ProductoDAOMemoria implements ProductoDAO {
    private final List<Producto> productos = new ArrayList<>();

    @Override
    public synchronized void guardar(Producto producto) {
        productos.add(producto);
    }

    @Override
    public synchronized void actualizar(Producto producto) {
        // Se guarda el mismo objeto que se devuelve en buscar: nada que hacer.
    }

    @Override
    public synchronized Optional<Producto> buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equals(nombre)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}
