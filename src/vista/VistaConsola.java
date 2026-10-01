package vista;

import modelo.Producto;

import java.util.List;
import java.util.Scanner;

/** Todo lo que el usuario ve o teclea. No decide nada ni conoce las reglas del negocio. */
public class VistaConsola implements Vista {
    private final Scanner sc = new Scanner(System.in);

    // ---- Entrada: devuelve texto crudo, el controlador lo interpreta ----
    public String pedirOpcion() {
        System.out.println("\n1) Agregar 2) Vender 3) Listar 4) Valor total 0) Salir");
        return pedir("Opcion: ");
    }

    public String pedir(String etiqueta) {
        System.out.print(etiqueta);
        return sc.nextLine();
    }

    // ---- Salida ----
    public void mostrarDatosInvalidos()     { System.out.println("Datos invalidos"); }
    public void mostrarAgregado()           { System.out.println("Agregado."); }
    public void mostrarNoExiste()           { System.out.println("No existe"); }
    public void mostrarStockInsuficiente()  { System.out.println("Stock insuficiente"); }
    public void mostrarVentaOk(int quedan)  { System.out.println("Venta ok. Quedan " + quedan); }

    public void mostrarListado(List<Producto> productos) {
        for (Producto p : productos) {
            System.out.printf("%-15s %4d $%.2f%n", p.getNombre(), p.getCantidad(), p.getPrecio());
        }
    }

    public void mostrarValorTotal(double total) {
        System.out.println("Valor del inventario: $" + total);
    }
}
