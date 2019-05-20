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

import java.util.Date;

/**
 * A <code>CurrentDate</code> is a {@link java.util.function.Function} that
 * returns the current {@link Date}.
 * It takes no arguments.
 */
@Since("1.8.0")
@Summary("Returns the current date")
public class CurrentDate extends KorypheFunction<Object, Date> {
    @Override
    public Date apply(final Object ignored) {
        return new Date();
    }
}
