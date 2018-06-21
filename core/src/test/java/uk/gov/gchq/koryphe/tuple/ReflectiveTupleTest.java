package uk.gov.gchq.koryphe.tuple;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        } catch (NoSuchElementException e) {
            assertEquals(String.format(ReflectiveTuple.SELECTION_S_DOES_NOT_EXIST, FIELD_X), e.getMessage());
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
        } catch (NoSuchElementException e) {
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
            assertTrue(e.toString(), e.getMessage().contains(String.format(ReflectiveTuple.METHOD_FOUND_WITH_DIFFERENT_CASE, FIELD_A.toUpperCase(), FIELD_A)));
        }
    }

    @Test
    public void shouldSuggestDifferentMethodFont() throws Exception {
        try {
            testObj.get("GETMETHODA");
            fail("Exception expected");
        } catch (RuntimeException e) {
            assertTrue(e.toString(), e.getMessage().contains(String.format(ReflectiveTuple.METHOD_FOUND_WITH_DIFFERENT_CASE, "GETMETHODA", "getMethodA")));
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
    }

}


