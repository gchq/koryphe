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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReflectiveTupleTest {

    public static final String FIELD_X = "fieldX";
    public static final String FIELD_B = "fieldB";
    public static final String FIELD_A = "fieldA";
    public static final String METHOD_B = "methodB";
    ReflectiveTuple testObj;

    @Before
    public void setUp() throws Exception {
        testObj = new ReflectiveTuple(new ExampleObj());
    }


    @Test
    public void shouldNotFindMissingField() throws Exception {
        try {
            testObj.get(FIELD_X);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, FIELD_X), e.getMessage());
        }
    }

    @Test
    public void shouldNotFindMissingMethod() throws Exception {
        try {
            testObj.get("get" + FIELD_X);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, "get" + FIELD_X), e.getMessage());
        }
    }

    @Test
    public void shouldFindPublicField() throws Exception {
        assertEquals("fa", testObj.get(FIELD_A));
    }

    @Test
    public void shouldNotFindPrivateField() throws Exception {
        try {
            testObj.get(FIELD_B);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, FIELD_B), e.getMessage());
        }
    }

    @Test
    public void shouldFindPublicMethod() throws Exception {
        assertEquals("ma", testObj.get("methodA"));
    }

    @Test
    public void shouldNotFindPrivateMethod() throws Exception {
        try {
            testObj.get(METHOD_B);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, METHOD_B), e.getMessage());
        }
    }

    @Test
    public void shouldSuggestDifferentFieldFont() throws Exception {
        try {
            testObj.get(FIELD_A.toUpperCase());
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertTrue(e.toString(), e.getMessage().contains(String.format(ReflectiveTuple.SELECTION_FOUND_WITH_DIFFERENT_CASE, FIELD_A.toUpperCase(), FIELD_A)));
        }
    }

    @Test
    public void shouldSuggestDifferentMethodFont() throws Exception {
        try {
            testObj.get("GETMETHODA");
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertTrue(e.toString(), e.getMessage().contains(String.format(ReflectiveTuple.SELECTION_FOUND_WITH_DIFFERENT_CASE, "GETMETHODA", "getMethodA")));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotPut() throws Exception {
        testObj.put("", "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotValues() throws Exception {
        testObj.values();
    }

    @Test
    public void shouldNotCallQuestionableMethods() throws Exception {
        final String methodB = "deleteAll";
        try {
            testObj.get(methodB);
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertFalse("deleteAll() should not be invoked", e.getMessage().contains(String.format(ReflectiveTuple.SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION, methodB)));
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, methodB), e.getMessage());
        }
    }

    @Test
    public void shouldGetInOrderFieldMethodIs() throws Exception {
        testObj = new ReflectiveTuple(new ExampleObj2());
        assertEquals("fa", testObj.get("valueA"));
        assertEquals("mb", testObj.get("valueB"));
        assertEquals("isc", testObj.get("valueC"));
    }

    @Test
    public void shouldUseCache() throws Exception {
        final Cache field = mock(Cache.class);
        final Cache method = mock(Cache.class);
        when(field.get(ExampleObj2.class, "valueA")).thenReturn(null);
        when(method.get(ExampleObj2.class, "getValueB")).thenReturn(null);
        testObj = new ReflectiveTuple(new ExampleObj2(), field, method);
        assertEquals("fa", testObj.get("valueA"));
        assertEquals("mb", testObj.get("valueB"));
        when(field.get(ExampleObj2.class, "valueA")).thenReturn(ExampleObj2.class.getField("valueAlt"));
        when(method.get(ExampleObj2.class, "getValueB")).thenReturn(ExampleObj2.class.getMethod("getValueBAlt"));
        assertEquals("falt", testObj.get("valueA"));
        assertEquals("mbAlt", testObj.get("valueB"));
        verify(field, atLeast(1)).put(Matchers.eq(ExampleObj2.class), Matchers.eq("valueA"), Matchers.any(Field.class));
        verify(method, atLeast(1)).put(Matchers.eq(ExampleObj2.class), Matchers.eq("getValueB"), Matchers.any(Method.class));
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

        public String getValueA() {
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

        public String isValueA() {
            return "isA";
        }

        public String isValueB() {
            return "isb";
        }

        public String isValueC() {
            return "isc";
        }
    }

}


