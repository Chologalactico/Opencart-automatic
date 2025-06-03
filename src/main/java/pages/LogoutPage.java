package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class LogoutPage extends BasePage {

    private By botonContinuar = By.cssSelector("a.btn.btn-primary");

    public LogoutPage(WebDriver driver) {
        super(driver);
    }

    public void continuarDespuesLogout() {
        WaitUtils.esperarElementoClickable(driver, botonContinuar, 10).click();
    }
}
