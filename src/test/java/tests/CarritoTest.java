package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BusquedaPage;
import pages.CarritoPage;
import pages.HomePage;
import utils.Constants;
import utils.ExcelUtils;

import java.util.List;

public class CarritoTest extends BaseTest {

    @Test
    public void flujoCompletoCarrito() {
        // 1. LEER PRODUCTOS DESDE EXCEL
        List<String[]> productos = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, Constants.SHEET_PRODUCTOS);

        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        // 2. AGREGAR TODOS LOS PRODUCTOS
        System.out.println("=== PASO 1: AGREGANDO PRODUCTOS ===");
        for (String[] producto : productos) {
            String nombreProducto = producto[0];
            try {
                System.out.println("Agregando: " + nombreProducto);

                homePage.buscarProducto(nombreProducto);
                busquedaPage.buscarYAgregarProducto(nombreProducto);
            } catch (Exception e) {
                System.err.println("Error al procesar producto '" + nombreProducto + "': " + e.getMessage());
                System.err.println("Continuando con el siguiente producto...\n");
            }
        }


        // 3. IR AL CARRITO
        System.out.println("\n=== PASO 2: NAVEGANDO AL CARRITO ===");
        carritoPage.irAlCarrito();

        // 4. VALIDAR PRODUCTOS
        System.out.println("\n=== PASO 3: VALIDANDO PRODUCTOS ===");
        boolean validacion = carritoPage.validarProductosEnCarrito(productos);
        Assert.assertTrue(validacion, "Los productos no se validaron correctamente en el carrito");

        // 5. OBTENER TODOS LOS DATOS Y GUARDAR EN EXCEL
        System.out.println("\n=== PASO 4: OBTENIENDO DATOS COMPLETOS ===");
        List<String[]> datosCompletos = carritoPage.obtenerTodosLosDatosDelCarrito();

        Assert.assertFalse(datosCompletos.isEmpty(), "No se obtuvieron datos del carrito");

        // 6. GUARDAR EN EXCEL
        System.out.println("\n=== PASO 5: GUARDANDO EN EXCEL ===");
        ExcelUtils.escribirExcel(Constants.FILE_PATH_OUTPUT_EXCEL, Constants.SHEET_PRODUCTOS_CARRITO, datosCompletos);

        System.out.println("PROCESO COMPLETADO EXITOSAMENTE");
        System.out.println("Archivo guardado: " + Constants.FILE_PATH_OUTPUT_EXCEL);
        System.out.println("Productos procesados: " + (datosCompletos.size() - 1)); // -1 por encabezados
    }
}