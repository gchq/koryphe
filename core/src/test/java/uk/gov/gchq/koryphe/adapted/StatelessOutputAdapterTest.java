package uk.gov.gchq.koryphe.adapted;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.Identity;
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatelessOutputAdapterTest extends EqualityTest<StatelessOutputAdapter> {

    @Override
    protected StatelessOutputAdapter getInstance() {
        return new StatelessOutputAdapter(new Identity());
    }

    @Override
    protected Iterable<StatelessOutputAdapter> getDifferentInstances() {
        return Arrays.asList(
                new StatelessOutputAdapter(),
                new StatelessOutputAdapter(new ToLong())
        );
    }

    @Test
    public void shouldReturnTheUnadaptedOutputIfNoAdapterIsProvided() {
        // Given
        StatelessOutputAdapter<Object, Object, Object> soa = new StatelessOutputAdapter<>();

        // When
        Object output = soa.apply(null, "input");

        // Then
        assertEquals("input", output);
    }

    @Test
    public void shouldApplyAnOutputAdapter() {
        // Given
        StatelessOutputAdapter<Object, Integer, Integer> soa = new StatelessOutputAdapter<>(new MultiplyBy(10));

        // When
        Object output = soa.apply(null, 10);

        // Then
        assertEquals(100, output);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        StatelessOutputAdapter<Object, Integer, Integer> soa = new StatelessOutputAdapter<>(new MultiplyBy(10));
        String json =
                "{\n" +
                    "\"class\": \"uk.gov.gchq.koryphe.adapted.StatelessOutputAdapter\",\n" +
                    "\"adapter\": {\n" +
                        "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyBy\",\n" +
                        "\"by\": 10\n" +
                    "}\n" +
                "}";
        // When
        String serialised = JsonSerialiser.serialise(soa);
        StatelessOutputAdapter deserialised = JsonSerialiser.deserialise(json, StatelessOutputAdapter.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(soa, deserialised);
    }
}