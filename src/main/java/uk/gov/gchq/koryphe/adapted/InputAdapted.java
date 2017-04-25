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
