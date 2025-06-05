package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class CarritoPage extends BasePage {

    public CarritoPage(WebDriver driver) {
        super(driver);
    }

    // Selectores
    private By botonCarrito = By.id("cart");
    private By botonVerCarrito = By.xpath("//a[contains(@href, 'checkout/cart')]");
    private By tablaProductos = By.cssSelector(".table.table-bordered tbody");
    private By filasProductos = By.cssSelector(".table.table-bordered tbody tr");
    private By nombreProducto = By.cssSelector("td.text-left a");

    public void irAlCarrito() {
        WaitUtils.esperarElementoClickable(driver, botonCarrito, 10).click();
        WaitUtils.esperarElementoClickable(driver, botonVerCarrito, 10).click();
    }

    public List<String> obtenerProductosEnCarrito() {
        List<String> productosEnCarrito = new ArrayList<>();

        // Esperar a que la tabla de productos cargue
        WaitUtils.esperarElementoVisible(driver, tablaProductos, 10);

        // Obtener todas las filas de productos
        List<WebElement> filas = driver.findElements(filasProductos);

        for (WebElement fila : filas) {
            try {
                WebElement enlaceProducto = fila.findElement(nombreProducto);
                String nombreProductoTexto = enlaceProducto.getText().trim();

                // Limpiar el texto del producto (quitar puntos de recompensa y otros elementos)
                nombreProductoTexto = limpiarNombreProducto(nombreProductoTexto);

                if (!nombreProductoTexto.isEmpty()) {
                    productosEnCarrito.add(nombreProductoTexto);
                    System.out.println("Producto encontrado en carrito: " + nombreProductoTexto);
                }
            } catch (Exception e) {
                System.err.println("Error al obtener producto de una fila: " + e.getMessage());
            }
        }

        return productosEnCarrito;
    }

    public boolean validarProductoEnCarrito(String nombreProducto) {
        List<String> productosEnCarrito = obtenerProductosEnCarrito();

        for (String producto : productosEnCarrito) {
            if (producto.toLowerCase().contains(nombreProducto.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private String limpiarNombreProducto(String textoCompleto) {
        // Eliminar texto de puntos de recompensa y otros elementos extra
        if (textoCompleto.contains("Puntos de recompensa")) {
            // Extraer solo el nombre del producto del enlace
            String[] partes = textoCompleto.split("Puntos de recompensa");
            if (partes.length > 1) {
                String parteProducto = partes[1].trim();
                if (parteProducto.startsWith("de ")) {
                    parteProducto = parteProducto.substring(3).trim();
                }
                return parteProducto;
            }
        }
        return textoCompleto.trim();
    }
}