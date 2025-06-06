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

        StringBuilder errores = new StringBuilder();

        for (String[] usuario : usuarios) {
            try {
                // Validar datos básicos antes de continuar
                if (!registroPage.datosValidos(usuario)) {
                    String mensaje = "Datos inválidos o incompletos para el usuario: " + registroPage.mostrarUsuario(usuario);
                    System.out.println(mensaje);
                    errores.append(mensaje).append("\n");
                    continue;
                }

                homePage.selectAccount();
                homePage.selectRegister();

                registroPage.completarFormulario(usuario);

                // Validar si el email ya está registrado
                if (registroPage.esEmailYaRegistrado()) {
                    String mensaje = "Email ya registrado: " + usuario[2];
                    System.out.println(mensaje);
                    errores.append(mensaje).append("\n");
                    continue;
                }

                // Validar errores de formulario
                List<String> erroresFormulario = registroPage.obtenerMensajesError();
                if (!erroresFormulario.isEmpty()) {
                    String mensaje = "Errores en formulario para: " + usuario[2] + "\n";
                    for (String error : erroresFormulario) {
                        mensaje += "   - " + error + "\n";
                    }
                    System.out.println(mensaje);
                    errores.append(mensaje).append("\n");
                    continue;
                }

                // Validar mensaje de éxito
                String mensajeExito = confirmacionRegistroPage.obtenerMensajeRegistro();
                if (!mensajeExito.contains("Congratulations")) {
                    String mensaje = "Registro fallido para: " + usuario[2] + " - Mensaje: " + mensajeExito;
                    System.out.println(mensaje);
                    errores.append(mensaje).append("\n");
                    continue;
                }

                System.out.println("Usuario registrado con éxito: " + usuario[2]);

                confirmacionRegistroPage.continuarDespuesRegistro();
                homePage.selectAccount();
                homePage.selectLogout();
                logoutPage.continuarDespuesLogout();

            } catch (Exception e) {
                String mensaje = "Excepción inesperada con el usuario " + registroPage.mostrarUsuario(usuario) + ": " + e.getMessage();
                System.err.println(mensaje);
                errores.append(mensaje).append("\n");
            }
        }

        if (errores.length() > 0) {
            Assert.fail("Se encontraron errores durante el registro:\n" + errores);
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
