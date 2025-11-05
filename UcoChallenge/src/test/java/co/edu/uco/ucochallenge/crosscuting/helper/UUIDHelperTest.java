package co.edu.uco.ucochallenge.crosscuting.helper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UUIDHelperTest {

    @Test
    void getDefaultShouldReturnAllZeroUuid() {
        assertThat(UUIDHelper.getDefault()).isEqualTo(new UUID(0L, 0L));
    }

    @Test
    void getDefaultShouldReturnProvidedValueWhenNotNull() {
        final UUID value = UUID.randomUUID();
        assertThat(UUIDHelper.getDefault(value)).isEqualTo(value);
    }

    @Test
    void getFromStringShouldReturnDefaultWhenValueIsBlank() {
        assertThat(UUIDHelper.getFromString(" \t ")).isEqualTo(UUIDHelper.getDefault());
    }

    @Test
    void getFromStringShouldParseUuid() {
        final UUID value = UUID.randomUUID();
        assertThat(UUIDHelper.getFromString(value.toString())).isEqualTo(value);
    }

}
