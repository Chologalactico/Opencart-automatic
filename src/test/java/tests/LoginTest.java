package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.LoginPage;
import utils.ExcelUtils;

import java.util.List;

public class LoginTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opencart.abstracta.us/");
    }

    @Test
    public void loginUsuarios() {
        List<String[]> usuarios = ExcelUtils.leerExcel("src/main/resources/" +
                "inputData.xlsx", "LoginData");
        LoginPage loginPage = new LoginPage(driver);

        for (String[] usuario : usuarios) {
            loginPage.irALogin();
            loginPage.iniciarSesion(usuario[0], usuario[1]);

            if (usuario[2].equalsIgnoreCase("valido")) {
                Assert.assertTrue(loginPage.loginExitoso(), "El login válido falló para: " + usuario[0]);
            } else {
                Assert.assertTrue(loginPage.loginFallido(), "El login inválido no mostró advertencia para: " + usuario[0]);
            }

            driver.get("https://opencart.abstracta.us/");  // Reinicio simple entre iteraciones
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
