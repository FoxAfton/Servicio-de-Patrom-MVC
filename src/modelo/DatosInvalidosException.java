package modelo;

/** Se lanza al crear un producto con nombre vacío, cantidad negativa o precio no positivo. */
public class DatosInvalidosException extends Exception {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
