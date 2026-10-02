package persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import modelo.Producto;
import modelo.ProductoDAO;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/** Lo que TODA implementacion de ProductoDAO debe cumplir. Las subclases solo dicen como crearla. */
public abstract class ProductoDAOContratoTest {

    protected ProductoDAO dao;

    protected abstract ProductoDAO crear() throws Exception;

    @Before
    public void preparar() throws Exception {
        dao = crear();
    }

    @Test
    public void alInicioNoHayProductos() {
        assertTrue(dao.listarTodos().isEmpty());
    }

    @Test
    public void guardarConservaDatosYOrden() throws Exception {
        dao.guardar(new Producto("Teclado", 5, 350.0));
        dao.guardar(new Producto("Mouse", 10, 120.5));
        List<Producto> todos = dao.listarTodos();
        assertEquals(2, todos.size());
        assertEquals("Teclado", todos.get(0).getNombre());
        assertEquals(5, todos.get(0).getCantidad());
        assertEquals(350.0, todos.get(0).getPrecio(), 0.0);
        assertEquals("Mouse", todos.get(1).getNombre());
        assertEquals(120.5, todos.get(1).getPrecio(), 0.0);
    }

    @Test
    public void buscarEncuentraExistenteYNoInventaInexistente() throws Exception {
        dao.guardar(new Producto("Teclado", 5, 350.0));
        assertTrue(dao.buscarPorNombre("Teclado").isPresent());
        assertFalse(dao.buscarPorNombre("Monitor").isPresent());
    }

    @Test
    public void actualizarPersisteElNuevoStock() throws Exception {
        dao.guardar(new Producto("Teclado", 5, 350.0));
        Producto p = dao.buscarPorNombre("Teclado").get();
        p.vender(2);
        dao.actualizar(p);
        assertEquals(3, dao.buscarPorNombre("Teclado").get().getCantidad());
        assertEquals(3, dao.listarTodos().get(0).getCantidad());
    }

    @Test
    public void conNombresRepetidosBuscarDevuelveElPrimero() throws Exception {
        dao.guardar(new Producto("Cable", 1, 10.0));
        dao.guardar(new Producto("Cable", 2, 20.0));
        assertEquals(1, dao.buscarPorNombre("Cable").get().getCantidad());
    }

    @Test
    public void nombresConCaracteresEspecialesSeConservan() throws Exception {
        dao.guardar(new Producto("Cable, USB \"3m\"\tnuevo\\", 4, 45.25));
        assertEquals("Cable, USB \"3m\"\tnuevo\\", dao.listarTodos().get(0).getNombre());
    }
}
