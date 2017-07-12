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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.Before;
import org.junit.Test;
import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SetMergeTest extends BinaryOperatorTest {
    private Set<Integer> state;

    @Before
    public void before() {
        state = null;
    }

    @Test
    public void testMerge() {
        // Given
        final SetMerge<Integer> setMerge = getInstance();
        Set initialSet = new HashSet();
        initialSet.add(1);

        // When 1
        state = setMerge.apply(null, initialSet);

        // Then 1
        assertTrue(state instanceof Set);
        assertEquals(1, state.size());

        Set set = new HashSet<>();
        set.add(2);

        // When 2
        state = setMerge.apply(state, set);

        // Then 2
        assertTrue(state instanceof Set);
        assertEquals(2, state.size());

        set.clear();
        set.add(2);

        // When 3
        state = setMerge.apply(state, set);

        // Then 3
        assertTrue(state instanceof Set);
        assertEquals(2, state.size());

    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final SetMerge mapMerge = getInstance();

        // When 1
        final String json = JsonSerialiser.serialise(mapMerge);

        JsonSerialiser.assertEquals(String.format("{\"class\":\"uk.gov.gchq.koryphe.impl.binaryoperator.SetMerge\"}"), json);

        // When 2
        final SetMerge deserialisedAggregator = JsonSerialiser.deserialise(json, SetMerge.class);

        // Then 2
        assertNotNull(deserialisedAggregator);
    }

    @Override
    protected SetMerge getInstance() {
        return new SetMerge();
    }

    @Override
    protected Class<SetMerge> getFunctionClass() {
        return SetMerge.class;
    }
}