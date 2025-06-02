package tests;


import utils.ExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
//IMPORT para google Chorome si en tal caso queremos cambiarlo pues modificamos esta
import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class RegistroTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        // Ruta local del driver para Microsoft Edge
        //driver = new EdgeDriver();
        //Ruta local del driver para Google
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opencart.abstracta.us/");
    }
    //Ultima prueba de gitHub

    @Test
    public void registrarUsuarios() {
        List<String[]> usuarios = ExcelUtils.leerExcel("src/main/resources/inputData.xlsx", "UsuariosRegistro");

        for (String[] usuario : usuarios) {
            driver.findElement(By.linkText("My Account")).click();
            driver.findElement(By.linkText("Register")).click();

            driver.findElement(By.id("input-firstname")).sendKeys(usuario[0]);
            driver.findElement(By.id("input-lastname")).sendKeys(usuario[1]);
            driver.findElement(By.id("input-email")).sendKeys(usuario[2]);
            driver.findElement(By.id("input-telephone")).sendKeys(usuario[3]);
            driver.findElement(By.id("input-password")).sendKeys(usuario[4]);
            driver.findElement(By.id("input-confirm")).sendKeys(usuario[4]);
            driver.findElement(By.name("agree")).click();
            driver.findElement(By.cssSelector("input[type='submit']")).click();

            String mensaje = driver.findElement(By.tagName("h1")).getText();
            Assert.assertEquals(mensaje, "Your Account Has Been Created!");

            driver.findElement(By.linkText("Continue")).click();
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
