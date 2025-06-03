package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class WaitUtils {

    public static WebElement esperarElemento(WebDriver driver, By locator, int tiempoSegundos) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(tiempoSegundos));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
