import controlador.ControladorInventario;
import modelo.Inventario;
import vista.Vista;
import vista.VistaConsola;
import vista.VistaCsv;

/** Ensambla las tres piezas y arranca. Uso: java App [csv] */
public class App {
    public static void main(String[] args) {
        Inventario modelo = new Inventario();
        Vista vista = (args.length > 0 && args[0].equals("csv"))
                ? new VistaCsv("inventario.csv")
                : new VistaConsola();
        new ControladorInventario(modelo, vista).iniciar();
    }
}
