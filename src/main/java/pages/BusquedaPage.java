package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.List;

public class BusquedaPage extends BasePage {

    public BusquedaPage(WebDriver driver) {
        super(driver);
    }

    // Selector base: todos los productos visibles
    private By productos = By.cssSelector(".product-thumb");

    // Selector relativo dentro de un producto
    private By tituloProducto = By.cssSelector(".caption a");
    private By botonAgregarAlCarrito = By.xpath(".//button[contains(@onclick, 'cart.add')]");

    // Selector para el mensaje de éxito
    private By mensajeExito = By.cssSelector(".alert.alert-success");

    public boolean buscarYAgregarProducto(String nombreProducto) {
        try {
            List<WebElement> listaProductos = driver.findElements(productos);
            if (listaProductos.isEmpty()) {
                System.err.println("No se encontraron productos en la página.");
                return false;
            }

            for (WebElement producto : listaProductos) {
                String titulo = producto.findElement(tituloProducto).getText().trim();
                if (titulo.toLowerCase().contains(nombreProducto.toLowerCase())) {
                    producto.findElement(botonAgregarAlCarrito).click();

                    // Validar mensaje
                    return validarProductoAgregado(nombreProducto);
                }
            }

            System.err.println("Producto no encontrado: " + nombreProducto + ". Verifica que el nombre sea correcto.");
            return false;

        } catch (Exception e) {
            System.err.println("Ocurrió un error al buscar/agregar el producto: " + nombreProducto);
            System.err.println("Detalles: " + e.getMessage());
            return false;
        }
    }

    private boolean validarProductoAgregado(String nombreProducto) {
        try {
            WebElement mensaje = WaitUtils.esperarElementoVisible(driver, mensajeExito, 10);

            String textoMensaje = mensaje.getText();

            // Validar que el mensaje contiene "Success" y el nombre del producto
            return textoMensaje.contains("Success") &&
                    textoMensaje.contains("You have added") &&
                    textoMensaje.toLowerCase().contains(nombreProducto.toLowerCase());

        } catch (TimeoutException e) {
            System.err.println("No apareció el mensaje de confirmación para: " + nombreProducto);
            return false;
        } catch (Exception e) {
            System.err.println("Error al validar producto agregado: " + e.getMessage());
            return false;
        }
    }
}

