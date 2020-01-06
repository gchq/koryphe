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

@Since("1.8.3")
@Summary("Prepends a string with the provided prefix.")
public class StringPrepend extends KorypheFunction<String, String> {

    private String prefix;

    public StringPrepend() {
    }

    public StringPrepend(final String prefix) {
        setPrefix(prefix);
    }

    @Override
    public String apply(final String input) {
        if (null == input) {
            return null;
        }
        return prefix + input;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(final String prefix) {
        if (null == prefix) {
            this.prefix = StringUtils.EMPTY;
        } else {
            this.prefix = prefix;
        }
    }
}
