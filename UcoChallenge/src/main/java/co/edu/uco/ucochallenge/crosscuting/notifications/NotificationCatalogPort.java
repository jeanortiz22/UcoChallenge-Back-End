package co.edu.uco.ucochallenge.crosscuting.notifications;

public interface NotificationCatalogPort {

    /**
     * Envía una solicitud de notificación utilizando el catálogo externo.
     *
     * @param command datos normalizados de la notificación a enviar.
     * @return identificador o respuesta textual entregada por NotificationAPI.
     */
    String send(NotificationCommand command);
}