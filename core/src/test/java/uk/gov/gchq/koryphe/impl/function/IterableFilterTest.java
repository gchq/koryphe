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
package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Lists;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IterableFilterTest extends FunctionTest {

    @Test
    public void shouldApplyKeyPredicate() {
        // Given
        final List<Integer> items = Arrays.asList(1, 2, 3);

        final IterableFilter<Integer> predicate =
                new IterableFilter<Integer>()
                        .predicate(new IsMoreThan(1));

        // When
        final Iterable<Integer> result = predicate.apply(items);

        // Then
        assertEquals(Arrays.asList(2, 3), Lists.newArrayList(result));
    }

    @Override
    protected IterableFilter<Integer> getInstance() {
        return new IterableFilter<>(new IsMoreThan(1));
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return IterableFilter.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableFilter predicate = getInstance();

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.function.IterableFilter\"," +
                "\"predicate\":{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsMoreThan\",\"orEqualTo\":false,\"value\":1}" +
                "}", json);

        // When 2
        final IterableFilter deserialised = JsonSerialiser.deserialise(json, IterableFilter.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(1, ((IsMoreThan) deserialised.getPredicate()).getControlValue());
    }
}
