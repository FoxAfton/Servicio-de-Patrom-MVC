package modelo;

/** Fallo al leer o escribir los datos (archivo ilegible, disco lleno, etc.). */
public class ErrorDePersistenciaException extends RuntimeException {
    public ErrorDePersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public ErrorDePersistenciaException(String mensaje) {
        super(mensaje);
    }
}
