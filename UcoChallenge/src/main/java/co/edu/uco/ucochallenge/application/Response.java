package co.edu.uco.ucochallenge.application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import co.edu.uco.ucochallenge.crosscuting.helper.ObjectHelper;
import co.edu.uco.ucochallenge.crosscuting.helper.TextHelper;

public abstract class Response<T> {

    private boolean dataReturned;
    private T data;

    protected Response(final boolean dataReturned, final T data) {
        setDataReturned(dataReturned);
        setData(data);
    }

    private void setDataReturned(final boolean dataReturned) {
        this.dataReturned = dataReturned;
    }

    private void setData(final T data) {
    	this.data = sanitizeData(data);
    }

    protected boolean isDataReturned() {
        return dataReturned;
    }

    protected T getData() {
        return data;
    }
    
    protected T sanitizeData(final T data) {
        return sanitize(data);
    }

    protected static String sanitizeText(final String value) {
        return TextHelper.getDefaultWithTrim(value);
    }

    protected static <E> List<E> sanitizeList(final List<E> values) {
        final var sanitized = ObjectHelper.getDefault(values, List.<E>of());
        final var normalized = sanitized.stream()
                .map(element -> (E) sanitize(element))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return List.copyOf(normalized);
    }

    protected static <K, V> Map<K, V> sanitizeMap(final Map<K, V> values) {
        final var sanitized = ObjectHelper.getDefault(values, Map.<K, V>of());
        final var normalized = sanitized.entrySet().stream()
                .filter(entry -> !ObjectHelper.isNull(entry.getKey()))
                .filter(entry -> !ObjectHelper.isNull(entry.getValue()))
                .collect(Collectors.toMap(
                        entry -> (K) sanitize(entry.getKey()),
                        entry -> (V) sanitize(entry.getValue()),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
        return Map.copyOf(normalized);
    }

    protected static <S> S sanitizeDto(final S dto, final Supplier<S> defaultSupplier) {
        if (!ObjectHelper.isNull(dto)) {
            return sanitize(dto);
        }
        if (ObjectHelper.isNull(defaultSupplier)) {
            return null;
        }
        return sanitize(defaultSupplier.get());
    }

    @SuppressWarnings("unchecked")
    private static <O> O sanitize(final O data) {
        if (ObjectHelper.isNull(data)) {
            return null;
        }
        if (data instanceof String stringData) {
            return (O) sanitizeText(stringData);
        }
        if (data instanceof List<?> listData) {
            return (O) sanitizeList((List<Object>) listData);
        }
        if (data instanceof Map<?, ?> mapData) {
            return (O) sanitizeMap((Map<Object, Object>) mapData);
        }
        return data;
    }
}

