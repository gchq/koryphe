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

package uk.gov.gchq.koryphe.signature;

import org.junit.Test;
import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.function.MockFunction;
import uk.gov.gchq.koryphe.function.MockFunction2;
import uk.gov.gchq.koryphe.function.MockFunction2b;
import uk.gov.gchq.koryphe.function.MockFunction3;
import uk.gov.gchq.koryphe.function.MockFunctionMultiParents2;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.impl.predicate.Or;
import uk.gov.gchq.koryphe.predicate.MockPredicate2False;
import uk.gov.gchq.koryphe.predicate.MockPredicateFalse;
import uk.gov.gchq.koryphe.predicate.MockPredicateTrue;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SignatureTest {
    @Test
    public void shouldCheckFunctionTypes() {
        final Function function = new MockFunction();
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Object.class).isValid());
        assertTrue(input.assignable(String.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(Object.class, Long.class).isValid());
        assertFalse(output.assignable(Long.class).isValid());

        assertArrayEquals(new Class[]{Object.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());

        assertArrayEquals(new Class[]{String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunction2Types() {
        final Function function = new MockFunction2();
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Double.class, Object.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Double.class, Object.class).isValid());

        assertArrayEquals(new Class[]{Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());

        assertArrayEquals(new Class[]{String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunctionMultiParentsTypes() {
        final Function function = new MockFunctionMultiParents2();
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Double.class, Object.class, Integer.class).isValid());
        assertTrue(input.assignable(Double.class, String.class, Integer.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Double.class, Object.class, Integer.class).isValid());

        assertArrayEquals(new Class[]{Double.class, Object.class, Integer.class}, input.getClasses());
        assertEquals((Integer) 3, input.getNumClasses());

        assertArrayEquals(new Class[]{String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunction2bTypes() {
        final Function function = new MockFunction2b();
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Double.class, Object.class).isValid());
        assertTrue(input.assignable(Double.class, Long.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Integer.class, Double.class, Object.class).isValid());

        assertArrayEquals(new Class[]{Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());

        assertArrayEquals(new Class[]{String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunction3Types() {
        final Function function = new MockFunction3();
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Integer.class, Double.class, Object.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Integer.class, Double.class, Object.class).isValid());

        assertArrayEquals(new Class[]{Integer.class, Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 3, input.getNumClasses());

        assertArrayEquals(new Class[]{String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes() {
        final Predicate predicate = new MockPredicateTrue();
        final Signature input = Signature.getInputSignature(predicate);

        assertTrue(input.assignable(Double.class).isValid());
        assertFalse(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Double.class, Double.class).isValid());

        assertArrayEquals(new Class[]{Double.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes1() {
        final Predicate predicate = new MockPredicateFalse();
        final Signature input = Signature.getInputSignature(predicate);

        assertTrue(input.assignable(Double.class).isValid());
        assertFalse(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Double.class, Integer.class).isValid());
        assertArrayEquals(new Class[]{Double.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes2() {
        final Predicate predicate = new MockPredicate2False();
        final Signature input = Signature.getInputSignature(predicate);

        assertTrue(input.assignable(Double.class, Integer.class).isValid());
        assertFalse(input.assignable(String.class, Integer.class).isValid());
        assertFalse(input.assignable(Double.class, Integer.class, Integer.class).isValid());

        assertArrayEquals(new Class[]{Double.class, Integer.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());
    }

    @Test
    public void shouldCheckOrInputClass() {
        final Predicate predicate = new Or.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();
        final Signature input = Signature.getInputSignature(predicate);

        final ValidationResult result = input.assignable(Integer.class, Double.class);
        assertTrue(result.getErrorString(), result.isValid());

        assertFalse(input.assignable(Integer.class, Collection.class).isValid());
        assertFalse(input.assignable(Double.class).isValid());

        assertArrayEquals(new Class[]{Signature.UnknownGenericType.class}, input.getClasses());
        assertNull(input.getNumClasses());
    }
}
