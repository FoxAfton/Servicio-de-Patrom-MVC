package persistencia;

import modelo.ProductoDAO;

public class ProductoDAOMemoriaTest extends ProductoDAOContratoTest {
    @Override
    protected ProductoDAO crear() {
        return new ProductoDAOMemoria();
    }
}
