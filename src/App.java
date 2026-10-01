import controlador.ControladorInventario;
import modelo.Inventario;
import vista.VistaConsola;

/** Ensambla las tres piezas y arranca. */
public class App {
    public static void main(String[] args) {
        Inventario modelo = new Inventario();
        VistaConsola vista = new VistaConsola();
        new ControladorInventario(modelo, vista).iniciar();
    }
}
