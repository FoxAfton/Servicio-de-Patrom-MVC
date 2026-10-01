package modelo;

/** Se lanza al intentar vender más unidades de las que hay en stock. */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
