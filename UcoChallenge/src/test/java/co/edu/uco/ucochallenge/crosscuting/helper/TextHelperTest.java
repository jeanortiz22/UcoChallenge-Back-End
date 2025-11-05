package co.edu.uco.ucochallenge.crosscuting.helper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TextHelperTest {

    @Test
    void getDefaultShouldReturnEmptyStringForNullValue() {
        assertThat(TextHelper.getDefault(null)).isEmpty();
    }

    @Test
    void getDefaultWithTrimShouldTrimWhitespace() {
        assertThat(TextHelper.getDefaultWithTrim("  value  ")).isEqualTo("value");
    }

    @Test
    void isEmptyShouldReturnTrueForWhitespaceOnlyValues() {
        assertThat(TextHelper.isEmpty("  ")).isTrue();
    }

    @Test
    void isEmptyApplyingTrimShouldDetectNullAndWhitespace() {
        assertThat(TextHelper.isEmptyApplyingTrim(null)).isTrue();
        assertThat(TextHelper.isEmptyApplyingTrim("  ")).isTrue();
        assertThat(TextHelper.isEmptyApplyingTrim("data")).isFalse();
    }
}