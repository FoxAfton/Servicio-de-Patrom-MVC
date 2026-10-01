package modelo;

/** Se lanza al buscar un producto que no existe en el inventario. */
public class ProductoNoEncontradoException extends Exception {
    public ProductoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
