package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.function.Function;

public class WaitUtils {

    public static WebElement esperarElementoVisible(WebDriver driver, By locator, int tiempo) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(tiempo));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement esperarElementoClickable(WebDriver driver, By locator, int tiempo) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(tiempo));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement esperarConFluentWait(WebDriver driver, By locator, int tiempoTotal, int pollingMs) {
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(tiempoTotal))
                .pollingEvery(Duration.ofMillis(pollingMs))
                .ignoring(NoSuchElementException.class);

        return wait.until(drv -> drv.findElement(locator));
    }
}
