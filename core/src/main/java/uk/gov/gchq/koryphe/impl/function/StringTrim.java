/*
 * Copyright 2019 Crown Copyright
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

import org.apache.commons.lang3.StringUtils;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A {@code StringTrim} is a {@link java.util.function.Function} which takes a {@link String} and removes all leading
 * and trailing whitespace characters.
 */
@Since("1.8.3")
@Summary("Trims all whitespace around a string.")
public class StringTrim extends KorypheFunction<String, String> {

    public StringTrim() {
    }

    @Override
    public String apply(final String input) {
        // If null, StringUtils will return null.
        return StringUtils.trim(input);
    }
}
