/*
 * Copyright 2017-2022 Crown Copyright
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

import org.junit.jupiter.api.Test;

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
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class SignatureTest {

    @Test
    public void shouldCheckFunctionTypes() {
        // Given
        final Function function = new MockFunction();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(input.assignable(Object.class).isValid());
        assertTrue(input.assignable(String.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(Object.class, Long.class).isValid());
        assertFalse(output.assignable(Long.class).isValid());

        assertArrayEquals(new Class[] {Object.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());

        assertArrayEquals(new Class[] {String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckInputForInRange() {
        // When
        final Signature input = Signature.getInputSignature(new InRange());

        // Then
        assertTrue(input.assignable(Long.class).isValid());
        assertFalse(input.assignable(Map.class).isValid());
    }

    @Test
    public void shouldCheckInputForTestPredicateClass() {
        // When
        final Signature input = Signature.getInputSignature(new InvalidSignatureTestPredicate());

        // Then
        assertTrue(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Long.class).isValid());
    }

    @Test
    public void shouldCheckFunction2Types() {
        // Given
        final Function function = new MockFunction2();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(input.assignable(Double.class, Object.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Double.class, Object.class).isValid());

        assertArrayEquals(new Class[] {Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());

        assertArrayEquals(new Class[] {String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunctionMultiParentsTypes() {
        // Given
        final Function function = new MockFunctionMultiParents2();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(input.assignable(Double.class, Object.class, Integer.class).isValid());
        assertTrue(input.assignable(Double.class, String.class, Integer.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Double.class, Object.class, Integer.class).isValid());

        assertArrayEquals(new Class[] {Double.class, Object.class, Integer.class}, input.getClasses());
        assertEquals((Integer) 3, input.getNumClasses());

        assertArrayEquals(new Class[] {String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunction2bTypes() {
        // Given
        final Function function = new MockFunction2b();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(input.assignable(Double.class, Object.class).isValid());
        assertTrue(input.assignable(Double.class, Long.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Integer.class, Double.class, Object.class).isValid());

        assertArrayEquals(new Class[] {Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());

        assertArrayEquals(new Class[] {String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckFunction3Types() {
        // Given
        final Function function = new MockFunction3();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        assertTrue(input.assignable(Integer.class, Double.class, Object.class).isValid());
        assertTrue(output.assignable(String.class).isValid());

        assertFalse(input.assignable(String.class).isValid());
        assertFalse(output.assignable(Integer.class, Double.class, Object.class).isValid());

        assertArrayEquals(new Class[] {Integer.class, Double.class, Object.class}, input.getClasses());
        assertEquals((Integer) 3, input.getNumClasses());

        assertArrayEquals(new Class[] {String.class}, output.getClasses());
        assertEquals((Integer) 1, output.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes() {
        // Given
        final Predicate predicate = new MockPredicateTrue();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        assertTrue(input.assignable(Double.class).isValid());
        assertFalse(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Double.class, Double.class).isValid());

        assertArrayEquals(new Class[] {Double.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes1() {
        // Given
        final Predicate predicate = new MockPredicateFalse();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        assertTrue(input.assignable(Double.class).isValid());
        assertFalse(input.assignable(String.class).isValid());
        assertFalse(input.assignable(Double.class, Integer.class).isValid());
        assertArrayEquals(new Class[] {Double.class}, input.getClasses());
        assertEquals((Integer) 1, input.getNumClasses());
    }

    @Test
    public void shouldCheckPredicateTypes2() {
        // Given
        final Predicate predicate = new MockPredicate2False();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        assertTrue(input.assignable(Double.class, Integer.class).isValid());
        assertFalse(input.assignable(String.class, Integer.class).isValid());
        assertFalse(input.assignable(Double.class, Integer.class, Integer.class).isValid());

        assertArrayEquals(new Class[] {Double.class, Integer.class}, input.getClasses());
        assertEquals((Integer) 2, input.getNumClasses());
    }

    @Test
    public void shouldCheckOrInputClass() {
        // Given
        final Predicate predicate = new Or.Builder()
                .select(0)
                .execute(new IsMoreThan(1))
                .select(1)
                .execute(new IsLessThan(10.0))
                .build();
        final Signature input = Signature.getInputSignature(predicate);

        // When
        final ValidationResult result = input.assignable(Integer.class, Double.class);

        // Then
        assertTrue(result.isValid(), result.getErrorString());

        assertFalse(input.assignable(Integer.class, Collection.class).isValid());
        assertFalse(input.assignable(Double.class).isValid());
    }

    @Test
    public void shouldAllowAnyInputsForLambdaFunctions() {
        // Given
        final Function<Integer, String> toString = Object::toString;

        // When
        final Signature input = Signature.getInputSignature(toString);

        // Then
        assertTrue(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertTrue(input.assignable(Object.class).isValid());
    }

    @Test
    public void shouldAllowAnyInputsForInlineFunctions() {
        // Given
        final Function<Integer, String> toString = new KorypheFunction<>() {
            @Override
            public String apply(final Integer integer) {
                return integer.toString();
            }
        };

        // When
        final Signature input = Signature.getInputSignature(toString);

        // Then
        assertTrue(input.assignable(Integer.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertFalse(input.assignable(Object.class).isValid());
    }

    @Test
    public void shouldAllowAnyInputsForMultiLambdaFunctions() {
        // Given
        final KorypheFunction2<Integer, Long, String> toString = new KorypheFunction2<>() {
            @Override
            public String apply(final Integer a, final Long b) {
                return a.toString() + b.toString();
            }
        };

        // When
        final Signature input = Signature.getInputSignature(toString);

        // Then
        assertTrue(input.assignable(Integer.class, Long.class).isValid());
        assertFalse(input.assignable(Integer.class, Integer.class).isValid());
        assertFalse(input.assignable(Object.class).isValid());
    }

    @Test
    public void shouldCheckCollectionConcatInputAndOutput() {
        // Given
        final CollectionConcat function = new CollectionConcat();

        // When
        final Signature input = Signature.getInputSignature(function);

        // Then
        assertTrue(input.assignable(Collection.class).isValid());
        assertFalse(input.assignable(Object.class).isValid());

        // When
        final Signature output = Signature.getOutputSignature(function);

        // Then
        assertTrue(output.assignable(Collection.class).isValid());
        assertFalse(output.assignable(Object.class).isValid());
    }

    @Test
    public void shouldCheckApplyBiFunctionTypes() {
        // Given
        final ApplyBiFunction applyBiFunction = new ApplyBiFunction(new Sum());

        // When
        final Signature signature = Signature.getInputSignature(applyBiFunction);

        // Then
        assertTrue(signature.assignable(Integer.class, Integer.class).isValid());
        assertTrue(signature.assignable(Long.class, Long.class).isValid());

        assertFalse(signature.assignable(Integer.class).isValid());
        assertFalse(signature.assignable(Integer.class, Integer.class, Integer.class).isValid());
        assertFalse(signature.assignable(String.class, String.class).isValid());
    }

    @Test
    public void shouldCheckApplyBiFunctionTypesForInlineBiFunction() {
        // Given
        final BiFunction<Long, Double, String> inlineBiFunction = new BiFunction<>() {
            @Override
            public String apply(final Long l, final Double d) {
                return Long.toString(l).concat(Double.toString(d));
            }
        };

        final ApplyBiFunction applyBiFunction = new ApplyBiFunction(inlineBiFunction);

        // When
        final Signature signature = Signature.getInputSignature(applyBiFunction);

        // Then
        assertTrue(signature.assignable(Long.class, Double.class).isValid());

        assertFalse(signature.assignable(Long.class).isValid());
        assertFalse(signature.assignable(Integer.class, Double.class, String.class).isValid());
        assertFalse(signature.assignable(Double.class, Long.class).isValid());
    }

    @Test
    public void shouldGenerateInputSignatureUsingTupleClassWhenNotParameterised() {
        // Given
        final Function<TestTuple, String> fromTestTuple = new KorypheFunction<>() {
            @Override
            public String apply(final TestTuple r) {
                return "something";
            }
        };

        // When
        final Signature signature = Signature.getInputSignature(fromTestTuple);

        // Then
        assertTrue(signature.assignable(TestTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateOutputSignatureUsingTupleClassWhenNotParameterised() {
        // Given
        final Function<String, TestTuple> toTestTuple = new KorypheFunction<>() {
            @Override
            public TestTuple apply(final String s) {
                return new TestTuple();
            }
        };

        // When
        final Signature signature = Signature.getOutputSignature(toTestTuple);

        // Then
        assertTrue(signature.assignable(TestTuple.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateInputSignatureUsingMapTupleParameterTypes() {
        // Given
        final Function<MapTuple<String>, String> fromMapTuple = new KorypheFunction<>() {
            @Override
            public String apply(final MapTuple<String> t) {
                return "anything";
            }
        };

        // When
        final Signature signature = Signature.getInputSignature(fromMapTuple);

        // Then
        assertTrue(signature.assignable(String.class).isValid());
        assertTrue(signature.assignable(String.class, String.class).isValid());
        assertFalse(signature.assignable(Object.class).isValid());
    }

    @Test
    public void shouldGenerateOutputSignatureUsingMapTupleParameterTypes() {
        // Given
        final Function<String, MapTuple<String>> toMapTuple = new KorypheFunction<>() {
            @Override
            public MapTuple<String> apply(final String s) {
                return new MapTuple<>();
            }
        };

        // When
        final Signature signature = Signature.getOutputSignature(toMapTuple);

        // Then
        assertTrue(signature.assignable(String.class).isValid());
        assertTrue(signature.assignable(String.class, String.class).isValid());
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

    @Test
    public void shouldBePermissiveWithMockedBinaryOperator() {
        // Given
        final BinaryOperator<Integer> mockBinaryOperator = mock(BinaryOperator.class);

        // When
        final Signature signature = Signature.getInputSignature(mockBinaryOperator);

        // Then
        assertTrue(signature.assignable(Object.class).isValid());
        assertTrue(signature.assignable(Object.class, Object.class).isValid());
    }

    @Test
    public void shouldBePermissiveWithInlineBinaryOperator() {
        // Given
        final BinaryOperator<Integer> inlineBinaryOperator = Integer::sum;

        // When
        final Signature signature = Signature.getInputSignature(inlineBinaryOperator);

        // Then
        assertTrue(signature.assignable(Object.class).isValid());
        assertTrue(signature.assignable(Object.class, Object.class).isValid());
    }

    @Test
    public void shouldCheckTestIntegerBinaryOperatorParameters() {
        // Given
        final TestIntegerBinaryOperator testBinaryOperator = new TestIntegerBinaryOperator();

        // When
        final Signature signature = Signature.getInputSignature(testBinaryOperator);

        // Then
        assertTrue(signature.assignable(Integer.class).isValid());
        assertFalse(signature.assignable(String.class).isValid());
        assertFalse(signature.assignable(Integer.class, Integer.class).isValid());
    }

    @Test
    public void shouldCheckTestObjectBinaryOperatorParameters() {
        // Given
        final TestObjectBinaryOperator objectBinaryOperator = new TestObjectBinaryOperator();

        // When
        final Signature signature = Signature.getInputSignature(objectBinaryOperator);

        // Then
        assertTrue(signature.assignable(Object.class).isValid());
        assertFalse(signature.assignable(Object.class, Object.class).isValid());
    }

    private static class TestIntegerBinaryOperator implements BinaryOperator<Integer> {
        @Override
        public Integer apply(final Integer integer, final Integer integer2) {
            return integer + integer2;
        }
    }

    private static class TestObjectBinaryOperator implements BinaryOperator<Object> {
        @Override
        public Object apply(final Object integer, final Object integer2) {
            return integer.toString().concat(integer2.toString());
        }
    }

}
