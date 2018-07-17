/*
 * Copyright 2018 Crown Copyright
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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.gov.gchq.koryphe.tuple.ReflectiveTuple.Cache;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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

    @Before
    public void setUp() throws Exception {
        testObj = new ReflectiveTuple(new ExampleObj());
    }

    @Test
    public void shouldNotFindMissingField() throws Exception {
        try {
            //when
            testObj.get(FIELD_X);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            //then
            assertEquals(String.format(SELECTION_S_DOES_NOT_EXIST, FIELD_X), e.getMessage());
        }
    }

    @Test
    public void shouldNotFindMissingMethod() throws Exception {
        try {
            //when
            testObj.get("get" + FIELD_X);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            //then
            assertEquals(String.format(SELECTION_S_DOES_NOT_EXIST, "get" + FIELD_X), e.getMessage());
        }
    }

    @Test
    public void shouldFindPublicField() throws Exception {
        assertEquals("fa", testObj.get(FIELD_A));
    }

    @Test
    public void shouldFindPublicFieldWithDot() throws Exception {
        assertEquals("fa", testObj.get(FIELD_A + "."));
    }

    @Test
    public void shouldGetNestedField() throws Exception {
        // Given
        testObj = new ReflectiveTuple(new ExampleNestedObj1());

        // When/Then
        assertEquals("fa", testObj.get(NESTED_FIELD));
    }

    @Test
    public void shouldNotFindPrivateField() throws Exception {
        try {
            //when
            testObj.get(FIELD_B);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            //then
            assertEquals(String.format(SELECTION_S_DOES_NOT_EXIST, FIELD_B), e.getMessage());
        }
    }

    @Test
    public void shouldFindPublicMethod() throws Exception {
        assertEquals("ma", testObj.get("methodA"));
    }

    @Test
    public void shouldNotFindPrivateMethod() throws Exception {
        try {
            //when
            testObj.get(METHOD_B);
            fail("Exception expected");
        } catch (final RuntimeException e) {
            //then
            assertEquals(String.format(SELECTION_S_DOES_NOT_EXIST, METHOD_B), e.getMessage());
        }
    }

    @Test
    public void shouldPutField() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        //when
        testObj.put("fieldA", "changed");
        //then
        assertEquals("changed", record.fieldA);
    }


    @Test
    public void shouldNotPutFieldWithWrongParam() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        try {
            //when
            testObj.put("fieldA", 1);
            fail("exception expected");
        } catch (final IllegalArgumentException e) {
            //then
            assertEquals(String.format(ReflectiveTuple.ERROR_WRONG_PARAM, "field", "fieldA", String.class, Integer.class.getSimpleName()), e.getMessage());
        }
    }

    @Test
    public void shouldPutMethod() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        //when
        testObj.put("fieldB", "changed");
        //then
        assertEquals("changed", record.fieldB);
    }

    @Test
    public void shouldPutMethodWithDot() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        //when
        testObj.put("fieldB.", "changed");
        //then
        assertEquals("changed", record.fieldB);
    }

    @Test
    public void shouldNotPutMethodWithWrongParam() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        try {
            //when
            testObj.put("fieldB", 1);
            fail("exception expected");
        } catch (final IllegalArgumentException e) {
            //then
            assertEquals(String.format(ReflectiveTuple.ERROR_WRONG_PARAM, "method", "setFieldB", Arrays.asList(String.class), Integer.class.getSimpleName()), e.getMessage());
        }
    }

    @Test
    public void shouldNotPutMethod() throws Exception {
        //given
        final ExampleObj3 record = new ExampleObj3();
        testObj = new ReflectiveTuple(record);
        try {
            //when
            testObj.put("fieldC", 1);
            fail("exception expected");
        } catch (final RuntimeException e) {
            //then
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, "fieldC"), e.getMessage());
        }
    }

    @Test
    public void shouldPutNestedField() throws Exception {
        //given
        final ExampleNestedObj1 record = new ExampleNestedObj1();
        testObj = new ReflectiveTuple(record);

        //when
        testObj.put(NESTED_FIELD, "changed");

        //then
        assertEquals("changed", record.getNestedField().fieldA);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotValues() throws Exception {
        testObj.values();
    }

    @Test
    public void shouldGetInOrderOfMethodGetIs() throws Exception {
        //given
        testObj = new ReflectiveTuple(new ExampleObj2());
        //then
        assertEquals("fa", testObj.get("valueA"));
        assertEquals("mb", testObj.get("valueB"));
        assertEquals("isc", testObj.get("valueC"));
    }

    @Test
    public void shouldUseCache() throws Exception {
        //given
        final Cache<Field> fieldCache = mock(Cache.class);
        final Cache<Method> methodCache = mock(Cache.class);
        when(fieldCache.get(ExampleObj2.class, "valueA")).thenReturn(null);
        when(methodCache.get(ExampleObj2.class, "getValueB")).thenReturn(null);

        testObj = new ReflectiveTuple(new ExampleObj2(), fieldCache, methodCache);

        //when
        final Object valueA1 = testObj.get("valueA");
        final Object valueB1 = testObj.get("valueB");

        //then
        assertEquals("fa", valueA1);
        assertEquals("mb", valueB1);
        verify(fieldCache, times(1)).put(eq(ExampleObj2.class), eq("valueA"), any(Field.class));
        verify(methodCache, times(1)).put(eq(ExampleObj2.class), eq("getValueB"), any(Method.class));

        //given mock set to alternative values.
        when(fieldCache.get(ExampleObj2.class, "valueA")).thenReturn(ExampleObj2.class.getField("valueAlt"));
        when(methodCache.get(ExampleObj2.class, "getValueB")).thenReturn(ExampleObj2.class.getMethod("getValueBAlt"));

        //when
        final Object valueA2 = testObj.get("valueA");
        final Object valueB2 = testObj.get("valueB");

        //then
        assertEquals("falt", valueA2);
        assertEquals("mbAlt", valueB2);
        verify(fieldCache, times(2)).get(ExampleObj2.class, "valueA");
        verify(methodCache, times(2)).get(ExampleObj2.class, "getValueB");
    }

    @Test
    public void shouldPutAndGetFromCache() throws Exception {
        //given
        Cache cache = new Cache();

        //when
        AccessibleObject actual = cache.get(String.class, "string");
        //then
        Assert.assertNull(actual);


        //given cache updated
        final Method toStringMethod = String.class.getMethod("toString");
        cache.put(String.class, "myToString", toStringMethod);

        //when
        actual = cache.get(String.class, "myToString");
        //then
        Assert.assertEquals(toStringMethod, actual);
    }

    private class ExampleObj {
        public String fieldA = "fa";
        private String fieldB = "fb";
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

    private class ExampleObj2 {
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

    private class ExampleObj3 {
        public String fieldA = "fa";
        private String fieldB = "fb";
        private String fieldC = "fc";

        public ExampleObj3 setFieldB(final String fieldB) {
            this.fieldB = fieldB;
            return this;
        }

        private ExampleObj3 fieldC(final String fieldC) {
            this.fieldC = fieldC;
            return this;
        }
    }

    private class ExampleNestedObj1 {
        private ExampleObj nestedField = new ExampleObj();

        public ExampleObj getNestedField() {
            return nestedField;
        }

        public void setNestedField(final ExampleObj nestedField) {
            this.nestedField = nestedField;
        }
    }
}


