package vista;

import modelo.Producto;

import java.io.IOException;
import java.util.List;

/**
 * Segunda vista: igual que la consola para pedir datos y dar avisos,
 * pero la opcion "Listar" exporta el inventario a un archivo CSV.
 */
public class VistaCsv extends VistaConsola {
    private final String archivo;

    public VistaCsv(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public void mostrarListado(List<Producto> productos) {
        try {
            ExportadorCsv.exportar(archivo, productos);
            System.out.println("Inventario exportado a " + archivo);
        } catch (IOException e) {
            System.out.println("No se pudo exportar: " + e.getMessage());
        }
    }
}
