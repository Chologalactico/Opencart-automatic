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
    private By botonCarrito = By.cssSelector("a[href*='checkout/cart']");
    private By tablaProductos = By.cssSelector(".table.table-bordered tbody");
    private By filasProductos = By.cssSelector(".table.table-bordered tbody tr");

    // Selectores para información de productos
    private By nombreProducto = By.cssSelector("td.text-left a");
    private By modeloProducto = By.cssSelector("td.text-left:nth-child(3)");
    private By cantidadProducto = By.cssSelector("input[name*='quantity']");
    private By precioUnitario = By.cssSelector("td.text-right:nth-child(5)");
    private By precioTotal = By.cssSelector("td.text-right:nth-child(6)");

    public void irAlCarrito() {
        WaitUtils.esperarElementoClickable(driver, botonCarrito, 10).click();
        // Esperar a que cargue la tabla de productos
        WaitUtils.esperarElementoVisible(driver, tablaProductos, 10);
    }

    public List<String[]> obtenerProductosDelCarrito() {
        List<String[]> productosEnCarrito = new ArrayList<>();

        try {
            List<WebElement> filas = driver.findElements(filasProductos);

            for (WebElement fila : filas) {
                String[] producto = new String[5];

                // Nombre del producto
                WebElement elementoNombre = fila.findElement(nombreProducto);
                producto[0] = elementoNombre.getText().trim();

                // Modelo del producto
                WebElement elementoModelo = fila.findElement(modeloProducto);
                producto[1] = elementoModelo.getText().trim();

                // Cantidad
                WebElement elementoCantidad = fila.findElement(cantidadProducto);
                producto[2] = elementoCantidad.getAttribute("value");

                // Precio unitario
                WebElement elementoPrecioUnitario = fila.findElement(precioUnitario);
                producto[3] = elementoPrecioUnitario.getText().trim();

                // Precio total
                WebElement elementoPrecioTotal = fila.findElement(precioTotal);
                producto[4] = elementoPrecioTotal.getText().trim();

                productosEnCarrito.add(producto);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener productos del carrito: " + e.getMessage());
        }

        return productosEnCarrito;
    }

    public boolean verificarProductoEnCarrito(String nombreProductoEsperado) {
        try {
            List<WebElement> filas = driver.findElements(filasProductos);

            for (WebElement fila : filas) {
                WebElement elementoNombre = fila.findElement(nombreProducto);
                String nombreEnCarrito = elementoNombre.getText().trim();

                if (nombreEnCarrito.toLowerCase().contains(nombreProductoEsperado.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar producto en carrito: " + e.getMessage());
        }

        return false;
    }

    public int contarProductosEnCarrito() {
        try {
            List<WebElement> filas = driver.findElements(filasProductos);
            return filas.size();
        } catch (Exception e) {
            System.err.println("Error al contar productos: " + e.getMessage());
            return 0;
        }
    }
}