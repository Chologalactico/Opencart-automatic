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
    public void verificarProductosEnCarritoYGuardarResultados() {
        // Leer productos desde Excel
        List<String[]> productosExcel = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, Constants.SHEET_PRODUCTOS_CARRITO);

        // Inicializar páginas
        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        // Lista para almacenar resultados
        List<String[]> resultados = new ArrayList<>();

        // Agregar encabezados al archivo de resultados
        String[] encabezados = {"Producto", "Estado", "Precio Unitario", "Precio Total", "Cantidad"};
        resultados.add(encabezados);

        // Navegar a la página principal
        homePage.navigateTo(Constants.BASE_URL);

        // Paso 1: Agregar productos al carrito uno por uno
        for (String[] producto : productosExcel) {
            String nombreProducto = producto[0];
            System.out.println("Procesando producto: " + nombreProducto);

            // Buscar el producto
            homePage.buscarProducto(nombreProducto);

            // Intentar agregarlo al carrito
            if (busquedaPage.buscarYAgregarProducto(nombreProducto)) {
                System.out.println("✅ Producto agregado al carrito: " + nombreProducto);
            } else {
                System.out.println("❌ No se pudo agregar el producto: " + nombreProducto);
            }
        }

        // Paso 2: Ir al carrito para verificar productos
        carritoPage.irAlCarrito();

        // Verificar que se llegó correctamente al carrito
        Assert.assertFalse(carritoPage.carritoVacio(), "El carrito no debería estar vacío después de agregar productos");

        // Obtener productos del carrito
        List<CarritoPage.ProductoCarrito> productosEnCarrito = carritoPage.obtenerProductosEnCarrito();

        System.out.println("📊 Total de productos en carrito: " + productosEnCarrito.size());
        System.out.println("💰 Total del carrito: " + carritoPage.obtenerTotalCarrito());

        // Paso 3: Verificar cada producto del Excel contra el carrito
        for (String[] productoExcel : productosExcel) {
            String nombreProductoBuscado = productoExcel[0];
            boolean encontrado = false;
            String[] resultado = new String[5];
            resultado[0] = nombreProductoBuscado;

            // Buscar el producto en la lista del carrito
            for (CarritoPage.ProductoCarrito productoCarrito : productosEnCarrito) {
                if (productoCarrito.getNombre().toLowerCase().contains(nombreProductoBuscado.toLowerCase())) {
                    encontrado = true;
                    resultado[1] = "AGREGADO EXITOSAMENTE";
                    resultado[2] = productoCarrito.getPrecioUnitario();
                    resultado[3] = productoCarrito.getPrecioTotal();
                    resultado[4] = String.valueOf(productoCarrito.getCantidad());

                    System.out.println("✅ Verificado en carrito: " + nombreProductoBuscado);
                    break;
                }
            }

            if (!encontrado) {
                resultado[1] = "NO AGREGADO";
                resultado[2] = "N/A";
                resultado[3] = "N/A";
                resultado[4] = "0";
                System.out.println("❌ No encontrado en carrito: " + nombreProductoBuscado);
            }

            resultados.add(resultado);
        }

        // Paso 4: Guardar resultados en Excel
        ExcelUtils.escribirExcel(Constants.FILE_PATH_OUTPUT_EXCEL, "ResultadosCarrito", resultados);
        System.out.println("📄 Resultados guardados en: " + Constants.FILE_PATH_OUTPUT_EXCEL);

        // Paso 5: Validaciones finales
        int productosAgregados = 0;
        for (String[] resultado : resultados) {
            if (resultado.length > 1 && "AGREGADO EXITOSAMENTE".equals(resultado[1])) {
                productosAgregados++;
            }
        }

        System.out.println("📈 Resumen de ejecución:");
        System.out.println("   - Productos procesados: " + productosExcel.size());
        System.out.println("   - Productos agregados exitosamente: " + productosAgregados);
        System.out.println("   - Productos no agregados: " + (productosExcel.size() - productosAgregados));

        // Assertion para verificar que al menos un producto fue agregado
        Assert.assertTrue(productosAgregados > 0,
                "Al menos un producto debería haber sido agregado al carrito exitosamente");
    }

    @Test
    public void verificarTotalesDelCarrito() {
        // Este test verifica los cálculos del carrito después de agregar productos
        List<String[]> productos = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, Constants.SHEET_PRODUCTOS_CARRITO);

        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        // Agregar algunos productos al carrito
        int productosAgregados = 0;
        for (String[] producto : productos) {
            homePage.buscarProducto(producto[0]);
            if (busquedaPage.buscarYAgregarProducto(producto[0])) {
                productosAgregados++;
                // Limitar a 3 productos para esta prueba
                if (productosAgregados >= 3) break;
            }
        }

        // Ir al carrito
        carritoPage.irAlCarrito();

        // Verificaciones
        Assert.assertFalse(carritoPage.carritoVacio(), "El carrito no debería estar vacío");
        Assert.assertTrue(carritoPage.contarProductosEnCarrito() > 0, "Debería haber productos en el carrito");

        String subtotal = carritoPage.obtenerSubtotal();
        String total = carritoPage.obtenerTotalCarrito();

        Assert.assertFalse(subtotal.isEmpty(), "El subtotal no debería estar vacío");
        Assert.assertFalse(total.isEmpty(), "El total no debería estar vacío");

        System.out.println("💰 Subtotal: " + subtotal);
        System.out.println("💵 Total: " + total);
        System.out.println("📦 Productos en carrito: " + carritoPage.contarProductosEnCarrito());
    }

    @Test
    public void verificarProductoEspecificoEnCarrito() {
        // Test para verificar un producto específico
        HomePage homePage = new HomePage(driver);
        BusquedaPage busquedaPage = new BusquedaPage(driver);
        CarritoPage carritoPage = new CarritoPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        // Producto específico para probar (puedes cambiarlo según tu Excel)
        String productoTest = "iPhone";

        // Buscar y agregar el producto
        homePage.buscarProducto(productoTest);
        boolean agregado = busquedaPage.buscarYAgregarProducto(productoTest);

        if (agregado) {
            // Ir al carrito y verificar
            carritoPage.irAlCarrito();

            boolean encontradoEnCarrito = carritoPage.verificarProductoEnCarrito(productoTest);
            Assert.assertTrue(encontradoEnCarrito,
                    "El producto " + productoTest + " debería estar en el carrito");

            System.out.println("✅ Producto " + productoTest + " verificado exitosamente en el carrito");
        } else {
            System.out.println("⚠️ No se pudo agregar el producto " + productoTest + " para la verificación");
        }
    }
}