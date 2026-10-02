package modelo;

/**
 * Contrato de quien quiere enterarse de los cambios del inventario.
 * El modelo solo conoce esta interfaz: no sabe si quien escucha es una ventana,
 * un archivo, una prueba o nada.
 */
public interface ObservadorInventario {
    void inventarioCambio(Inventario inventario);
}
