package uk.gov.gchq.koryphe.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * A TestClass which checks for equality and inequality using both the Equals and Hashcode methods of an object
 * @param <T> The Class of object that is being tested.
 */
public abstract class EqualityTest<T> {

    /**
     * Provides a new instance to test equality against.
     * @return an instance
     */
    protected abstract T getInstance();


    /**
     * Provides an iterable of instances which should not equal the instance provided
     * by {@code getInstance()}. If the instance does not have any variety (ie it has
     * no fields) then null should be returned.
     * @return an iterable of instances which are different to that provided by
     * {@code getInstance()}
     */
    protected abstract Iterable<T> getDifferentInstancesOrNull();

    @Test
    public void shouldEqualsItself() {
        // Given
        T instance = getInstance();

        // When
        T itself = instance; // only added this line to make the test explicit and read better

        // Then
        assertEquals(instance, itself);
    }

    @Test
    public void shouldHaveTheSameHashcodeAsItself() {
        // Given
        T instance = getInstance();

        // When
        T itself = instance; // only added this line to make the test explicit and read better

        // Then
        assertEquals(instance.hashCode(), itself.hashCode());
    }

    @Test
    public void shouldEqualsTheSameObject() {
        // Given
        T instance = getInstance();

        // When
        T sameObject = getInstance();

        // Then
        assertEquals(instance, sameObject);
    }

    @Test
    public void shouldHaveTheSameHashcodeAsTheSameObject() {
        // Given
        T instance = getInstance();

        // When
        T sameObject = getInstance();

        // Then
        assertEquals(instance.hashCode(), sameObject.hashCode());
    }

    @Test
    public void shouldNotEqualsNull() {
        // Given
        T instance = getInstance();

        // When
        T nullObject = null; // only added this line to make the test explicit and read better

        // Then
        assertNotEquals(instance, nullObject);
    }

    @Test
    public void shouldNotEqualsDifferentClass() {
        // Given
        T instance = getInstance();

        // When
        DifferentClass differentObject = new DifferentClass(); // only added this line to make the test explicit and read better

        // Then
        assertNotEquals(instance, differentObject);
    }

    @Test
    public void shouldNotEqualDifferentInstances() {
        // Given
        T instance = getInstance();
        Iterable<T> alternativeInstances = getDifferentInstancesOrNull();

        // When
        if (alternativeInstances == null) {
            return;
        }

        // Then
        for (T alternativeInstance : alternativeInstances) {
            assertNotEquals(instance, alternativeInstance);
        }
    }

    @Test
    public void shouldHaveDifferentHashcodesToDifferentInstances() {
        // Given
        T instance = getInstance();
        Iterable<T> alternativeInstances = getDifferentInstancesOrNull();

        // When
        if (alternativeInstances == null) {
            return;
        }

        // Then
        for (T alternativeInstance : alternativeInstances) {
            assertNotEquals(instance.hashCode(), alternativeInstance.hashCode());
        }
    }

    private static class DifferentClass {
        // Used for testing equality with different classes
    }

}
