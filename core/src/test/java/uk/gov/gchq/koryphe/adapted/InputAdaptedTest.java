package uk.gov.gchq.koryphe.adapted;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.FunctionChain;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.util.EqualityTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputAdaptedTest extends EqualityTest<InputAdapted> {

    @Override
    protected InputAdapted getInstance() {
        return new InputAdapted(new ToLong());
    }

    @Override
    protected Iterable<InputAdapted> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new InputAdapted(),
                new InputAdapted(new ToString()),
                new InputAdapted(new FunctionChain(new ToLong()))
        );
    }

    @Test
    public void shouldApplyInputAdapterToInput() {
        // Given
        Integer input = 5;

        // When
        InputAdapted<Object, Long> inputAdapted = new InputAdapted<>(new ToLong());
        Long output = inputAdapted.adaptInput(input);

        // Then
        assertEquals(Long.class, output.getClass());
        assertEquals(5L, output);
    }
}