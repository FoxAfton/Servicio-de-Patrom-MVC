package vista;

import modelo.Producto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

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
        try (PrintWriter out = new PrintWriter(archivo, "UTF-8")) {
            out.println("nombre,cantidad,precio");
            for (Producto p : productos) {
                out.println(escapar(p.getNombre()) + "," + p.getCantidad() + ","
                        + String.format(Locale.ROOT, "%.2f", p.getPrecio()));
            }
            System.out.println("Inventario exportado a " + archivo);
        } catch (IOException e) {
            System.out.println("No se pudo exportar: " + e.getMessage());
        }
    }

    private String escapar(String texto) {
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
