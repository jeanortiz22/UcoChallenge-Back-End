package co.edu.uco.ucochallenge.infrastructure.secondary.adapters.notifications;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.notificationapi.NotificationApi;
import com.notificationapi.model.NotificationRequest;

import co.edu.uco.ucochallenge.crosscuting.exception.UcoChallengeTechnicalException;
import co.edu.uco.ucochallenge.infrastructure.secondary.ports.repository.notifications.NotificationCommand;

@ExtendWith(MockitoExtension.class)
class NotificationApiClientTest {

    @Mock
    private NotificationApi notificationApi;

    private NotificationApiClient client;

    @BeforeEach
    void setUp() {
        client = new NotificationApiClient(notificationApi);
    }

    @Test
    void send_whenCommandIsNull_shouldThrowTechnicalException() {
        final var exception = assertThrows(UcoChallengeTechnicalException.class, () -> client.send(null));

        assertEquals("NOTIFICACION_COMANDO_NULO", exception.getMessageCode());
    }

    @Test
    void send_whenTemplateIsMissing_shouldRejectCommand() {
        final var command = new NotificationCommand(
                "   ",
                "user@example.com",
                "subject",
                "<p>Body</p>",
                "3000000000",
                "sms");

        final var exception = assertThrows(UcoChallengeTechnicalException.class, () -> client.send(command));

        assertEquals("NOTIFICACION_PLANTILLA_OBLIGATORIA", exception.getMessageCode());
    }

    @Test
    void send_whenNoDestinationProvided_shouldRejectCommand() {
        final var command = new NotificationCommand(
                "welcome",
                " ",
                null,
                null,
                " ",
                null);

        final var exception = assertThrows(UcoChallengeTechnicalException.class, () -> client.send(command));

        assertEquals("NOTIFICACION_DESTINATARIO_REQUERIDO", exception.getMessageCode());
    }

    @Test
    void send_whenApiReturnsError_shouldWrapAsTechnicalException() {
        final var command = new NotificationCommand(
                "welcome",
                "user@example.com",
                "Hello",
                "<p>Hello</p>",
                "3001234567",
                "SMS message");

        when(notificationApi.send(any(NotificationRequest.class))).thenThrow(new RuntimeException("boom"));

        final var exception = assertThrows(UcoChallengeTechnicalException.class, () -> client.send(command));

        assertEquals("ERROR_ENVIANDO_NOTIFICACION", exception.getMessageCode());
        assertNotNull(exception.getCause());
    }

    @Test
    void send_whenCommandIsValid_shouldInvokeNotificationApiWithNormalizedPayload() {
        final var command = new NotificationCommand(
                "welcome",
                "USER@example.com",
                "Welcome subject",
                "<p>Hello</p>",
                "3001234567",
                "SMS message");

        when(notificationApi.send(any(NotificationRequest.class))).thenReturn("queued-123");

        final String response = client.send(command);

        assertEquals("queued-123", response);

        final ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(notificationApi).send(requestCaptor.capture());

        final NotificationRequest request = requestCaptor.getValue();
        assertNotNull(request);

        final Object user = readRequiredProperty(request, List.of("getUser"), List.of("user"));
        assertEquals(command.email(), String.valueOf(readRequiredProperty(user, List.of("getEmail"), List.of("email"))));
        assertEquals(command.mobileNumber(),
                String.valueOf(readOptionalProperty(user, List.of("getNumber", "getPhone", "getMobile"), List.of("number", "phone", "mobileNumber"))));

        final Object emailOptions = readOptionalProperty(request, List.of("getEmail"), List.of("email", "emailOptions"));
        if (emailOptions != null) {
            assertEquals(command.subject(), String.valueOf(readOptionalProperty(emailOptions, List.of("getSubject"), List.of("subject"))));
            assertEquals(command.htmlBody(), String.valueOf(readOptionalProperty(emailOptions, List.of("getHtml", "getHtmlBody"), List.of("html", "htmlBody"))));
        }

        final Object smsOptions = readOptionalProperty(request, List.of("getSms"), List.of("sms", "smsOptions"));
        if (smsOptions != null) {
            assertEquals(command.smsMessage(), String.valueOf(readOptionalProperty(smsOptions, List.of("getMessage"), List.of("message"))));
        }
    }

    private Object readRequiredProperty(final Object target, final List<String> getterCandidates, final List<String> fieldCandidates) {
        return tryReadProperty(target, getterCandidates, fieldCandidates)
                .orElseThrow(() -> new AssertionError("Property not accessible on " + target.getClass()));
    }

    private Optional<Object> tryReadProperty(final Object target,
                                             final List<String> getterCandidates,
                                             final List<String> fieldCandidates) {
        for (String getter : getterCandidates) {
            try {
                final Method method = target.getClass().getMethod(getter);
                return Optional.ofNullable(method.invoke(target));
            } catch (NoSuchMethodException ignored) {
                // continue searching
            } catch (ReflectiveOperationException ex) {
                throw new AssertionError("Unable to invoke method " + getter, ex);
            }
        }

        for (String fieldName : fieldCandidates) {
            try {
                final Field field = target.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return Optional.ofNullable(field.get(target));
            } catch (NoSuchFieldException ignored) {
                // continue searching
            } catch (IllegalAccessException ex) {
                throw new AssertionError("Unable to access field " + fieldName, ex);
            }
        }

        return Optional.empty();
    }

    private Object readOptionalProperty(final Object target,
                                        final List<String> getterCandidates,
                                        final List<String> fieldCandidates) {
        return tryReadProperty(target, getterCandidates, fieldCandidates).orElse(null);
    }
}
