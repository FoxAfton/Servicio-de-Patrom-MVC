package vista;

/** Avisos que el controlador le da a la vista que originó una operación. */
public interface Mensajes {
    void mostrarDatosInvalidos();
    void mostrarAgregado();
    void mostrarNoExiste();
    void mostrarStockInsuficiente();
    void mostrarVentaOk(int quedan);
    void mostrarNumeroInvalido();
}
