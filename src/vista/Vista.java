package vista;

import modelo.Producto;

import java.util.List;

/** Vista interactiva por menu (consola). Ademas de los avisos, pide datos y muestra resultados. */
public interface Vista extends Mensajes {
    String pedirOpcion();
    String pedir(String etiqueta);

    void mostrarListado(List<Producto> productos);
    void mostrarValorTotal(double total);
}
