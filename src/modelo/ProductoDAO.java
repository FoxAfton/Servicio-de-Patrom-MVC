package modelo;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos que el modelo necesita. El modelo define QUE necesita
 * guardar y recuperar; no sabe si eso ocurre en memoria, en un archivo o en una base de datos.
 */
public interface ProductoDAO {
    /** Agrega un producto nuevo al final. */
    void guardar(Producto producto);

    /** Persiste el estado actual (p. ej. el stock) del primer producto con ese nombre. */
    void actualizar(Producto producto);

    /** Primer producto con ese nombre, si existe. */
    Optional<Producto> buscarPorNombre(String nombre);

    /** Todos los productos, en orden de insercion. Devuelve una copia. */
    List<Producto> listarTodos();
}
