package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.RegistroPage;
import utils.ExcelUtils;

import java.util.List;

public class RegistroTest {

    private WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opencart.abstracta.us/");
    }

    @Test
    public void registrarUsuarios() {
        List<String[]> usuarios = ExcelUtils.leerExcel("src/main/resources/inputData.xlsx", "UsuariosRegistro");
        RegistroPage registroPage = new RegistroPage(driver);

        for (String[] usuario : usuarios) {
            registroPage.abrirFormularioRegistro();
            registroPage.completarFormulario(usuario);
            String mensaje = registroPage.obtenerMensajeRegistro();
            Assert.assertEquals(mensaje, "Your Account Has Been Created!");
            registroPage.continuarDespuesRegistro();
        }
    }

    @AfterMethod
    public void tearDown() {
        driver.quit();
    }
}
