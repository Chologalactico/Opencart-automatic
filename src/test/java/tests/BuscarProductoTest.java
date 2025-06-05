package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BusquedaPage;
import pages.HomePage;
import utils.Constants;
import utils.ExcelUtils;

import java.util.List;

public class BuscarProductoTest extends BaseTest{

    @Test
    public void buscarProductos() {
        List<String[]> productos = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, "Productos");
        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        for (String[] producto : productos) {
            homePage.buscarProducto(producto[0]);
            if (busquedaPage.buscarYAgregarProducto(producto[0])) {
                System.out.println("Producto agregado al carrito: " + producto[0]);
            } else {
                Assert.fail("El producto no se encontró: " + producto[0]);
            }
        }
    }
}