package co.edu.uco.ucochallenge.user.registeruser.application.parameters;

import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserParametersProvider {

    private final ParameterCatalogPort catalog;

    public RegisterUserParametersProvider(ParameterCatalogPort catalog) {
        this.catalog = catalog;
    }

    public RegisterUserParameters snapshot() {

        int idMin = num(RegisterUserParameterCode.ID_NUMBER_MIN, 5, 1, 200);
        int idMax = num(RegisterUserParameterCode.ID_NUMBER_MAX, 20, 1, 200);

        int fnMin = num(RegisterUserParameterCode.FIRST_NAME_MIN, 1, 1, 200);
        int fnMax = num(RegisterUserParameterCode.FIRST_NAME_MAX, 50, 2, 200);

        int snMin = num(RegisterUserParameterCode.SECOND_NAME_MIN, 0, 0, 200);
        int snMax = num(RegisterUserParameterCode.SECOND_NAME_MAX, 50, 0, 200);

        int fsMin = num(RegisterUserParameterCode.FIRST_SURNAME_MIN, 1, 1, 200);
        int fsMax = num(RegisterUserParameterCode.FIRST_SURNAME_MAX, 50, 2, 200);

        int ssMin = num(RegisterUserParameterCode.SECOND_SURNAME_MIN, 0, 0, 200);
        int ssMax = num(RegisterUserParameterCode.SECOND_SURNAME_MAX, 50, 0, 200);

        int emailMin = num(RegisterUserParameterCode.EMAIL_MIN, 5, 1, 300);
        int emailMax = num(RegisterUserParameterCode.EMAIL_MAX, 120, 5, 300);
        String emailRx = str(RegisterUserParameterCode.EMAIL_PATTERN, "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

        int mobileMin = num(RegisterUserParameterCode.MOBILE_MIN, 7, 0, 50);
        int mobileMax = num(RegisterUserParameterCode.MOBILE_MAX, 15, 0, 50);
        String mobileRx = str(RegisterUserParameterCode.MOBILE_PATTERN, "^[0-9]+$");

        String nameRx = str(RegisterUserParameterCode.NAME_PATTERN, "^[A-Za-zÁÉÍÓÚÑáéíóúñ\\s'-]+$");

        int emailTtl = num(RegisterUserParameterCode.EMAIL_TOKEN_TTL_MIN, 30, 1, 1440);
        int smsTtl = num(RegisterUserParameterCode.SMS_TOKEN_TTL_MIN, 10, 1, 1440);
        int retries = num(RegisterUserParameterCode.MAX_RETRIES, 5, 0, 50);
        boolean self = bool(RegisterUserParameterCode.FEATURE_SELF_REGISTER_ENABLED, true);

        return new RegisterUserParameters(
                idMin, idMax,
                fnMin, fnMax,
                snMin, snMax,
                fsMin, fsMax,
                ssMin, ssMax,
                emailMin, emailMax, emailRx,
                mobileMin, mobileMax, mobileRx,
                nameRx,
                emailTtl, smsTtl, retries, self
        );
    }

    private String str(String key, String def) {
        String v = catalog.get(key);
        return (v == null || v.isBlank()) ? def : v;
    }

    private int num(String key, int def, int min, int max) {
        try {
            int v = Integer.parseInt(str(key, String.valueOf(def)));
            return (v < min || v > max) ? def : v;
        } catch (Exception e) {
            return def;
        }
    }

    private boolean bool(String key, boolean def) {
        String v = str(key, String.valueOf(def));
        return v.equalsIgnoreCase("true") || v.equals("1") || v.equalsIgnoreCase("yes");
    }
}
