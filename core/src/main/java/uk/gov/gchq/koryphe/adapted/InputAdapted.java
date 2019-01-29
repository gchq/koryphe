/*
 * Copyright 2017-2019 Crown Copyright
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

package uk.gov.gchq.koryphe.adapted;

import java.util.function.Function;

/**
 * An <code>InputAdapted</code> class is one that applies a function to it's inputs, allowing it to be applied to
 * different input types. It can also be used to apply a function to values contained within a complex object.
 *
 * For example, if we wanted to apply the existing predicate <code>(i -&gt; i != null)</code> to validate the existence of
 * member variable <code>value</code> of a context object (co), we could use an <code>InputAdapted</code> predicate with
 * the input adapter function <code>(co -&gt; co.getValue())</code>.
 *
 * @param <I> Input type
 * @param <AI> Type adapted from input
 */
public class InputAdapted<I, AI> {
    protected Function<I, AI> inputAdapter;

    public Function<I, AI> getInputAdapter() {
        return inputAdapter;
    }

    public void setInputAdapter(final Function<I, AI> inputAdapter) {
        this.inputAdapter = inputAdapter;
    }

    /**
     * Get the adapted input value by applying the <code>inputAdapter</code> to an input value.
     *
     * @param input Input to adapt
     * @return Adapted input
     */
    protected AI adaptInput(final I input) {
        return inputAdapter == null ? (AI) input : inputAdapter.apply(input);
    }
}
