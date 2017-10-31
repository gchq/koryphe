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
package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Iterables;

import uk.gov.gchq.koryphe.function.KorypheFunction;

/**
 * A {@code NthItem} is a {@link KorypheFunction} that returns an item based on user selection,
 * from an {@link Iterable} of items of type T
 *
 * @param <T> the type of objects in the iterable
 */
public class NthItem<T> extends KorypheFunction<Iterable<T>, T> {

    private int selection;

    public NthItem() {
        // empty
    }

    public NthItem(final int selection) {
        this.selection = selection;
    }

    @Override
    public T apply(final Iterable<T> input) {
        try {
            return Iterables.get(input, selection);
        } finally {
            if (input instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) input).close();
                } catch (final Exception e) {
                    System.out.println(e.getMessage());
                    // Ignore exception
                }
            }
        }
    }

    public void setSelection(final int selection) {
        this.selection = selection;
    }

    public int getSelection() {
        return selection;
    }
}
