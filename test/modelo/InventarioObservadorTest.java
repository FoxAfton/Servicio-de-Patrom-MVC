package modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

/** El modelo notifica a sus observadores sin saber quienes son (aqui, un contador de prueba). */
public class InventarioObservadorTest {

    /** Observador falso: solo cuenta avisos y guarda la ultima fuente. */
    private static class Contador implements ObservadorInventario {
        int avisos = 0;
        Inventario ultimaFuente;
        public void inventarioCambio(Inventario inventario) {
            avisos++;
            ultimaFuente = inventario;
        }
    }

    private Inventario inv;
    private Contador a;
    private Contador b;

    @Before
    public void preparar() throws Exception {
        inv = new Inventario();
        inv.agregar("Teclado", 5, 350.0);   // antes de registrar observadores
        a = new Contador();
        b = new Contador();
        inv.agregarObservador(a);
        inv.agregarObservador(b);
    }

    @Test
    public void agregarNotificaATodosLosObservadores() throws Exception {
        inv.agregar("Mouse", 3, 100.0);
        assertEquals(1, a.avisos);
        assertEquals(1, b.avisos);
        assertSame(inv, a.ultimaFuente);
    }

    @Test
    public void venderNotificaATodosLosObservadores() throws Exception {
        inv.vender("Teclado", 2);
        assertEquals(1, a.avisos);
        assertEquals(1, b.avisos);
    }

    @Test
    public void elObservadorYaVeElEstadoNuevo() throws Exception {
        final int[] vistoPorElObservador = new int[1];
        inv.agregarObservador(new ObservadorInventario() {
            public void inventarioCambio(Inventario i) {
                try {
                    vistoPorElObservador[0] = i.buscar("Teclado").getCantidad();
                } catch (ProductoNoEncontradoException e) {
                    vistoPorElObservador[0] = -1;
                }
            }
        });
        inv.vender("Teclado", 2);
        assertEquals(3, vistoPorElObservador[0]);
    }

    @Test
    public void unaVentaRechazadaNoNotifica() throws Exception {
        try {
            inv.vender("Teclado", 99);
        } catch (StockInsuficienteException esperada) {
            // correcto
        }
        assertEquals(0, a.avisos);
        assertEquals(0, b.avisos);
    }

    @Test
    public void unProductoInvalidoNoNotifica() {
        try {
            inv.agregar("Mouse", 3, 0.0);
        } catch (DatosInvalidosException esperada) {
            // correcto
        }
        assertEquals(0, a.avisos);
    }

    @Test
    public void unObservadorRetiradoYaNoRecibeAvisos() throws Exception {
        inv.quitarObservador(b);
        inv.vender("Teclado", 1);
        assertEquals(1, a.avisos);
        assertEquals(0, b.avisos);
    }

    @Test
    public void sinObservadoresTodoSigueFuncionando() throws Exception {
        Inventario solo = new Inventario();
        solo.agregar("X", 1, 1.0);
        assertEquals(0, solo.vender("X", 1));
    }
}
