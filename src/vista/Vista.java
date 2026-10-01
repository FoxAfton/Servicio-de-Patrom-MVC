package vista;

import modelo.Producto;

import java.util.List;

/** Contrato que el controlador conoce. Cualquier vista (consola, CSV, GUI) lo implementa. */
public interface Vista {
    String pedirOpcion();
    String pedir(String etiqueta);

    void mostrarDatosInvalidos();
    void mostrarAgregado();
    void mostrarNoExiste();
    void mostrarStockInsuficiente();
    void mostrarVentaOk(int quedan);
    void mostrarListado(List<Producto> productos);
    void mostrarValorTotal(double total);
}
