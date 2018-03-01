/*
 * Copyright 2017 Crown Copyright
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

package uk.gov.gchq.koryphe.predicate;

import org.junit.AssumptionViolatedException;
import org.junit.Test;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.io.IOException;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assume.assumeTrue;

public abstract class PredicateTest {
    protected abstract Predicate getInstance();

    protected abstract Class<? extends Predicate> getPredicateClass();

    @Test
    public abstract void shouldJsonSerialiseAndDeserialise() throws IOException;

    protected String serialise(Object object) throws IOException {
        return JsonSerialiser.serialise(object);
    }

    protected Predicate deserialise(String json) throws IOException {
        return JsonSerialiser.deserialise(json, getPredicateClass());
    }

    @Test
    public void shouldEquals() {
        // Given
        final Predicate instance = getInstance();

        // When
        final Predicate other = getInstance();

        // Then
        assertEquals(instance, other);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void shouldEqualsWhenSameObject() {
        // Given
        final Predicate instance = getInstance();

        // Then
        assertEquals(instance, instance);
        assertEquals(instance.hashCode(), instance.hashCode());
    }

    @Test
    public void shouldNotEqualsWhenDifferentClass() {
        // Given
        final Predicate instance = getInstance();

        // When
        final Object other = new Object();

        // Then
        assertNotEquals(instance, other);
        assertNotEquals(instance.hashCode(), other.hashCode());
    }

    @Test
    public void shouldNotEqualsNull() {
        // Given
        final Predicate instance = getInstance();

        // Then
        assertNotEquals(instance, null);
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final Predicate instance = getInstance();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        if (null == annotation || null == annotation.value()) {
            throw new AssumptionViolatedException("Missing Since annotation on class " + instance.getClass().getName());
        }
        assumeTrue(annotation.value() + " is not a valid value string.",
                VersionUtil.validateVersionString(annotation.value()));
    }
}
