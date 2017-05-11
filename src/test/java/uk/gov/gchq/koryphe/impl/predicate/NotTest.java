/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.Test;
import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import java.io.IOException;
import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotTest extends PredicateTest {
    @Test
    public void shouldAcceptTheValueWhenTheWrappedFunctionReturnsFalse() {
        // Given
        final Predicate<String> function = mock(Predicate.class);
        final Not<String> filter = new Not<>(function);
        given(function.test("some value")).willReturn(false);

        // When
        boolean accepted = filter.test("some value");

        // Then
        assertTrue(accepted);
        verify(function).test("some value");
    }

    @Test
    public void shouldRejectTheValueWhenTheWrappedFunctionReturnsTrue() {
        // Given
        final Predicate<String> function = mock(Predicate.class);
        final Not<String> filter = new Not<>(function);
        given(function.test("some value")).willReturn(true);

        // When
        boolean accepted = filter.test("some value");

        // Then
        assertFalse(accepted);
        verify(function).test("some value");
    }

    @Test
    public void shouldRejectTheValueWhenNullFunction() {
        // Given
        final Not<String> filter = new Not<>();

        // When
        boolean accepted = filter.test("some value");

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsA isA = new IsA(String.class);
        final Not<Object> filter = new Not<>(isA);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.Not\",%n" +
                "  \"predicate\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final Not deserialisedFilter = JsonSerialiser.deserialise(json, Not.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(String.class.getName(), ((IsA) deserialisedFilter.getPredicate()).getType());
    }

    @Override
    protected Class<Not> getPredicateClass() {
        return Not.class;
    }

    @Override
    protected Not<Object> getInstance() {
        return new Not<>(new IsA(String.class));
    }

    @Test
    public void shouldCheckInputClass() {
        Predicate predicate = new Not<>(new IsMoreThan(1));
        Signature input = Signature.getInputSignature(predicate);
        assertTrue(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Double.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());

        predicate = new Not<>(new IsXLessThanY());
        input = Signature.getInputSignature(predicate);
        assertTrue(input.assignable(Integer.class, Integer.class).isValid());
        assertTrue(input.assignable(Double.class, Double.class).isValid());
        assertFalse(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Double.class, Integer.class).isValid());

        assertArrayEquals(new Class[]{Signature.UnknownGenericType.class}, input.getClasses());
        assertNull(input.getNumClasses());
    }
}
