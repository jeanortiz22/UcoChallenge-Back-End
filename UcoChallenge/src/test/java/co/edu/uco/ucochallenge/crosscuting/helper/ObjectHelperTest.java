package co.edu.uco.ucochallenge.crosscuting.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ObjectHelperTest {

    @Test
    void isNullShouldReturnTrueForNullReference() {
        assertThat(ObjectHelper.isNull(null)).isTrue();
    }

    @Test
    void getDefaultShouldReturnValueWhenPresent() {
        final String value = "value";
        assertThat(ObjectHelper.getDefault(value, "default")).isEqualTo(value);
    }

    @Test
    void getDefaultShouldReturnDefaultWhenValueIsNull() {
        assertThat(ObjectHelper.getDefault(null, "default")).isEqualTo("default");
    }
}
