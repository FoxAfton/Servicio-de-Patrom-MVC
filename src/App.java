import controlador.ControladorInventario;
import modelo.Inventario;
import persistencia.ProductoDAOArchivo;
import persistencia.ProductoDAOMemoria;
import vista.ObservadorConsola;
import vista.ObservadorCsv;
import vista.Vista;
import vista.VistaConsola;
import vista.VistaCsv;
import vista.VistaGrafica;

import javax.swing.SwingUtilities;

/**
 * Ensambla las piezas y arranca.
 *   java App        -> consola
 *   java App csv    -> consola con "Listar" exportando a CSV
 *   java App sync   -> consola + ventana grafica + CSV espejo, todas sobre el mismo modelo
 */
public class App {
    public static void main(String[] args) {
        String modo = args.length > 0 ? args[0] : "";
        // >>> UNICA LINEA A CAMBIAR PARA ALTERNAR ENTRE MEMORIA Y ARCHIVO <<<
        //Inventario modelo = new Inventario(new ProductoDAOMemoria());
         Inventario modelo = new Inventario(new ProductoDAOArchivo("inventario.dat"));

        if (modo.equals("sync")) {
            Vista consola = new VistaConsola();
            ControladorInventario controlador = new ControladorInventario(modelo, consola);

            VistaGrafica grafica = new VistaGrafica(controlador, modelo);
            modelo.agregarObservador(grafica);
            modelo.agregarObservador(new ObservadorConsola());
            modelo.agregarObservador(new ObservadorCsv("inventario.csv"));
            SwingUtilities.invokeLater(grafica::mostrar);

            controlador.iniciar();
            System.exit(0);   // cierra tambien la ventana
        } else {
            Vista vista = modo.equals("csv") ? new VistaCsv("inventario.csv") : new VistaConsola();
            new ControladorInventario(modelo, vista).iniciar();
        }
    }
}
