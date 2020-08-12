package uk.gov.gchq.koryphe.impl.function;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrentDateTest extends FunctionTest {
    @Override
    protected Class<? extends Function> getFunctionClass() {
        return CurrentDate.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Date.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        String json = "{ \"class\": \"uk.gov.gchq.koryphe.impl.function.CurrentDate\" }";
        CurrentDate CurrentDate = new CurrentDate();

        // When
        String serialised = JsonSerialiser.serialise(CurrentDate);
        CurrentDate deserialised = JsonSerialiser.deserialise(json, CurrentDate.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(CurrentDate, deserialised);

    }

    @Override
    protected Function getInstance() {
        return new CurrentDate();
    }
}
