package modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import persistencia.ProductoDAOMemoria;
import org.junit.Test;

/**
 * Pruebas del modelo sin vista: no hay menus, ni Scanner, ni System.out.
 * Compatible con JUnit 4.x (en 4.5 no existe assertThrows, por eso se usa try/fail/catch).
 */
public class InventarioTest {

    protected Inventario inv;

    /** Las subclases cambian la implementacion de acceso a datos; las pruebas no cambian. */
    protected ProductoDAO crearDAO() throws Exception {
        return new ProductoDAOMemoria();
    }

    @Before
    public void preparar() throws Exception {
        inv = new Inventario(crearDAO());
        inv.agregar("Teclado", 5, 350.0);
    }

    // Caso 5
    @Test
    public void noSePuedeVenderMasDelStockDisponible() throws Exception {
        try {
            inv.vender("Teclado", 8);
            fail("Debio lanzar StockInsuficienteException");
        } catch (StockInsuficienteException esperada) {
            // correcto
        }
        assertEquals(5, inv.buscar("Teclado").getCantidad());
    }

    // Caso 6
    @Test
    public void venderCantidadValidaDescuentaExactamenteEsaCantidad() throws Exception {
        int quedan = inv.vender("Teclado", 2);
        assertEquals(3, quedan);
        assertEquals(3, inv.buscar("Teclado").getCantidad());
    }

    @Test
    public void sePuedeVenderTodoElStock() throws Exception {
        assertEquals(0, inv.vender("Teclado", 5));
        assertEquals(0, inv.buscar("Teclado").getCantidad());
    }

    // Caso 7
    @Test
    public void precioNegativoSeRechaza() {
        try {
            inv.agregar("Mouse", 3, -10.0);
            fail("Debio lanzar DatosInvalidosException");
        } catch (DatosInvalidosException esperada) {
            // correcto
        }
        assertEquals(1, inv.listar().size());
    }

    @Test
    public void precioCeroSeRechaza() {
        try {
            inv.agregar("Mouse", 3, 0.0);
            fail("Debio lanzar DatosInvalidosException");
        } catch (DatosInvalidosException esperada) {
            // correcto
        }
        assertEquals(1, inv.listar().size());
    }

    // Caso 8
    @Test
    public void valorTotalConVariosProductos() throws Exception {
        inv.agregar("Mouse", 10, 120.5);
        inv.agregar("Cable", 3, 45.25);
        // 5*350 + 10*120.5 + 3*45.25 = 1750 + 1205 + 135.75
        assertEquals(3090.75, inv.valorTotal(), 0.001);
    }

    @Test
    public void valorTotalDeInventarioVacioEsCero() throws Exception {
        assertEquals(0.0, new Inventario(crearDAO()).valorTotal(), 0.0);
    }

    // Caso 9 (decision de diseno: buscar lanza excepcion)
    @Test
    public void buscarProductoInexistenteLanzaExcepcion() {
        try {
            inv.buscar("Monitor");
            fail("Debio lanzar ProductoNoEncontradoException");
        } catch (ProductoNoEncontradoException esperada) {
            // correcto
        }
    }

    @Test
    public void venderProductoInexistenteLanzaExcepcion() throws Exception {
        try {
            inv.vender("Monitor", 1);
            fail("Debio lanzar ProductoNoEncontradoException");
        } catch (ProductoNoEncontradoException esperada) {
            // correcto
        }
    }

    @Test
    public void buscarDevuelveElProductoAgregado() throws Exception {
        Producto p = inv.buscar("Teclado");
        assertEquals("Teclado", p.getNombre());
        assertEquals(350.0, p.getPrecio(), 0.0);
        assertEquals(1, inv.listar().size());
        assertEquals("Teclado", inv.listar().get(0).getNombre());
    }
}
