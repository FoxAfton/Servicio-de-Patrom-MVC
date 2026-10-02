package vista;

import modelo.Inventario;
import modelo.ObservadorInventario;

import java.io.IOException;

/** Vista pasiva: cada vez que el inventario cambia, reescribe el CSV. */
public class ObservadorCsv implements ObservadorInventario {
    private final String archivo;

    public ObservadorCsv(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public void inventarioCambio(Inventario inventario) {
        try {
            ExportadorCsv.exportar(archivo, inventario.listar());
        } catch (IOException e) {
            System.out.println("[CSV] No se pudo exportar: " + e.getMessage());
        }
    }
}
