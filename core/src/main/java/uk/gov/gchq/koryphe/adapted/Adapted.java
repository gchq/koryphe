/*
 * Copyright 2017-2020 Crown Copyright
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

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * An <code>Adapted</code> class is one that applies functions to it's inputs and outputs, allowing it to be applied
 * to different input and output types. It can also be used to apply a function to values contained within a complex
 * object.
 *
 * For example, if we wanted to apply the existing function <code>(i -&gt; i+1)</code> to increment a specific integer
 * member variable <code>count</code> of a context object (co), we could use an <code>Adapted</code> function with the
 * input adapter function <code>(co -&gt; co.getCount())</code> and the output adapter function
 * <code>((co, o) -&gt; co.setCount(o))</code>.
 *
 * The context type <code>C</code> is the type supplied to the output adapter to combine with the type <code>AO</code>
 * to produce the output type <code>O</code>. An <code>Adapted</code> {@link java.util.function.Function} might merge
 * it's output into the input type, as in the example above, while an <code>Adapted</code>
 * {@link java.util.function.BiFunction} would merge it's output into some state object.
 *
 * @param <I> Input type
 * @param <AI> Type adapted from input
 * @param <AO> Type to be adapted to output
 * @param <O> Output type
 * @param <C> Context type - either <code>I</code> or <code>O</code>, depending on context.
 */
public abstract class Adapted<I, AI, AO, O, C> extends InputAdapted<I, AI> {
    protected BiFunction<C, AO, O> outputAdapter;

    public BiFunction<C, AO, O> getOutputAdapter() {
        return outputAdapter;
    }

    public void setOutputAdapter(final BiFunction<C, AO, O> outputAdapter) {
        this.outputAdapter = outputAdapter;
    }

    public void setOutputAdapter(final Function<AO, O> outputAdapter) {
        setOutputAdapter((o, ao) -> outputAdapter.apply(ao));
    }

    /**
     * Get the adapted output value by applying the <code>outputAdapter</code> to an output value and a context object.
     *
     * @param output Output to adapt
     * @param into (optional) context to merge output into
     * @return Adapted output
     */
    protected O adaptOutput(final AO output, final C into) {
        return outputAdapter == null ? (O) output : outputAdapter.apply(into, output);
    }
}
