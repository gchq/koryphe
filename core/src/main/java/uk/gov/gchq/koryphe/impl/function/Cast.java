/*
 * Copyright 2018-2019 Crown Copyright
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

/**
 * A <code>Cast</code> is a {@link java.util.function.Function} that takes
 * an input class and casts it to an output class.
 * <p>
 * The resulting object is what is returned from the method.
 */
@Since("1.5.0")
@Summary("Casts input class to output class")
public class Cast<I, O> extends KorypheFunction<I, O> {
    private Class<O> outputClass;

    public Cast() {
    }

    public Cast(final Class<O> outputClass) {
        this.outputClass = outputClass;
    }

    @Override
    public O apply(final I value) {
        return outputClass.cast(value);
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public void setOutputClass(final Class<O> outputClass) {
        this.outputClass = outputClass;
    }
}
