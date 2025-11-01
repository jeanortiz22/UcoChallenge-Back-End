package co.edu.uco.parametersservice.catalog;

import java.util.ArrayList;
import java.util.List;

final class ParameterCatalogDefaults {

    private ParameterCatalogDefaults() {
    }

    static List<Parameter> defaults() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter("FechaDefectoMaxima", "31/12/9999"));
        parameters.add(new Parameter("CorreoAdministrador", "admin@uco.edu.co"));
        parameters.add(new Parameter("NumeroMaximoReintentosEnvioCorreo", "8"));
        parameters.add(new Parameter("USER.REGISTER.DUPLICATE.ADMIN_EMAIL", "alertas.registro@uco.edu.co"));
        parameters.add(new Parameter("USER.REGISTER.CONFIRMATION.TTL_MINUTES", "30"));
        parameters.add(new Parameter("USER.REGISTER.MOBILE_CONFIRMATION_TTL_MINUTES", "15"));
        return parameters;
    }
}