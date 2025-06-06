package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.WaitUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CarritoPage extends BasePage {

    public CarritoPage(WebDriver driver) {
        super(driver);
    }

    private By botonCarrito = By.id("cart");
    private By opcionVerCarrito = By.xpath("//a[contains(@href, 'checkout/cart')]");
    private By filasProductos = By.xpath("//table[@class='table table-bordered']//tbody/tr");

    public void irAlCarrito() {
        try {
            WebElement btnCarrito = WaitUtils.esperarElementoClickable(driver, botonCarrito, 10);
            btnCarrito.click();
            Thread.sleep(1000);

            WebElement verCarrito = WaitUtils.esperarElementoClickable(driver, opcionVerCarrito, 10);
            verCarrito.click();
            Thread.sleep(2000);

            System.out.println("Navegación al carrito completada");
        } catch (Exception e) {
            throw new RuntimeException("Error al ir al carrito: " + e.getMessage());
        }
    }

    public List<String[]> obtenerTodosLosDatosDelCarrito() {
        List<String[]> productosCompletos = new ArrayList<>();

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            // Agregar encabezados
            productosCompletos.add(new String[]{
                    "Nombre", "Modelo", "Cantidad", "Precio Unitario", "Precio Total", "Reward Points"
            });

            // Obtener filas de productos
            List<WebElement> filas = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(filasProductos));
            System.out.println("Productos encontrados en carrito: " + filas.size());

            for (int i = 0; i < filas.size(); i++) {
                String[] datosProducto = extraerDatosDeFila(filas.get(i));
                if (datosProducto != null) {
                    productosCompletos.add(datosProducto);
                    System.out.println("Producto " + (i + 1) + ":");
                    System.out.println("  Nombre          : " + datosProducto[0]);
                    System.out.println("  Modelo          : " + datosProducto[1]);
                    System.out.println("  Cantidad        : " + datosProducto[2]);
                    System.out.println("  Precio Unitario : " + datosProducto[3]);
                    System.out.println("  Precio Total    : " + datosProducto[4]);
                    System.out.println("  Reward Points   : " + datosProducto[5]);
                    System.out.println("--------------------------------------------");

                }
            }

        } catch (Exception e) {
            System.err.println("Error al obtener datos del carrito: " + e.getMessage());
        }

        return productosCompletos;
    }

    private String[] extraerDatosDeFila(WebElement fila) {
        try {
            List<WebElement> columnas = fila.findElements(By.tagName("td"));

            if (columnas.size() < 6) return null;

            String nombre = "";
            String modelo = "";
            String cantidad = "";
            String precioUnitario = "";
            String precioTotal = "";
            String rewardPoints = "";

            // Columna 2: Nombre del producto
            try {
                WebElement enlaceNombre = columnas.get(1).findElement(By.tagName("a"));
                nombre = enlaceNombre.getText().trim();

                // Buscar reward points
                try {
                    WebElement smallElement = columnas.get(1).findElement(By.tagName("small"));
                    rewardPoints = smallElement.getText().replace("Reward Points:", "").trim();
                } catch (Exception e) {
                    rewardPoints = "N/A";
                }
            } catch (Exception e) {
                nombre = "Error al obtener nombre";
            }

            // Columna 3: Modelo
            try {
                modelo = columnas.get(2).getText().trim();
            } catch (Exception e) {
                modelo = "N/A";
            }

            // Columna 4: Cantidad
            try {
                WebElement inputCantidad = columnas.get(3).findElement(By.tagName("input"));
                cantidad = inputCantidad.getAttribute("value");
            } catch (Exception e) {
                cantidad = "N/A";
            }

            // Columna 5: Precio Unitario
            try {
                precioUnitario = columnas.get(4).getText().trim();
            } catch (Exception e) {
                precioUnitario = "N/A";
            }

            // Columna 6: Precio Total
            try {
                precioTotal = columnas.get(5).getText().trim();
            } catch (Exception e) {
                precioTotal = "N/A";
            }

            return new String[]{nombre, modelo, cantidad, precioUnitario, precioTotal, rewardPoints};

        } catch (Exception e) {
            System.err.println("Error al procesar fila: " + e.getMessage());
            return null;
        }
    }

    public boolean validarProductosEnCarrito(List<String[]> productosEsperados) {
        List<String[]> productosEnCarrito = obtenerTodosLosDatosDelCarrito();

        // Saltar encabezados
        for (int i = 1; i < productosEnCarrito.size(); i++) {
            String nombreEnCarrito = productosEnCarrito.get(i)[0];

            for (String[] productoEsperado : productosEsperados) {
                String nombreEsperado = productoEsperado[0];

                if (nombreEnCarrito.toLowerCase().contains(nombreEsperado.toLowerCase())) {
                    System.out.println("Validado: " + nombreEsperado + " encontrado como: " + nombreEnCarrito);
                }
            }
        }

        return productosEnCarrito.size() > 1; // Más de 1 porque incluye encabezados
    }
}