package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Estado y reglas de negocio del inventario. No imprime ni lee nada. */
public class Inventario {
    private final List<Producto> productos = new ArrayList<>();

    public void agregar(String nombre, int cantidad, double precio) throws DatosInvalidosException {
        productos.add(new Producto(nombre, cantidad, precio));
    }

    /** Devuelve el primer producto con ese nombre, o lanza excepción si no existe. */
    public Producto buscar(String nombre) throws ProductoNoEncontradoException {
        for (Producto p : productos) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        throw new ProductoNoEncontradoException("No existe");
    }

    /** Vende unidades de un producto y devuelve cuántas quedan. */
    public int vender(String nombre, int unidades)
            throws ProductoNoEncontradoException, StockInsuficienteException {
        return buscar(nombre).vender(unidades);
    }

    public List<Producto> listar() {
        return Collections.unmodifiableList(productos);
    }

    public double valorTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getValor();
        }
        return total;
    }
}
