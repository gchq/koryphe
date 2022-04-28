/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.function;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.signature.SignatureAssert;
import uk.gov.gchq.koryphe.util.EqualityTest;
import uk.gov.gchq.koryphe.util.SummaryUtil;
import uk.gov.gchq.koryphe.util.VersionUtil;

import java.io.IOException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class FunctionTest<T extends Function> extends EqualityTest<T> {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        return mapper;
    }

    protected Class<? extends Function> getFunctionClass() {
        return getInstance().getClass();
    }

    protected abstract Class[] getExpectedSignatureInputClasses();

    protected abstract Class[] getExpectedSignatureOutputClasses();

    public abstract void shouldJsonSerialiseAndDeserialise() throws IOException;

    protected String serialise(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    protected Function deserialise(String json) throws IOException {
        return MAPPER.readValue(json, getFunctionClass());
    }

    @Test
    public void shouldHaveSinceAnnotation() {
        // Given
        final Function instance = getInstance();

        // When
        final Since annotation = instance.getClass().getAnnotation(Since.class);

        // Then
        assertThat(annotation)
                .isNotNull()
                .withFailMessage("Missing Since annotation on class %s", instance.getClass().getName());
        assertThat(annotation.value())
                .isNotNull()
                .withFailMessage("Missing Since annotation on class %s", instance.getClass().getName());
        assertThat(VersionUtil.validateVersionString(annotation.value()))
                .isTrue()
                .withFailMessage("%s is not a valid value string.", annotation.value());
    }

    @Test
    public void shouldHaveSummaryAnnotation() {
        // Given
        final Function instance = getInstance();

        // When
        final Summary annotation = instance.getClass().getAnnotation(Summary.class);

        // Then
        assertThat(annotation)
                .isNotNull()
                .withFailMessage("Missing Summary annotation on class %s", instance.getClass().getName());
        assertThat(annotation.value())
                .isNotNull()
                .withFailMessage("Missing Summary annotation on class %s", instance.getClass().getName());
        assertThat(SummaryUtil.validateSummaryString(annotation.value()))
                .isTrue()
                .withFailMessage("%s is not a valid value string.", annotation.value());
    }

    @Test
    public void shouldGenerateExpectedInputSignature() {
        // Given
        final Function function = getInstance();

        // When
        final Signature signature = Signature.getInputSignature(function);

        // Then
        SignatureAssert.assertThat(signature).isAssignableFrom(getExpectedSignatureInputClasses());
    }

    @Test
    public void shouldGenerateExpectedOutputSignature() {
        // Given
        final Function function = getInstance();

        // When
        final Signature signature = Signature.getOutputSignature(function);

        // Then
        SignatureAssert.assertThat(signature).isAssignableFrom(getExpectedSignatureOutputClasses());
    }
}
