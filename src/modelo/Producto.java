package modelo;

/** Entidad del dominio: reemplaza a las tres listas paralelas del código original. */
public class Producto {
    private final String nombre;
    private int cantidad;
    private final double precio;

    public Producto(String nombre, int cantidad, double precio) throws DatosInvalidosException {
        if (nombre.isEmpty() || cantidad < 0 || precio <= 0) {
            throw new DatosInvalidosException("Datos invalidos");
        }
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public synchronized int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }

    /** Valor en inventario de este producto (cantidad x precio). */
    public synchronized double getValor() {
        return cantidad * precio;
    }

    /** Regla: no se puede vender más de lo que hay en stock. Devuelve lo que queda. */
    public synchronized int vender(int unidades) throws StockInsuficienteException {
        if (unidades > cantidad) {
            throw new StockInsuficienteException("Stock insuficiente");
        }
        cantidad -= unidades;
        return cantidad;
    }
}
