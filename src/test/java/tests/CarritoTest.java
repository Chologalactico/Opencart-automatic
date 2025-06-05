package tests;

import org.testng.annotations.Test;
import pages.BusquedaPage;
import pages.CarritoPage;
import pages.HomePage;
import utils.Constants;
import utils.ExcelUtils;

import java.util.ArrayList;
import java.util.List;

public class CarritoTest extends BaseTest {

    @Test
    public void validarProductosEnCarritoYGuardarEnExcel() {
        // Leer productos desde Excel
        List<String[]> productos = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, Constants.SHEET_PRODUCTOS);

        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        // Navegar al sitio
        homePage.navigateTo(Constants.BASE_URL);

        // Buscar y agregar productos al carrito
        for (String[] producto : productos) {
            String nombreProducto = producto[0];
            System.out.println("Procesando producto: " + nombreProducto);

            homePage.buscarProducto(nombreProducto);

            if (busquedaPage.buscarYAgregarProducto(nombreProducto)) {
                System.out.println("Producto agregado al carrito: " + nombreProducto);
            } else {
                System.err.println("No se pudo agregar el producto: " + nombreProducto);
            }
        }

        // Ir al carrito para validar productos
        carritoPage.irAlCarrito();

        // Obtener productos que están efectivamente en el carrito
        List<String> productosEnCarrito = carritoPage.obtenerProductosEnCarrito();

        // Validar que se encontraron productos
        if (productosEnCarrito.isEmpty()) {
            System.err.println("No se encontraron productos en el carrito");
            return;
        }

        // Preparar datos para escribir en Excel
        List<String[]> datosParaExcel = new ArrayList<>();

        // Agregar encabezado
        datosParaExcel.add(new String[]{"Producto Agregado al Carrito"});

        // Agregar productos encontrados
        for (String producto : productosEnCarrito) {
            datosParaExcel.add(new String[]{producto});
            System.out.println("Producto validado en carrito: " + producto);
        }

        // Escribir resultados en Excel
        ExcelUtils.escribirExcel(Constants.FILE_PATH_OUTPUT_EXCEL, Constants.SHEET_PRODUCTOS_CARRITO, datosParaExcel);

        System.out.println("Proceso completado. Productos guardados en: " + Constants.FILE_PATH_OUTPUT_EXCEL);
        System.out.println("Total de productos en carrito: " + productosEnCarrito.size());
    }
}