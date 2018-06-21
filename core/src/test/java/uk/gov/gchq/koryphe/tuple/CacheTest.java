package uk.gov.gchq.koryphe.tuple;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class CacheTest {
    Cache testObt = new Cache();

    @Test
    public void shouldPutAndGet() throws Exception {
        final AccessibleObject get = testObt.get(String.class, "string");
        Assert.assertNull(get);
        final Method toString = String.class.getMethod("toString");
        testObt.put(String.class, "string", toString);
        final AccessibleObject get2 = testObt.get(String.class, "string");
        Assert.assertEquals(toString, get2);
    }
}