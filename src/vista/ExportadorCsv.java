package vista;

import modelo.Producto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

/** Escribe el inventario en un archivo CSV. Lo usan VistaCsv y ObservadorCsv. */
final class ExportadorCsv {
    private ExportadorCsv() {}

    static void exportar(String archivo, List<Producto> productos) throws IOException {
        try (PrintWriter out = new PrintWriter(archivo, "UTF-8")) {
            out.println("nombre,cantidad,precio");
            for (Producto p : productos) {
                out.println(escapar(p.getNombre()) + "," + p.getCantidad() + ","
                        + String.format(Locale.ROOT, "%.2f", p.getPrecio()));
            }
        }
    }

    private static String escapar(String texto) {
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
