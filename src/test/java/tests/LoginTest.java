package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import pages.HomePage;
import pages.LoginPage;
import utils.Constants;
import utils.ExcelUtils;

import java.util.List;

public class LoginTest extends BaseTest{

    @Test
    public void loginUsuarios() {
        List<String[]> usuarios = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, "LoginData");
        HomePage homePage = new HomePage(driver);
        LoginPage loginPage = new LoginPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        for (String[] usuario : usuarios) {
            homePage.selectAccount();
            homePage.selectLogin();
            String password = limpiarPassword(usuario[1]);
            loginPage.iniciarSesion(usuario[0], password);
            validarLogin(loginPage, usuario, password);
            System.out.println("Login correcto del usuario: " + usuario[0]);
            homePage.selectAccount();
            homePage.selectLogout();
        }
    }

    private void validarLogin(LoginPage loginPage, String[] usuario, String password) {
        if (!loginPage.validarLogin()) {
            System.out.println("Usuario o contraseña incorrecta: " + usuario[0] + " / " + password);
            Assert.fail("No se pudo iniciar sesión con las credenciales: " + usuario[0]);
        }
    }

    private String limpiarPassword(String input) {
        if (input.matches("\\d+\\.0")) {
            return input.substring(0, input.indexOf('.'));
        }
        return input;
    }

}
