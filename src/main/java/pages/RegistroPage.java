package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

import java.util.ArrayList;
import java.util.List;

public class RegistroPage extends BasePage{

    public RegistroPage(WebDriver driver) {
        super(driver);
    }

    // Selectores
    private By inputFirstname = By.id("input-firstname");
    private By inputLastname = By.id("input-lastname");
    private By inputEmail = By.id("input-email");
    private By inputTelephone = By.id("input-telephone");
    private By inputPassword = By.id("input-password");
    private By inputConfirm = By.id("input-confirm");
    private By checkboxAgree = By.name("agree");
    private By submitButton = By.cssSelector("input[type='submit']");

    private By mensajesError = By.cssSelector("div.text-danger");
    private By alertaEmailRegistrado = By.cssSelector("div.alert.alert-danger.alert-dismissible");

    public void completarFormulario(String[] datos) {
        String nombre = datos[0];
        String apellido = datos[1];
        String email = limpiarDato(datos[2]);
        String telefono = limpiarDato(datos[3]);
        String password = limpiarDato(datos[4]);

        WaitUtils.esperarElementoVisible(driver, inputFirstname, 10).sendKeys(nombre);
        driver.findElement(inputLastname).sendKeys(apellido);
        driver.findElement(inputEmail).sendKeys(email);
        driver.findElement(inputTelephone).sendKeys(telefono);
        driver.findElement(inputPassword).sendKeys(password);
        driver.findElement(inputConfirm).sendKeys(password);
        driver.findElement(checkboxAgree).click();
        driver.findElement(submitButton).click();
    }

    public List<String> obtenerMensajesError() {
        List<WebElement> errores = driver.findElements(mensajesError);
        List<String> mensajes = new ArrayList<>();
        for (WebElement error : errores) {
            mensajes.add(error.getText());
        }
        return mensajes;
    }

    public boolean esEmailYaRegistrado() {
        try {
            return driver.findElement(alertaEmailRegistrado).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private String limpiarDato(String dato) {
        if (dato == null) return "";
        return dato.endsWith(".0") ? dato.replace(".0", "") : dato.trim();
    }

}
