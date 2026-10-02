package persistencia;

import modelo.DatosInvalidosException;
import modelo.ErrorDePersistenciaException;
import modelo.Producto;
import modelo.ProductoDAO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persiste los datos en un archivo de texto: una linea por producto,
 * "nombre TAB cantidad TAB precio". Los datos sobreviven entre ejecuciones.
 * Cada operacion lee/escribe el archivo; es sencillo y suficiente para este tamano.
 */
public class ProductoDAOArchivo implements ProductoDAO {
    private final Path archivo;

    public ProductoDAOArchivo(String ruta) {
        this.archivo = Paths.get(ruta);
    }

    @Override
    public synchronized void guardar(Producto producto) {
        try {
            Files.write(archivo, (formatear(producto) + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ErrorDePersistenciaException("No se pudo escribir " + archivo, e);
        }
    }

    @Override
    public synchronized void actualizar(Producto producto) {
        List<Producto> todos = leer();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getNombre().equals(producto.getNombre())) {
                todos.set(i, producto);
                break;
            }
        }
        escribirTodo(todos);
    }

    @Override
    public synchronized Optional<Producto> buscarPorNombre(String nombre) {
        for (Producto p : leer()) {
            if (p.getNombre().equals(nombre)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public synchronized List<Producto> listarTodos() {
        return leer();
    }

    // ---------- lectura / escritura ----------
    private List<Producto> leer() {
        List<Producto> productos = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return productos;
        }
        try {
            for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
                if (!linea.isEmpty()) {
                    productos.add(parsear(linea));
                }
            }
        } catch (IOException e) {
            throw new ErrorDePersistenciaException("No se pudo leer " + archivo, e);
        }
        return productos;
    }

    private void escribirTodo(List<Producto> productos) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Producto p : productos) {
                sb.append(formatear(p)).append(System.lineSeparator());
            }
            Path temporal = Paths.get(archivo.toString() + ".tmp");
            Files.write(temporal, sb.toString().getBytes(StandardCharsets.UTF_8));
            Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ErrorDePersistenciaException("No se pudo escribir " + archivo, e);
        }
    }

    // ---------- formato ----------
    private static String formatear(Producto p) {
        return escapar(p.getNombre()) + "\t" + p.getCantidad() + "\t" + p.getPrecio();
    }

    private static Producto parsear(String linea) {
        String[] campos = linea.split("\t", -1);
        if (campos.length != 3) {
            throw new ErrorDePersistenciaException("Linea con formato invalido: " + linea);
        }
        try {
            return new Producto(desescapar(campos[0]), Integer.parseInt(campos[1]), Double.parseDouble(campos[2]));
        } catch (NumberFormatException | DatosInvalidosException e) {
            throw new ErrorDePersistenciaException("Datos invalidos en el archivo: " + linea, e);
        }
    }

    private static String escapar(String s) {
        return s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String desescapar(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(++i);
                sb.append(n == 't' ? '\t' : n == 'n' ? '\n' : n == 'r' ? '\r' : n);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
