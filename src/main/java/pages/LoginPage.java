package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WaitUtils;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void irALogin() {
        driver.findElement(By.linkText("My Account")).click();
        driver.findElement(By.linkText("Login")).click();
    }

    public void iniciarSesion(String email, String password) {
        WaitUtils.esperarElementoVisible(driver, By.id("input-email"), 10).sendKeys(email);
        driver.findElement(By.id("input-password")).sendKeys(password);
        driver.findElement(By.cssSelector("input[type='submit']")).click();
    }

    public boolean loginExitoso() {
        return driver.getPageSource().contains("My Account");
    }

    public boolean loginFallido() {
        return driver.getPageSource().contains("Warning: No match for E-Mail Address and/or Password.");
    }
}
