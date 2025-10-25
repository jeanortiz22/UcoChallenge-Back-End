package co.edu.uco.ucochallenge.crosscuting.messages;

import java.text.MessageFormat;

public interface MessageCatalogPort {
    String getTemplate(String key);

    default String format(String key, Object... params) {
        var tpl = getTemplate(key);
        try { return MessageFormat.format(tpl, params); }
        catch (Exception e) { return tpl; }
    }
}
