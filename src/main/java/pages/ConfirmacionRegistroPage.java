package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class ConfirmacionRegistroPage extends BasePage{

    private By mensajeRegistroExitoso = By.xpath("//div[@id='content']/p[contains(text(), 'Congratulations')]");
    private By botonContinuar = By.cssSelector("a.btn.btn-primary");

    public ConfirmacionRegistroPage(WebDriver driver) {
        super(driver);
    }

    public String obtenerMensajeRegistro() {
        return WaitUtils.esperarElementoVisible(driver, mensajeRegistroExitoso, 10).getText();
    }

    public void continuarDespuesRegistro() {
        WaitUtils.esperarElementoClickable(driver, botonContinuar, 10).click();
    }
}
