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

        StringBuilder errores = new StringBuilder();

        for (String[] producto : productos) {
            homePage.buscarProducto(producto[0]);
            boolean exito = busquedaPage.buscarYAgregarProducto(producto[0]);

            if (exito) {
                System.out.println("Producto agregado al carrito: " + producto[0]);
            } else {
                errores.append("El producto no se encontró o falló al agregar: ").append(producto[0]).append("\n");
            }
        }

        if (errores.length() > 0) {
            Assert.fail("Se encontraron errores:\n" + errores);
        }
    }

}