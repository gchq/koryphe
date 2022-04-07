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

import static org.assertj.core.api.Assertions.from;
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
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Object.class)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Object.class, Long.class)
                .returns(new Class[] {Object.class}, from(Signature::getClasses))
                .returns((Integer) 1, from(Signature::getNumClasses));

        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Long.class)
                .returns(new Class[] {String.class}, from(Signature::getClasses))
                .returns((Integer) 1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckInputForInRange() {
        // When
        final Signature input = Signature.getInputSignature(new InRange());

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Long.class)
                .isNotAssignableFrom(Map.class);
    }

    @Test
    public void shouldCheckInputForTestPredicateClass() {
        // When
        final Signature input = Signature.getInputSignature(new InvalidSignatureTestPredicate());

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Long.class);
    }

    @Test
    public void shouldCheckFunction2Types() {
        // Given
        final Function function = new MockFunction2();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class, Object.class)
                .isNotAssignableFrom(String.class)
                .returns(new Class[] {Double.class, Object.class}, from(Signature::getClasses))
                .returns((Integer) 2, from(Signature::getNumClasses));

        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Double.class, Object.class)
                .returns(new Class[] {String.class}, from(Signature::getClasses))
                .returns((Integer) 1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckFunctionMultiParentsTypes() {
        // Given
        final Function function = new MockFunctionMultiParents2();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class, Object.class, Integer.class)
                .isAssignableFrom(Double.class, String.class, Integer.class)
                .isNotAssignableFrom(String.class)
                .returns(new Class[] {Double.class, Object.class, Integer.class}, from(Signature::getClasses))
                .returns(3, from(Signature::getNumClasses));

        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Double.class, Object.class, Integer.class)
                .returns(new Class[] {String.class}, from(Signature::getClasses))
                .returns(1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckFunction2bTypes() {
        // Given
        final Function function = new MockFunction2b();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class, Object.class)
                .isAssignableFrom(Double.class, Long.class)
                .isNotAssignableFrom(String.class)
                .returns(new Class[] {Double.class, Object.class}, from(Signature::getClasses))
                .returns(2, from(Signature::getNumClasses));

        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Integer.class, Double.class, Object.class)
                .returns(new Class[] {String.class}, from(Signature::getClasses))
                .returns(1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckFunction3Types() {
        // Given
        final Function function = new MockFunction3();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class, Double.class, Object.class)
                .isNotAssignableFrom(String.class)
                .returns(new Class[] {Integer.class, Double.class, Object.class}, from(Signature::getClasses))
                .returns(3, from(Signature::getNumClasses));

        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isNotAssignableFrom(Integer.class, Double.class, Object.class)
                .returns(new Class[] {String.class}, from(Signature::getClasses))
                .returns(1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckPredicateTypes() {
        // Given
        final Predicate predicate = new MockPredicateTrue();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class)
                .isNotAssignableFrom(String.class)
                .isNotAssignableFrom(Double.class, Double.class)
                .returns(new Class[] {Double.class}, from(Signature::getClasses))
                .returns(1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckPredicateTypes1() {
        // Given
        final Predicate predicate = new MockPredicateFalse();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class)
                .isNotAssignableFrom(String.class)
                .isNotAssignableFrom(Double.class, Integer.class)
                .returns(new Class[] {Double.class}, from(Signature::getClasses))
                .returns(1, from(Signature::getNumClasses));
    }

    @Test
    public void shouldCheckPredicateTypes2() {
        // Given
        final Predicate predicate = new MockPredicate2False();

        // When
        final Signature input = Signature.getInputSignature(predicate);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Double.class, Integer.class)
                .isNotAssignableFrom(String.class, Integer.class)
                .isNotAssignableFrom(Double.class, Integer.class, Integer.class)
                .returns(new Class[] {Double.class, Integer.class}, from(Signature::getClasses))
                .returns(2, from(Signature::getNumClasses));
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

        // When / Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class, Double.class)
                .isNotAssignableFrom(Integer.class, Collection.class)
                .isNotAssignableFrom(Double.class);
    }

    @Test
    public void shouldAllowAnyInputsForLambdaFunctions() {
        // Given
        final Function<Integer, String> toString = Object::toString;

        // When
        final Signature input = Signature.getInputSignature(toString);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class)
                .isNotAssignableFrom(Integer.class, Integer.class)
                .isAssignableFrom(Object.class);
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
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class)
                .isNotAssignableFrom(Integer.class, Integer.class)
                .isNotAssignableFrom(Object.class);
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
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class, Long.class)
                .isNotAssignableFrom(Integer.class, Integer.class)
                .isNotAssignableFrom(Object.class);
    }

    @Test
    public void shouldCheckCollectionConcatInputAndOutput() {
        // Given
        final CollectionConcat function = new CollectionConcat();

        // When
        final Signature input = Signature.getInputSignature(function);
        final Signature output = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Collection.class)
                .isNotAssignableFrom(Object.class);

        SignatureAssert.assertThat(output)
                .isAssignableFrom(Collection.class)
                .isNotAssignableFrom(Object.class);
    }

    @Test
    public void shouldCheckApplyBiFunctionTypes() {
        // Given
        final ApplyBiFunction applyBiFunction = new ApplyBiFunction(new Sum());

        // When
        final Signature input = Signature.getInputSignature(applyBiFunction);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class, Integer.class)
                .isAssignableFrom(Long.class, Long.class)
                .isNotAssignableFrom(Integer.class)
                .isNotAssignableFrom(Integer.class, Integer.class, Integer.class)
                .isNotAssignableFrom(String.class, String.class);
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
        final Signature input = Signature.getInputSignature(applyBiFunction);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Long.class, Double.class)
                .isNotAssignableFrom(Long.class)
                .isNotAssignableFrom(Integer.class, Double.class, String.class)
                .isNotAssignableFrom(Double.class, Long.class);
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
        final Signature input = Signature.getInputSignature(fromTestTuple);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(TestTuple.class)
                .isNotAssignableFrom(Object.class);
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
        final Signature output = Signature.getOutputSignature(toTestTuple);

        // Then
        SignatureAssert.assertThat(output)
                .isAssignableFrom(TestTuple.class)
                .isNotAssignableFrom(Object.class);
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
        final Signature input = Signature.getInputSignature(fromMapTuple);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(String.class)
                .isAssignableFrom(String.class, String.class)
                .isNotAssignableFrom(Object.class);
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
        final Signature output = Signature.getOutputSignature(toMapTuple);

        // Then
        SignatureAssert.assertThat(output)
                .isAssignableFrom(String.class)
                .isAssignableFrom(String.class, String.class)
                .isNotAssignableFrom(Object.class);
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
        final Signature input = Signature.getInputSignature(mockBinaryOperator);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Object.class)
                .isAssignableFrom(Object.class, Object.class);
    }

    @Test
    public void shouldBePermissiveWithInlineBinaryOperator() {
        // Given
        final BinaryOperator<Integer> inlineBinaryOperator = Integer::sum;

        // When
        final Signature input = Signature.getInputSignature(inlineBinaryOperator);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Object.class)
                .isAssignableFrom(Object.class, Object.class);
    }

    @Test
    public void shouldCheckTestIntegerBinaryOperatorParameters() {
        // Given
        final TestIntegerBinaryOperator testBinaryOperator = new TestIntegerBinaryOperator();

        // When
        final Signature input = Signature.getInputSignature(testBinaryOperator);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class)
                .isNotAssignableFrom(String.class)
                .isNotAssignableFrom(Integer.class, Integer.class);
    }

    @Test
    public void shouldCheckTestObjectBinaryOperatorParameters() {
        // Given
        final TestObjectBinaryOperator objectBinaryOperator = new TestObjectBinaryOperator();

        // When
        final Signature input = Signature.getInputSignature(objectBinaryOperator);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Object.class)
                .isNotAssignableFrom(Object.class, Object.class);
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
