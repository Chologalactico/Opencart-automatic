package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class CarritoPage extends BasePage {

    public CarritoPage(WebDriver driver) {
        super(driver);
    }

    // Selectores para la página del carrito
    private By tablaProductos = By.cssSelector(".table.table-bordered tbody");
    private By filasProductos = By.cssSelector(".table.table-bordered tbody tr");
    private By botonViewCart = By.cssSelector("#cart-total");
    private By enlaceCarrito = By.cssSelector("#cart > button");
    private By opcionViewCart = By.xpath("//a[contains(@href, 'checkout/cart')]");

    // Selectores para información de productos
    private By nombreProducto = By.cssSelector("td.text-left a[href*='product_id']");
    private By modeloProducto = By.cssSelector("td.text-left:nth-child(3)");
    private By cantidadProducto = By.cssSelector("input[name*='quantity']");
    private By precioUnitario = By.cssSelector("td.text-right:nth-child(5)");
    private By precioTotal = By.cssSelector("td.text-right:nth-child(6)");

    // Selectores para totales
    private By totalCarrito = By.cssSelector(".col-sm-4.col-sm-offset-8 table tr:last-child td.text-right:last-child");
    private By subtotal = By.cssSelector(".col-sm-4.col-sm-offset-8 table tr:nth-child(1) td.text-right:last-child");

    /**
     * Navega al carrito haciendo clic en el botón del carrito
     */
    public void irAlCarrito() {
        try {
            // Hacer clic en el botón del carrito
            WaitUtils.esperarElementoClickable(driver, enlaceCarrito, 10).click();

            // Hacer clic en "View Cart"
            WaitUtils.esperarElementoClickable(driver, opcionViewCart, 10).click();

            // Esperar a que cargue la página del carrito
            WaitUtils.esperarElementoVisible(driver, tablaProductos, 10);
        } catch (Exception e) {
            System.err.println("Error al navegar al carrito: " + e.getMessage());
        }
    }

    /**
     * Obtiene la lista de todos los productos en el carrito
     * @return Lista de ProductoCarrito con la información de cada producto
     */
    public List<ProductoCarrito> obtenerProductosEnCarrito() {
        List<ProductoCarrito> productos = new ArrayList<>();

        try {
            List<WebElement> filas = driver.findElements(filasProductos);

            for (WebElement fila : filas) {
                try {
                    ProductoCarrito producto = new ProductoCarrito();

                    // Obtener nombre del producto
                    WebElement elementoNombre = fila.findElement(nombreProducto);
                    producto.setNombre(elementoNombre.getText().trim());

                    // Obtener modelo
                    WebElement elementoModelo = fila.findElement(modeloProducto);
                    producto.setModelo(elementoModelo.getText().trim());

                    // Obtener cantidad
                    WebElement elementoCantidad = fila.findElement(cantidadProducto);
                    producto.setCantidad(Integer.parseInt(elementoCantidad.getAttribute("value")));

                    // Obtener precio unitario
                    WebElement elementoPrecioUnitario = fila.findElement(precioUnitario);
                    producto.setPrecioUnitario(elementoPrecioUnitario.getText().trim());

                    // Obtener precio total
                    WebElement elementoPrecioTotal = fila.findElement(precioTotal);
                    producto.setPrecioTotal(elementoPrecioTotal.getText().trim());

                    productos.add(producto);

                } catch (NoSuchElementException e) {
                    System.err.println("Error al obtener información de un producto: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener productos del carrito: " + e.getMessage());
        }

        return productos;
    }

    /**
     * Verifica si un producto específico está en el carrito
     * @param nombreProducto Nombre del producto a buscar
     * @return true si el producto está en el carrito, false en caso contrario
     */
    public boolean verificarProductoEnCarrito(String nombreProducto) {
        List<ProductoCarrito> productos = obtenerProductosEnCarrito();

        for (ProductoCarrito producto : productos) {
            if (producto.getNombre().toLowerCase().contains(nombreProducto.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el total del carrito
     * @return String con el total del carrito
     */
    public String obtenerTotalCarrito() {
        try {
            WebElement elementoTotal = WaitUtils.esperarElementoVisible(driver, totalCarrito, 10);
            return elementoTotal.getText().trim();
        } catch (Exception e) {
            System.err.println("Error al obtener total del carrito: " + e.getMessage());
            return "";
        }
    }

    /**
     * Obtiene el subtotal del carrito
     * @return String con el subtotal del carrito
     */
    public String obtenerSubtotal() {
        try {
            WebElement elementoSubtotal = WaitUtils.esperarElementoVisible(driver, subtotal, 10);
            return elementoSubtotal.getText().trim();
        } catch (Exception e) {
            System.err.println("Error al obtener subtotal del carrito: " + e.getMessage());
            return "";
        }
    }

    /**
     * Verifica si el carrito está vacío
     * @return true si el carrito está vacío, false en caso contrario
     */
    public boolean carritoVacio() {
        try {
            List<WebElement> filas = driver.findElements(filasProductos);
            return filas.isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Cuenta el número total de productos en el carrito
     * @return Número de productos en el carrito
     */
    public int contarProductosEnCarrito() {
        try {
            List<WebElement> filas = driver.findElements(filasProductos);
            return filas.size();
        } catch (Exception e) {
            System.err.println("Error al contar productos: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Clase interna para representar un producto en el carrito
     */
    public static class ProductoCarrito {
        private String nombre;
        private String modelo;
        private int cantidad;
        private String precioUnitario;
        private String precioTotal;

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getModelo() { return modelo; }
        public void setModelo(String modelo) { this.modelo = modelo; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public String getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(String precioUnitario) { this.precioUnitario = precioUnitario; }

        public String getPrecioTotal() { return precioTotal; }
        public void setPrecioTotal(String precioTotal) { this.precioTotal = precioTotal; }

        @Override
        public String toString() {
            return "ProductoCarrito{" +
                    "nombre='" + nombre + '\'' +
                    ", modelo='" + modelo + '\'' +
                    ", cantidad=" + cantidad +
                    ", precioUnitario='" + precioUnitario + '\'' +
                    ", precioTotal='" + precioTotal + '\'' +
                    '}';
        }
    }
}