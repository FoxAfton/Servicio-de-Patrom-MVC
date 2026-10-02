package persistencia;

import static org.junit.Assert.assertEquals;

import modelo.Producto;
import modelo.ProductoDAO;
import org.junit.Test;

import java.io.File;

/** Mismo contrato que la version en memoria, mas lo propio de persistir en disco. */
public class ProductoDAOArchivoTest extends ProductoDAOContratoTest {

    private File archivo;

    @Override
    protected ProductoDAO crear() throws Exception {
        archivo = File.createTempFile("inventario-test", ".dat");
        archivo.delete();                 // que empiece sin archivo
        archivo.deleteOnExit();
        return new ProductoDAOArchivo(archivo.getPath());
    }

    @Test
    public void losDatosSobrevivenAUnaNuevaInstancia() throws Exception {
        dao.guardar(new Producto("Teclado", 5, 350.0));
        Producto p = dao.buscarPorNombre("Teclado").get();
        p.vender(1);
        dao.actualizar(p);

        ProductoDAO otraEjecucion = new ProductoDAOArchivo(archivo.getPath());
        assertEquals(1, otraEjecucion.listarTodos().size());
        assertEquals(4, otraEjecucion.buscarPorNombre("Teclado").get().getCantidad());
    }
}
