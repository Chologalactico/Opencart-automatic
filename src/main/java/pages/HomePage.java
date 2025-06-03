package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.WaitUtils;

public class HomePage extends BasePage {

    //Constructor
    public HomePage(WebDriver driver) {
        super(driver);
    }

    //Categorias
    private By category(String category) {
        return By.xpath("//a[text()='" + category + "']");
    }

    private By subcategory(String subcategory) {
        return By.xpath("//div[@class='dropdown-inner']//a[contains(text(), ''" + subcategory + "')]'");
    }

    // Cuenta
    private By myAccountLink = By.xpath("//*[@id='top-links']//a[@title='My Account']");
    private By registerLInk = By.xpath("//*[@id='top-links']//a[text()='Register']");
    private By loginLink = By.xpath("//*[@id='top-links']//a[text()='Login']");
    private By logoutLink = By.xpath("//*[@id='top-links']//a[text()='Logout']");

    //Busqueda
    private By inputBusqueda = By.cssSelector("#search input[name='search']");
    private By botonBuscar = By.cssSelector("#search button");


    //Metodos o acciones de la pagina
    public void selectCategory(String category) {
        driver.findElement(category(category)).click();
    }

    public void selectSubCategory(String subcategory) {
        driver.findElement(subcategory(subcategory)).click();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void selectAccount() {
        WaitUtils.esperarElementoClickable(driver, myAccountLink, 10).click();
    }

    public boolean isLoggedIn() {
        try {
            return driver.findElement(logoutLink).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


    public void selectRegister() {
        if (isLoggedIn()) {
            throw new IllegalStateException("El usuario ya está logueado. No se puede registrar.");
        }
        WaitUtils.esperarElementoClickable(driver, registerLInk, 10).click();
    }

    public void selectLogin() {
        if (isLoggedIn()) {
            throw new IllegalStateException("El usuario ya está logueado. No se puede hacer login.");
        }
        WaitUtils.esperarElementoClickable(driver, loginLink, 10).click();
    }

    public void selectLogout() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("El usuario no está logueado. No se puede hacer logout.");
        }
        WaitUtils.esperarElementoClickable(driver, logoutLink, 10).click();
    }

    public void buscarProducto(String producto) {
        WebElement campoBusqueda = WaitUtils.esperarElementoClickable(driver, inputBusqueda, 10);
        campoBusqueda.clear();
        campoBusqueda.sendKeys(producto);
        driver.findElement(botonBuscar).click();
    }
}

