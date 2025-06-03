package tests;

import org.testng.Assert;
import org.testng.annotations.*;

import pages.ConfirmacionRegistroPage;
import pages.HomePage;
import pages.LogoutPage;
import pages.RegistroPage;
import utils.Constants;
import utils.ExcelUtils;

import java.util.List;

public class RegistroTest extends BaseTest {

    @Test
    public void registrarUsuarios() {
        List<String[]> usuarios = ExcelUtils.leerExcel(Constants.FILE_PATH_INPUT_EXCEL, "UsuariosRegistro");
        HomePage homePage = new HomePage(driver);
        RegistroPage registroPage = new RegistroPage(driver);
        ConfirmacionRegistroPage confirmacionRegistroPage = new ConfirmacionRegistroPage(driver);
        LogoutPage logoutPage = new LogoutPage(driver);

        homePage.navigateTo(Constants.BASE_URL);

        for (String[] usuario : usuarios) {
            homePage.selectAccount();
            homePage.selectRegister();

            registroPage.completarFormulario(usuario);

            verificarEmailDuplicado(registroPage, usuario);
            verificarErroresFormulario(registroPage);
            verificarRegistroExitoso(confirmacionRegistroPage, usuario);

            confirmacionRegistroPage.continuarDespuesRegistro();
            homePage.selectAccount();
            homePage.selectLogout();
            logoutPage.continuarDespuesLogout();
        }
    }

    private void verificarEmailDuplicado(RegistroPage registroPage, String[] usuario) {
        if (registroPage.esEmailYaRegistrado()) {
            System.out.println("Email duplicado: " + usuario[2]);
            Assert.fail("El correo ya estaba registrado");
        }
    }

    private void verificarErroresFormulario(RegistroPage registroPage) {
        List<String> errores = registroPage.obtenerMensajesError();
        if (!errores.isEmpty()) {
            errores.forEach(error -> System.out.println("Error de validación: " + error));
            Assert.fail("Se encontraron errores de validación en el formulario.");
        }
    }

    private void verificarRegistroExitoso(ConfirmacionRegistroPage confirmacionRegistroPage, String[] usuario) {
        String mensaje = confirmacionRegistroPage.obtenerMensajeRegistro();
        Assert.assertTrue(mensaje.contains("Congratulations"),
                "El mensaje de éxito no es el esperado para el usuario " + usuario[2]);
        System.out.println("Usuario " + usuario[2] + " creado");
    }
}
