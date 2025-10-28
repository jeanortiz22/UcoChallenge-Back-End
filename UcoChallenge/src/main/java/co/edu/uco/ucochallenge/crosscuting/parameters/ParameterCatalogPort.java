package co.edu.uco.ucochallenge.crosscuting.parameters;

import java.util.Locale;

public interface ParameterCatalogPort {
    String get(String code, Object... args);
    String get(String code, Locale locale, Object... args);
}
