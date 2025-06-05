package tests;

import org.testng.Assert;
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
    public void verificarProductosEnCarrito() {
        // Leer productos desde Excel
        List<String[]> productosEsperados = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, Constants.SHEET_PRODUCTOS);

        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Agregar productos al carrito
        System.out.println("=== AGREGANDO PRODUCTOS AL CARRITO ===");
        for (String[] producto : productosEsperados) {
            homePage.buscarProducto(producto[0]);
            if (busquedaPage.buscarYAgregarProducto(producto[0])) {
                System.out.println("✅ Producto agregado: " + producto[0]);
            } else {
                System.out.println("❌ No se pudo agregar: " + producto[0]);
            }
        }

        // Ir al carrito
        System.out.println("\n=== VERIFICANDO PRODUCTOS EN CARRITO ===");
        carritoPage.irAlCarrito();

        // Verificar cada producto esperado
        List<String> productosEncontrados = new ArrayList<>();
        List<String> productosFaltantes = new ArrayList<>();

        for (String[] producto : productosEsperados) {
            if (carritoPage.verificarProductoEnCarrito(producto[0])) {
                productosEncontrados.add(producto[0]);
                System.out.println("✅ Producto verificado en carrito: " + producto[0]);
            } else {
                productosFaltantes.add(producto[0]);
                System.out.println("❌ Producto NO encontrado en carrito: " + producto[0]);
            }
        }

        // Obtener todos los productos del carrito
        List<String[]> productosEnCarrito = carritoPage.obtenerProductosDelCarrito();

        // Mostrar resumen
        System.out.println("\n=== RESUMEN ===");
        System.out.println("Total productos esperados: " + productosEsperados.size());
        System.out.println("Total productos encontrados: " + productosEncontrados.size());
        System.out.println("Total productos en carrito: " + productosEnCarrito.size());

        // Escribir resultados en Excel
        escribirResultadosEnExcel(productosEnCarrito);

        // Validación final
        Assert.assertTrue(productosFaltantes.isEmpty(),
                "Los siguientes productos no se encontraron en el carrito: " + productosFaltantes);

        Assert.assertTrue(productosEnCarrito.size() > 0,
                "El carrito está vacío");

        System.out.println("✅ Verificación de carrito completada exitosamente");
    }

    private void escribirResultadosEnExcel(List<String[]> productosEnCarrito) {
        try {
            // Crear encabezados para el Excel de salida
            List<String[]> datosParaExcel = new ArrayList<>();
            String[] encabezados = {"Nombre", "Modelo", "Cantidad", "Precio Unitario", "Precio Total"};
            datosParaExcel.add(encabezados);

            // Agregar productos del carrito
            datosParaExcel.addAll(productosEnCarrito);

            // Escribir en Excel
            ExcelUtils.escribirExcel(Constants.FILE_PATH_OUTPUT_EXCEL, Constants.SHEET_PRODUCTOS_CARRITO, datosParaExcel);

            System.out.println("📊 Resultados escritos en: " + Constants.FILE_PATH_OUTPUT_EXCEL);
            System.out.println("📋 Hoja: " + Constants.SHEET_PRODUCTOS_CARRITO);

        } catch (Exception e) {
            System.err.println("Error al escribir en Excel: " + e.getMessage());
        }
    }
}