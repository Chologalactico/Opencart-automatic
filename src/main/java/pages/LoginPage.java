package pages;

import org.openqa.selenium.*;
import utils.WaitUtils;

public class LoginPage extends BasePage{

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    private By inputEmail = By.id("input-email");
    private By inputPassword = By.id("input-password");
    private By inputSubmit = By.cssSelector("input[type='submit']");

    private By mensajeErrorLogin = By.cssSelector(".alert.alert-danger");

    public void iniciarSesion(String email, String password) {
        WaitUtils.esperarElementoVisible(driver, inputEmail, 10).sendKeys(email);
        driver.findElement(inputPassword).sendKeys(password);
        driver.findElement(inputSubmit).click();
    }

    public boolean validarLogin() {
        try {
            // Si encontramos el mensaje de error, es porque el login falló
            WaitUtils.esperarConFluentWait(driver, mensajeErrorLogin, 3, 300);
            return false; // Lo encontramos → login fallido
        } catch (TimeoutException | NoSuchElementException e) {
            return true; // No se encontró el mensaje → login exitoso
        }
    }
}
