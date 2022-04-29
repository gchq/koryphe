/*
 * Copyright 2018-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.tuple.ReflectiveTuple.Cache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.gchq.koryphe.tuple.ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST;

public class ReflectiveTupleTest {

    private static final String FIELD_X = "fieldX";
    private static final String FIELD_B = "fieldB";
    private static final String FIELD_A = "fieldA";
    private static final String METHOD_B = "methodB";
    private static final String NESTED_FIELD = "nestedField." + FIELD_A;

    private ReflectiveTuple testObj;

    @BeforeEach
    public void setUp() {
        testObj = new ReflectiveTuple(new ExampleObj());
    }

    @Test
    public void shouldNotFindMissingField() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObj.get(FIELD_X))
                .withMessage(SELECTION_S_DOES_NOT_EXIST, FIELD_X);
    }

    @Test
    public void shouldNotFindMissingMethod() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObj.get("get" + FIELD_X))
                .withMessage(SELECTION_S_DOES_NOT_EXIST, "get" + FIELD_X);
    }

    @Test
    public void shouldFindPublicField() {
        assertThat(testObj.get(FIELD_A)).isEqualTo("fa");
    }

    @Test
    public void shouldNotFindPublicFieldIfSubfieldNameIsBlank() {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.get("nestedField."))
                .withMessage("nested field reference is required");
    }

    @Test
    public void shouldNotFindPublicFieldWithDotAtStart() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.get("." + FIELD_A))
                .withMessage("field reference is required");
    }

    @Test
    public void shouldNotFindPublicFieldWith2Dots() {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.get("nestedField.." + FIELD_A))
                .withMessage("field reference is required");
    }

    @Test
    public void shouldGetNestedField() {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When / Then
        assertThat(testObj.get(NESTED_FIELD)).isEqualTo("fa");
    }

    @Test
    public void shouldNotFindPrivateField() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObj.get(FIELD_B))
                .withMessage(SELECTION_S_DOES_NOT_EXIST, FIELD_B);
    }

    @Test
    public void shouldFindPublicMethod() {
        assertThat(testObj.get("methodA")).isEqualTo("ma");
    }

    @Test
    public void shouldNotFindPrivateMethod() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObj.get(METHOD_B))
                .withMessage(SELECTION_S_DOES_NOT_EXIST, METHOD_B);
    }

    @Test
    public void shouldPutField() {
        // Given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);

        // When
        testObj.put("fieldA", "changed");

        // Then
        assertThat(record.fieldA).isEqualTo("changed");
    }


    @Test
    public void shouldNotPutFieldWithWrongParam() {
        // Given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.put("fieldA", 1))
                .withMessage(ReflectiveTuple.ERROR_WRONG_PARAM, "field", "fieldA", String.class, Integer.class.getSimpleName());
    }

    @Test
    public void shouldPutMethod() {
        // Given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);

        // When
        testObj.put("fieldB", "changed");

        // Then
        assertThat(record.fieldB).isEqualTo("changed");

    }

    @Test
    public void shouldNotPutFieldIfSubfieldIsBlank() {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.put("nestedField.", "changed"))
                .withMessage("nested field reference is required");
    }

    @Test
    public void shouldNotPutFieldWithDotAtStart() {
        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.put("." + FIELD_A, "changed"))
                .withMessage("field reference is required");
    }

    @Test
    public void shouldNotPutFieldWith2Dots() {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.put("nestedField.." + FIELD_A, "changed"))
                .withMessage("field reference is required");
    }

    @Test
    public void shouldNotPutMethodWithWrongParam() {
        // Given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> testObj.put("fieldB", 1))
                .withMessage(ReflectiveTuple.ERROR_WRONG_PARAM, "method", "setFieldB",
                Collections.singletonList(String.class), Integer.class.getSimpleName());
    }

    @Test
    public void shouldNotPutMethod() {
        // Given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);

        // When / Then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> testObj.put("fieldC", 1))
                .withMessage(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, "fieldC");
    }

    @Test
    public void shouldPutNestedField() {
        // Given
        final ExampleNestedObj1 record = new ExampleNestedObj1();
        testObj = new ReflectiveTuple(record);

        // When
        testObj.put(NESTED_FIELD, "changed");

        // Then
        assertThat(record.getNestedField().fieldA).isEqualTo("changed");
    }

    @Test
    public void shouldNotValues() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> testObj.values());
    }

    @Test
    public void shouldGetInOrderOfMethodGetIs() {
        // When
        testObj = new ReflectiveTuple(new ExampleObj2());

        // Then
        assertThat(testObj.get("valueA")).isEqualTo("fa");
        assertThat(testObj.get("valueB")).isEqualTo("mb");
        assertThat(testObj.get("valueC")).isEqualTo("isc");
    }

    @Test
    public void shouldUseCache() throws Exception {
        // Given
        final Cache<Field> fieldCache = mock(Cache.class);
        final Cache<Method> methodCache = mock(Cache.class);
        when(fieldCache.get(ExampleObj2.class, "valueA")).thenReturn(null);
        when(methodCache.get(ExampleObj2.class, "getValueB")).thenReturn(null);

        testObj = new ReflectiveTuple(new ExampleObj2(), fieldCache, methodCache);

        // When
        final Object valueA1 = testObj.get("valueA");
        final Object valueB1 = testObj.get("valueB");

        // Then
        assertThat(valueA1).isEqualTo("fa");
        assertThat(valueB1).isEqualTo("mb");
        verify(fieldCache, times(1)).put(eq(ExampleObj2.class), eq("valueA"), any(Field.class));
        verify(methodCache, times(1)).put(eq(ExampleObj2.class), eq("getValueB"), any(Method.class));

        // Given - mock set to alternative values.
        when(fieldCache.get(ExampleObj2.class, "valueA")).thenReturn(ExampleObj2.class.getField("valueAlt"));
        when(methodCache.get(ExampleObj2.class, "getValueB")).thenReturn(ExampleObj2.class.getMethod("getValueBAlt"));

        // When
        final Object valueA2 = testObj.get("valueA");
        final Object valueB2 = testObj.get("valueB");

        // Then
        assertThat(valueA2).isEqualTo("falt");
        assertThat(valueB2).isEqualTo("mbAlt");
        verify(fieldCache, times(2)).get(ExampleObj2.class, "valueA");
        verify(methodCache, times(2)).get(ExampleObj2.class, "getValueB");
    }

    @Test
    public void shouldPutAndGetFromCache() throws Exception {
        // Given
        Cache cache = new Cache();

        // When
        AccessibleObject actual = cache.get(String.class, "string");

        // Then
        assertThat(actual).isNull();

        // Given - cache updated
        final Method toStringMethod = String.class.getMethod("toString");
        cache.put(String.class, "myToString", toStringMethod);

        // When
        actual = cache.get(String.class, "myToString");

        // Then
        assertThat(actual).isEqualTo(toStringMethod);
    }

    private static class ExampleObj {
        public String fieldA = "fa";
        private String methodA = "ma";
        private String methodB = "mb";

        public String getMethodA() {
            return methodA;
        }

        private String getMethodB() {
            return methodB;
        }

        public String deleteAll() throws IllegalAccessException {
            throw new IllegalAccessException("Should not invoke");
        }
    }

    private static class ExampleObj2 {
        public String valueA = "fa";
        public String valueAlt = "falt";
        private String valueB = "fb";
        private String valueC = "fc";

        private String getValueA() {
            return "mA";
        }

        public String getValueB() {
            return "mb";
        }

        public String getValueBAlt() {
            return "mbAlt";
        }

        private String getValueC() {
            return "mc";
        }

        private String isValueA() {
            return "isA";
        }

        public String isValueB() {
            return "isb";
        }

        public String isValueC() {
            return "isc";
        }
    }

    private static class ExampleObj3 {
        public String fieldA = "fa";
        private String fieldB = "fb";

        public ExampleObj3 setFieldB(final String fieldB) {
            this.fieldB = fieldB;
            return this;
        }
    }

    private static class ExampleNestedObj1 {
        private ExampleObj nestedField = new ExampleObj();

        public ExampleObj getNestedField() {
            return nestedField;
        }

        public void setNestedField(final ExampleObj nestedField) {
            this.nestedField = nestedField;
        }
    }
}


