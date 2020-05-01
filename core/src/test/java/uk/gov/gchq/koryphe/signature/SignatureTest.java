/*
 * Copyright 2017-2020 Crown Copyright
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
import uk.gov.gchq.koryphe.function.KorypheFunction;
import uk.gov.gchq.koryphe.function.MockFunction;
import uk.gov.gchq.koryphe.function.MockFunction2;
import uk.gov.gchq.koryphe.function.MockFunction2b;
import uk.gov.gchq.koryphe.function.MockFunction3;
import uk.gov.gchq.koryphe.function.MockFunctionMultiParents2;
import uk.gov.gchq.koryphe.impl.binaryoperator.CollectionConcat;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.impl.function.ApplyBiFunction;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.impl.predicate.Or;
import uk.gov.gchq.koryphe.impl.predicate.range.InRange;
import uk.gov.gchq.koryphe.predicate.MockPredicate2False;
import uk.gov.gchq.koryphe.predicate.MockPredicateFalse;
import uk.gov.gchq.koryphe.predicate.MockPredicateTrue;
import uk.gov.gchq.koryphe.tuple.MapTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.tuple.function.KorypheFunction2;
import uk.gov.gchq.koryphe.util.InvalidSignatureTestPredicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void shouldCheckInputForInRange() {
        final Signature input = Signature.getInputSignature(new InRange());
        assertTrue(input.assignable(Long.class).isValid());
        assertFalse(input.assignable(Map.class).isValid());
    }

    @Test
    public void shouldCheckInputForTestPredicateClass() {
        final Signature input = Signature.getInputSignature(new InvalidSignatureTestPredicate());
        assertTrue(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Long.class).isValid());
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

        assertArrayEquals(new Class[]{Object.class}, input.getClasses());
        assertTrue(input.getNumClasses().equals(1));
    }

    @Test
    public void shouldAllowAnyInputsForLambdaFunctions() {
        final Function<Integer, String> toString = Object::toString;
        final Signature input = Signature.getInputSignature(toString);
        assertTrue(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertTrue(input.assignable(Object.class).isValid());
    }

    @Test
    public void shouldAllowAnyInputsForInlineFunctions() {
        final Function<Integer, String> toString = new KorypheFunction<Integer, String>() {
            @Override
            public String apply(final Integer integer) {
                return integer.toString();
            }
        };
        final Signature input = Signature.getInputSignature(toString);
        assertTrue(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertFalse(input.assignable(Object.class).isValid());
    }

    @Test
    public void shouldAllowAnyInputsForMultiLambdaFunctions() {
        final KorypheFunction2<Integer, Long, String> toString = new KorypheFunction2<Integer, Long, String>() {
            @Override
            public String apply(final Integer a, final Long b) {
                return a.toString() + b.toString();
            }
        };
        final Signature input = Signature.getInputSignature(toString);
        assertTrue(input.assignable(Integer.class, Long.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertFalse(input.assignable(Object.class).isValid());
    }

    @Test
    public void ShouldCheckCollectionConcatInputAndOutput() {
        // Given
        final CollectionConcat function = new CollectionConcat();

        // When
        final Signature input = Signature.getInputSignature(function);

        // Then
        assertTrue(input.assignable(Collection.class).isValid());

        // When
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(output.assignable(Collection.class).isValid());
    }

    @Test
    public void shouldCheckApplyBiFunctionTypes() {
        final ApplyBiFunction applyBiFunction = new ApplyBiFunction(new Sum());
        final Signature signature = Signature.getInputSignature(applyBiFunction);

        assertTrue(signature.assignable(Integer.class, Integer.class).isValid());
        assertTrue(signature.assignable(Long.class, Long.class).isValid());

        assertFalse(signature.assignable(Integer.class).isValid());
        assertFalse(signature.assignable(Integer.class, Integer.class, Integer.class).isValid());
        assertFalse(signature.assignable(String.class, String.class).isValid());
    }

    @Test
    public void shouldCheckApplyBiFunctionTypesForInlineBiFunction() {
        final BiFunction<Long, Double, String> inlineBiFunction = new BiFunction<Long, Double, String>() {
            @Override
            public String apply(final Long l, final Double d) {
                return Long.toString(l).concat(Double.toString(d));
            }
        };

        final ApplyBiFunction applyBiFunction = new ApplyBiFunction(inlineBiFunction);
        final Signature signature = Signature.getInputSignature(applyBiFunction);

        assertTrue(signature.assignable(Long.class, Double.class).isValid());

        assertFalse(signature.assignable(Long.class).isValid());
        assertFalse(signature.assignable(Integer.class, Double.class, String.class).isValid());
        assertFalse(signature.assignable(Double.class, Long.class).isValid());
    }

    @Test
    public void shouldGenerateInputSignatureUsingTupleClassWhenNotAssignableToTupleN() {
        final Function<TestTuple, String> fromTestTuple = new KorypheFunction<TestTuple, String>() {
            @Override
            public String apply(final TestTuple r) {
                return "something";
            }
        };

        final Signature signature = Signature.getInputSignature(fromTestTuple);
        assertTrue(signature.assignable(TestTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateOutputSignatureUsingTupleClassWhenNotNotAssignableToTupleN() {
        final Function<String, TestTuple> toTestTuple = new KorypheFunction<String, TestTuple>() {
            @Override
            public TestTuple apply(final String s) {
                return new TestTuple();
            }
        };

        final Signature signature = Signature.getOutputSignature(toTestTuple);
        assertTrue(signature.assignable(TestTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateInputSignatureUsingMapTupleClass() {
        final Function<MapTuple<String>, String> fromMapTuple = new KorypheFunction<MapTuple<String>, String>() {
            @Override
            public String apply(final MapTuple<String> t) {
                return "anything";
            }
        };

        final Signature signature = Signature.getInputSignature(fromMapTuple);
        assertTrue(signature.assignable(MapTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateOutputSignatureUsingMapTupleClass() {
        final Function<String, MapTuple<String>> toMapTuple = new KorypheFunction<String, MapTuple<String>>() {
            @Override
            public MapTuple<String> apply(final String s) {
                return new MapTuple<>();
            }
        };

        final Signature signature = Signature.getOutputSignature(toMapTuple);
        assertTrue(signature.assignable(MapTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    private static class TestTuple implements Tuple<String> {
        private Map<String, Object> values = new HashMap<>();

        @Override
        public void put(final String reference, final Object value) {
            values.put(reference, value);
        }

        @Override
        public Object get(final String reference) {
            return values.get(reference);
        }

        @Override
        public Iterable<Object> values() {
            return values.values();
        }
    }
}
