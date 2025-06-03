package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class RegistroPage {
    WebDriver driver;

    public RegistroPage(WebDriver driver) {
        this.driver = driver;
    }

    public void abrirFormularioRegistro() {
        WaitUtils.esperarElementoClickable(driver, By.linkText("My Account"), 10).click();
        WaitUtils.esperarElementoClickable(driver, By.linkText("Register"), 10).click();
    }

    public void completarFormulario(String[] datos) {
        WaitUtils.esperarElementoVisible(driver, By.id("input-firstname"), 10).sendKeys(datos[0]);
        driver.findElement(By.id("input-lastname")).sendKeys(datos[1]);
        driver.findElement(By.id("input-email")).sendKeys(datos[2]);
        driver.findElement(By.id("input-telephone")).sendKeys(datos[3]);
        driver.findElement(By.id("input-password")).sendKeys(datos[4]);
        driver.findElement(By.id("input-confirm")).sendKeys(datos[4]);
        driver.findElement(By.name("agree")).click();
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }

    public String obtenerMensajeRegistro() {
        return WaitUtils.esperarElementoVisible(driver, By.tagName("h1"), 10).getText();
    }

    public void continuarDespuesRegistro() {
        driver.findElement(By.linkText("Continue")).click();
    }
}
