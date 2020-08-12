/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.EqualityTest;
import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.SummaryUtil;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.io.IOException;
import java.util.function.BinaryOperator;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public abstract class BinaryOperatorTest extends EqualityTest<BinaryOperator> {

    protected abstract Class<? extends BinaryOperator> getFunctionClass();

    @Test
    public abstract void shouldJsonSerialiseAndDeserialise() throws IOException;

    protected String serialise(Object object) throws IOException {
        return JsonSerialiser.serialise(object);
    }

    protected BinaryOperator deserialise(String json) throws IOException {
        return JsonSerialiser.deserialise(json, getFunctionClass());
    }

    @Test
    public void shouldNotEqualsNull() {
        // Given
        final BinaryOperator instance = getInstance();

        // Then
        assertNotEquals(instance, null);
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final BinaryOperator instance = getInstance();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        assertNotNull(annotation, "Missing Since annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Since annotation on class " + instance.getClass().getName());
        assumeTrue(VersionUtil.validateVersionString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }

    @Test
    public void shouldHaveSummaryAnnotation() {
        // Given
        final BinaryOperator instance = getInstance();

        // When
        final Summary annotation = instance.getClass().getAnnotation(Summary.class);

        // Then
        assertNotNull(annotation, "Missing Summary annotation on class " + instance.getClass().getName());
        assertNotNull(annotation.value(), "Missing Summary annotation on class " + instance.getClass().getName());
        assumeTrue(SummaryUtil.validateSummaryString(annotation.value()),
                annotation.value() + " is not a valid value string.");
    }
}
