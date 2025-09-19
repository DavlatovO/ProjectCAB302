package com.example.projectcab302;

import com.example.projectcab302.Controller.LoginController;
import com.example.projectcab302.Controller.RegisterController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login and Register feature.
 *
 */
class LoginAndRegisterTest {

    // Helper to call RegisterController's private isValidEmail(String)
    private static boolean callIsValidEmail(RegisterController rc, String email) throws Exception {
        Method m = RegisterController.class.getDeclaredMethod("isValidEmail", String.class);
        m.setAccessible(true);
        return (boolean) m.invoke(rc, email);
    }

    // ---------- 1–2) Register: email validation (valid vs invalid) ----------

    @Test
    @DisplayName("Register: isValidEmail accepts common valid emails")
    void register_isValidEmail_valids() throws Exception {
        RegisterController rc = new RegisterController();
        for (String e : List.of(
                "alice@example.com",
                "bob.smith@sub.domain.co",
                "x_y-z+1@my-domain.io",
                "A.B-C+D@EXAMPLE.EDU"
        )) {
            assertTrue(callIsValidEmail(rc, e), "Expected VALID: " + e);
        }
    }

    @Test
    @DisplayName("Register: isValidEmail rejects clearly invalid emails")
    void register_isValidEmail_invalids() throws Exception {
        RegisterController rc = new RegisterController();
        for (String e : List.of(
                "", "plainaddress", "noatsign.com", "@no-local-part.com",
                "name@no-tld", "name@domain..double-dot.com",
                "bad char@domain.com", "name@.leadingdot.com"
        )) {
            assertFalse(callIsValidEmail(rc, e), "Expected INVALID: " + e);
        }
    }

    // ---------- 3) Additional email edge case: null ----------

    @Test
    @DisplayName("Register: isValidEmail(null) returns false")
    void register_isValidEmail_nullIsInvalid() throws Exception {
        RegisterController rc = new RegisterController();
        assertFalse(callIsValidEmail(rc, null));
    }

    // ---------- 4) Additional email edge case: mixed case + plus tag ----------

    @Test
    @DisplayName("Register: mixed case + plus tag email is valid")
    void register_isValidEmail_mixedCaseAndPlus() throws Exception {
        RegisterController rc = new RegisterController();
        assertTrue(callIsValidEmail(rc, "Mixed.Case+tag@Example.CoM"));
    }

    // ---------- 5–6) Minimal nav “shape” checks: method presence ----------

    @Test
    @DisplayName("LoginController exposes onLoginAsTeacher() and onLoginAsStudent()")
    void loginController_hasHandlers() throws Exception {
        Method teacher = LoginController.class.getDeclaredMethod("onLoginAsTeacher");
        Method student = LoginController.class.getDeclaredMethod("onLoginAsStudent");
        assertNotNull(teacher);
        assertNotNull(student);
        assertEquals(0, teacher.getParameterCount());
        assertEquals(0, student.getParameterCount());
    }

    @Test
    @DisplayName("RegisterController exposes switchToLogin()")
    void registerController_hasSwitchToLogin() throws Exception {
        Method m = RegisterController.class.getDeclaredMethod("switchToLogin");
        assertNotNull(m);
        assertEquals(0, m.getParameterCount());
    }

    // ---------- 7–8) Constructors exist (no-arg) ----------

    @Test
    @DisplayName("LoginController has a no-arg constructor")
    void loginController_defaultConstructorExists() {
        assertNotNull(new LoginController());
    }

    @Test
    @DisplayName("RegisterController has a no-arg constructor")
    void registerController_defaultConstructorExists() {
        assertNotNull(new RegisterController());
    }

    // ---------- 9–10) Handler signatures: public void, no params ----------

    @Test
    @DisplayName("LoginController handlers are public void with no params")
    void loginController_handlerSignatures() throws Exception {
        Method teacher = LoginController.class.getDeclaredMethod("onLoginAsTeacher");
        Method student = LoginController.class.getDeclaredMethod("onLoginAsStudent");

        assertTrue(Modifier.isPublic(teacher.getModifiers()));
        assertTrue(Modifier.isPublic(student.getModifiers()));
        assertEquals(void.class, teacher.getReturnType());
        assertEquals(void.class, student.getReturnType());
        assertEquals(0, teacher.getParameterCount());
        assertEquals(0, student.getParameterCount());
    }

    @Test
    @DisplayName("RegisterController switchToLogin is public void with no params")
    void registerController_switchToLogin_signature() throws Exception {
        Method m = RegisterController.class.getDeclaredMethod("switchToLogin");
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertEquals(void.class, m.getReturnType());
        assertEquals(0, m.getParameterCount());
    }
}
