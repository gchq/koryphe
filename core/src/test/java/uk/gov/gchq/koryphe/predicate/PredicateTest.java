/*
 * Copyright 2017-2021 Crown Copyright
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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.SummaryUtil;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.io.IOException;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class PredicateTest<T extends Predicate> extends EqualityTest<T> {

    protected Class<? extends Predicate> getPredicateClass() {
        return getInstance().getClass();
    }

    @Test
    public abstract void shouldJsonSerialiseAndDeserialise() throws IOException;

    protected String serialise(Object object) throws IOException {
        return JsonSerialiser.serialise(object);
    }

    protected Predicate deserialise(String json) throws IOException {
        return JsonSerialiser.deserialise(json, getPredicateClass());
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final Predicate instance = getInstance();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        assertThat(annotation)
                .withFailMessage("Missing Since annotation on class %s", instance.getClass().getName())
                .isNotNull();
        assertThat(annotation)
                .withFailMessage("Missing Since annotation on class %s", instance.getClass().getName())
                .isNotNull();
        assertThat(VersionUtil.validateVersionString(annotation.value()))
                .withFailMessage("%s is not a valid value string.", annotation.value())
                .isTrue();
    }

    @Test
    public void shouldHaveSummaryAnnotation() {
        // Given
        final Predicate instance = getInstance();

        // When
        final Summary annotation = instance.getClass().getAnnotation(Summary.class);

        // Then
        assertThat(annotation)
                .withFailMessage("Missing Summary annotation on class %s", instance.getClass().getName())
                .isNotNull();
        assertThat(annotation)
                .withFailMessage("Missing Summary annotation on class %s", instance.getClass().getName())
                .isNotNull();
        assertThat(SummaryUtil.validateSummaryString(annotation.value()))
                .withFailMessage("%s is not a valid value string.", annotation.value())
                .isTrue();
    }
}
