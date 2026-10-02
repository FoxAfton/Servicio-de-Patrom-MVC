package vista;

import modelo.Inventario;
import modelo.ObservadorInventario;
import modelo.Producto;

/** Vista pasiva de consola: avisa en una linea que el inventario cambio. */
public class ObservadorConsola implements ObservadorInventario {
    @Override
    public void inventarioCambio(Inventario inventario) {
        StringBuilder sb = new StringBuilder("\n[aviso] Inventario actualizado:");
        if (inventario.listar().isEmpty()) {
            sb.append(" (vacio)");
        }
        for (Producto p : inventario.listar()) {
            sb.append(" ").append(p.getNombre()).append("=").append(p.getCantidad());
        }
        System.out.println(sb);
    }
}
