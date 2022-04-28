/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(instance).isEqualTo(itself);
    }

    @Test
    public void shouldHaveTheSameHashcodeAsItself() {
        // Given
        T instance = getInstance();

        // When
        T itself = instance; // only added this line to make the test explicit and read better

        // Then
        assertThat(instance.hashCode()).isEqualTo(itself.hashCode());
    }

    @Test
    public void shouldEqualsTheSameObject() {
        // Given
        T instance = getInstance();

        // When
        T sameObject = getInstance();

        // Then
        assertThat(instance).isEqualTo(sameObject);
    }

    @Test
    public void shouldHaveTheSameHashcodeAsTheSameObject() {
        // Given
        T instance = getInstance();

        // When
        T sameObject = getInstance();

        // Then
        assertThat(instance.hashCode()).isEqualTo(sameObject.hashCode());

    }

    @Test
    public void shouldNotEqualsNull() {
        // Given
        T instance = getInstance();

        // When
        T nullObject = null; // only added this line to make the test explicit and read better

        // Then
        assertThat(instance).isNotEqualTo(nullObject);
    }

    @Test
    public void shouldNotEqualsDifferentClass() {
        // Given
        T instance = getInstance();

        // When
        DifferentClass differentObject = new DifferentClass(); // only added this line to make the test explicit and read better

        // Then
        assertThat(instance).isNotEqualTo(differentObject);
    }

    @Test
    public void shouldNotEqualDifferentInstances() {
        // Given
        T instance = getInstance();
        Iterable<T> alternativeInstances = getDifferentInstancesOrNull();

        // When
        if (null == alternativeInstances) {
            return;
        }

        // Then
        for (T alternativeInstance : alternativeInstances) {
            assertThat(alternativeInstance).isNotEqualTo(instance);
        }
    }

    @Test
    public void shouldHaveDifferentHashcodesToDifferentInstances() {
        // Given
        T instance = getInstance();
        Iterable<T> alternativeInstances = getDifferentInstancesOrNull();

        // When
        if (null == alternativeInstances) {
            return;
        }

        // Then
        for (T alternativeInstance : alternativeInstances) {
            assertThat(alternativeInstance.hashCode()).isNotEqualTo(instance.hashCode());
        }
    }

    private static class DifferentClass {
        // Used for testing equality with different classes
    }

}
