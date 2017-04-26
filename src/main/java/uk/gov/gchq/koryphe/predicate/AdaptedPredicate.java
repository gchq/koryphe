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

package uk.gov.gchq.koryphe.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.gov.gchq.koryphe.adapted.InputAdapted;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An {@link InputAdapted} {@link Predicate}.
 *
 * @param <I> Input type
 * @param <PI> Adapted input type for predicate
 */
public class AdaptedPredicate<I, PI> extends InputAdapted<I, PI> implements Predicate<I> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    protected Predicate<PI> predicate;

    /**
     * Default - for serialisation.
     */
    public AdaptedPredicate() {
    }

    public AdaptedPredicate(final Function<I, PI> inputAdapter, final Predicate<PI> predicate) {
        setInputAdapter(inputAdapter);
        setPredicate(predicate);
    }

    /**
     * Apply the Predicate by adapting the input.
     *
     * @param input Input to adapt and apply predicate to
     * @return Predicate result
     */
    @Override
    public boolean test(final I input) {
        return null == predicate || predicate.test(adaptInput(input));
    }

    public Predicate<PI> getPredicate() {
        return predicate;
    }

    public void setPredicate(final Predicate<PI> predicate) {
        this.predicate = predicate;
    }
}
