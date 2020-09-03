package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrentDateTest extends FunctionTest<CurrentDate> {

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Date.class };
    }

    @Test
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
    protected CurrentDate getInstance() {
        return new CurrentDate();
    }

    @Override
    protected Iterable<CurrentDate> getDifferentInstancesOrNull() {
        return null;
    }
}
