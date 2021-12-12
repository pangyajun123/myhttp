package com.iwbfly.myhttp.utils;

import java.io.*;
import java.lang.reflect.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * @auther: pangyajun
 * @create: 2021/12/1 15:53
 **/
public class Util {
    /**
     * The HTTP Content-Length header field name.
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * The HTTP Content-Encoding header field name.
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * The HTTP Retry-After header field name.
     */
    public static final String RETRY_AFTER = "Retry-After";
    /**
     * Value for the Content-Encoding header that indicates that GZIP encoding is in use.
     */
    public static final String ENCODING_GZIP = "gzip";
    /**
     * Value for the Content-Encoding header that indicates that DEFLATE encoding is in use.
     */
    public static final String ENCODING_DEFLATE = "deflate";
    /**
     * UTF-8: eight-bit UCS Transformation Format.
     */
    public static final Charset UTF_8 = Charset.forName("UTF-8");

    // com.google.common.base.Charsets
    /**
     * ISO-8859-1: ISO Latin Alphabet Number 1 (ISO-LATIN-1).
     */
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    private static final int BUF_SIZE = 0x800; // 2K chars (4K bytes)




    private Util() { // no instances
    }

    /**
     * Copy of {@code com.google.common.base.Preconditions#checkArgument}.
     */
    public static void checkArgument(boolean expression,
                                     String errorMessageTemplate,
                                     Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(
                    format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * Copy of {@code com.google.common.base.Preconditions#checkNotNull}.
     */
    public static <T> T checkNotNull(T reference,
                                     String errorMessageTemplate,
                                     Object... errorMessageArgs) {
        if (reference == null) {
            // If either of these parameters is null, the right thing happens anyway
            throw new NullPointerException(
                    format(errorMessageTemplate, errorMessageArgs));
        }
        return reference;
    }

    /**
     * Copy of {@code com.google.common.base.Preconditions#checkState}.
     */
    public static void checkState(boolean expression,
                                  String errorMessageTemplate,
                                  Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(
                    format(errorMessageTemplate, errorMessageArgs));
        }
    }

    /**
     * Identifies a method as a default instance method.
     */
    public static boolean isDefault(Method method) {
        // Default methods are public non-abstract, non-synthetic, and non-static instance methods
        // declared in an interface.
        // method.isDefault() is not sufficient for our usage as it does not check
        // for synthetic methods. As a result, it picks up overridden methods as well as actual default
        // methods.
        final int SYNTHETIC = 0x00001000;
        return ((method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) == Modifier.PUBLIC)
                && method.getDeclaringClass().isInterface();
    }

    /**
     * Adapted from {@code com.google.common.base.Strings#emptyToNull}.
     */
    public static String emptyToNull(String string) {
        return string == null || string.isEmpty() ? null : string;
    }

    /**
     * Removes values from the array that meet the criteria for removal via the supplied
     * {@link Predicate} value
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] removeValues(T[] values, Predicate<T> shouldRemove, Class<T> type) {
        Collection<T> collection = new ArrayList<>(values.length);
        for (T value : values) {
            if (shouldRemove.negate().test(value)) {
                collection.add(value);
            }
        }
        T[] array = (T[]) Array.newInstance(type, collection.size());
        return collection.toArray(array);
    }

    /**
     * Adapted from {@code com.google.common.base.Strings#emptyToNull}.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
        Collection<T> collection;
        if (iterable instanceof Collection) {
            collection = (Collection<T>) iterable;
        } else {
            collection = new ArrayList<T>();
            for (T element : iterable) {
                collection.add(element);
            }
        }
        T[] array = (T[]) Array.newInstance(type, collection.size());
        return collection.toArray(array);
    }

    /**
     * Returns an unmodifiable collection which may be empty, but is never null.
     */
    public static <T> Collection<T> valuesOrEmpty(Map<String, Collection<T>> map, String key) {
        return map.containsKey(key) && map.get(key) != null ? map.get(key) : Collections.<T>emptyList();
    }

    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }

    private static final Map<Class<?>, Supplier<Object>> EMPTIES;
    static {
        final Map<Class<?>, Supplier<Object>> empties = new LinkedHashMap<Class<?>, Supplier<Object>>();
        empties.put(boolean.class, () -> false);
        empties.put(Boolean.class, () -> false);
        empties.put(byte[].class, () -> new byte[0]);
        empties.put(Collection.class, Collections::emptyList);
        empties.put(Iterator.class, Collections::emptyIterator);
        empties.put(List.class, Collections::emptyList);
        empties.put(Map.class, Collections::emptyMap);
        empties.put(Set.class, Collections::emptySet);
        empties.put(Optional.class, Optional::empty);
        empties.put(Stream.class, Stream::empty);
        EMPTIES = Collections.unmodifiableMap(empties);
    }

    /**
     * Adapted from {@code com.google.common.io.CharStreams.toString()}.
     */
    public static String toString(Reader reader) throws IOException {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder to = new StringBuilder();
            CharBuffer charBuf = CharBuffer.allocate(BUF_SIZE);
            // must cast to super class Buffer otherwise break when running with java 11
            Buffer buf = charBuf;
            while (reader.read(charBuf) != -1) {
                buf.flip();
                to.append(charBuf);
                buf.clear();
            }
            return to.toString();
        } finally {
            ensureClosed(reader);
        }
    }

    /**
     * Adapted from {@code com.google.common.io.ByteStreams.toByteArray()}.
     */
    public static byte[] toByteArray(InputStream in) throws IOException {
        checkNotNull(in, "in");
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            copy(in, out);
            return out.toByteArray();
        } finally {
            ensureClosed(in);
        }
    }

    /**
     * Adapted from {@code com.google.common.io.ByteStreams.copy()}.
     */
    private static long copy(InputStream from, OutputStream to)
            throws IOException {
        checkNotNull(from, "from");
        checkNotNull(to, "to");
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    public static String decodeOrDefault(byte[] data, Charset charset, String defaultValue) {
        if (data == null) {
            return defaultValue;
        }
        checkNotNull(charset, "charset");
        try {
            return charset.newDecoder().decode(ByteBuffer.wrap(data)).toString();
        } catch (CharacterCodingException ex) {
            return defaultValue;
        }
    }

    /**
     * If the provided String is not null or empty.
     *
     * @param value to evaluate.
     * @return true of the value is not null and not empty.
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * If the provided String is null or empty.
     *
     * @param value to evaluate.
     * @return true if the value is null or empty.
     */
    public static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
