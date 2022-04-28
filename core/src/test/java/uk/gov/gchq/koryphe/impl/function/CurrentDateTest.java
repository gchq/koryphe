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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrentDateTest extends FunctionTest<CurrentDate> {

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Date.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        String json = "{ \"class\": \"uk.gov.gchq.koryphe.impl.function.CurrentDate\" }";
        CurrentDate currentDate = new CurrentDate();

        // When
        String serialised = JsonSerialiser.serialise(currentDate);
        CurrentDate deserialised = JsonSerialiser.deserialise(json, CurrentDate.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(currentDate);
    }

    @Override
    protected CurrentDate getInstance() {
        return new CurrentDate();
    }

    @Override
    protected Iterable<CurrentDate> getDifferentInstancesOrNull() {
        return null;
    }
}
