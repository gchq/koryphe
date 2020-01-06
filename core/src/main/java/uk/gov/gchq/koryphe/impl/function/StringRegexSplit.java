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

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Since("1.8.2")
@Summary("Split a string using the provided regular expression.")
public class StringRegexSplit extends KorypheFunction<String, List<String>> {

    private String regex;

    public StringRegexSplit() {
    }

    public StringRegexSplit(final String regex) {
        this.regex = regex;
    }

    @Override
    public List<String> apply(final String input) {
        if (null == input) {
            return null;
        }

        final Pattern pattern = Pattern.compile(regex);
        return Arrays.asList(pattern.split(input));
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(final String regex) {
        this.regex = regex;
    }
}
