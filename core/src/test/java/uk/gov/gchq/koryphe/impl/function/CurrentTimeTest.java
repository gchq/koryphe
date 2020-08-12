package uk.gov.gchq.koryphe.impl.function;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrentTimeTest extends FunctionTest {
    @Override
    protected Class<? extends Function> getFunctionClass() {
        return CurrentTime.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Long.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        String json = "{ \"class\": \"uk.gov.gchq.koryphe.impl.function.CurrentTime\" }";
        CurrentTime currentTime = new CurrentTime();

        // When
        String serialised = JsonSerialiser.serialise(currentTime);
        CurrentTime deserialised = JsonSerialiser.deserialise(json, CurrentTime.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(currentTime, deserialised);

    }

    @Override
    protected Function getInstance() {
        return new CurrentTime();
    }
}
