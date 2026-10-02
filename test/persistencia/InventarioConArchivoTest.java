package persistencia;

import static org.junit.Assert.assertEquals;

import modelo.Inventario;
import modelo.InventarioTest;
import modelo.ProductoDAO;
import org.junit.Test;

import java.io.File;

/**
 * Corre TODAS las pruebas de Inventario (stock, precios, total, inexistentes)
 * sobre la version en archivo, sin tocar una sola prueba.
 */
public class InventarioConArchivoTest extends InventarioTest {

    private File archivo;

    @Override
    protected ProductoDAO crearDAO() throws Exception {
        archivo = File.createTempFile("inventario-test", ".dat");
        archivo.delete();
        archivo.deleteOnExit();
        return new ProductoDAOArchivo(archivo.getPath());
    }

    @Test
    public void unNuevoInventarioRecuperaLoGuardado() throws Exception {
        inv.vender("Teclado", 2);
        Inventario reabierto = new Inventario(new ProductoDAOArchivo(archivo.getPath()));
        assertEquals(3, reabierto.buscar("Teclado").getCantidad());
        assertEquals(1050.0, reabierto.valorTotal(), 0.001);
    }
}
