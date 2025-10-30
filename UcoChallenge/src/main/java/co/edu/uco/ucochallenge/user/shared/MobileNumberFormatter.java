package co.edu.uco.ucochallenge.user.shared;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.parameters.ParameterCatalogPort;

/**
 * Utility component that normalizes mobile numbers into the E.164 format expected by
 * NotificationAPI. If the incoming number does not include a country code, the default country
 * code is retrieved from the parameter catalog (falling back to Colombia's +57).
 */
@Component
public class MobileNumberFormatter {

    static final String DEFAULT_COUNTRY_CODE_PARAMETER = "USER.CONTACT.DEFAULT_COUNTRY_CODE";
    static final String FALLBACK_COUNTRY_CODE = "+57";

    private static final Logger log = LoggerFactory.getLogger(MobileNumberFormatter.class);

    private final ParameterCatalogPort parameterCatalog;

    public MobileNumberFormatter(final ParameterCatalogPort parameterCatalog) {
        this.parameterCatalog = parameterCatalog;
    }

    public String format(final String mobileNumber) {
        var sanitized = TextHelper.getDefaultWithTrim(mobileNumber);
        if (TextHelper.isEmpty(sanitized)) {
            return sanitized;
        }

        sanitized = sanitized.replaceAll("[\\s()-]", "");

        if (sanitized.startsWith("+")) {
            return normalizeWithPlusPrefix(sanitized);
        }

        if (sanitized.startsWith("00")) {
            sanitized = sanitized.substring(2);
        }

        sanitized = sanitized.replaceAll("[^0-9]", "");
        if (TextHelper.isEmpty(sanitized)) {
            return TextHelper.getDefault();
        }

        final var countryCode = resolveDefaultCountryCode();
        return countryCode + sanitized;
    }

    private String normalizeWithPlusPrefix(final String mobileNumber) {
        final var digitsOnly = mobileNumber.substring(1).replaceAll("[^0-9]", "");
        if (TextHelper.isEmpty(digitsOnly)) {
            return TextHelper.getDefault();
        }
        return "+" + digitsOnly;
    }

    private String resolveDefaultCountryCode() {
        try {
            var value = parameterCatalog.get(DEFAULT_COUNTRY_CODE_PARAMETER, Locale.getDefault());
            value = TextHelper.getDefaultWithTrim(value);
            if (TextHelper.isEmpty(value) || value.startsWith("[")) {
                return FALLBACK_COUNTRY_CODE;
            }

            value = value.replaceAll("[^0-9+]", "");
            if (TextHelper.isEmpty(value)) {
                return FALLBACK_COUNTRY_CODE;
            }

            if (!value.startsWith("+")) {
                value = "+" + value;
            }

            return value;
        } catch (Exception ex) {
            log.warn("No fue posible obtener el indicativo país por defecto: {}", ex.getMessage());
            return FALLBACK_COUNTRY_CODE;
        }
    }
}