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

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.gov.gchq.koryphe.composite.Composite;
import java.util.List;
import java.util.function.Predicate;

/**
 * A {@link Composite} {@link Predicate} that applies each predicate in turn, returning true if all Predicates are met,
 * otherwise false.
 *
 * @param <I> Input type
 * @param <C> Type of Predicate components
 */
public class PredicateComposite<I, C extends Predicate<I>> extends Composite<C> implements Predicate<I> {
    /**
     * Default - for serialisation.
     */
    public PredicateComposite() {
        super();
    }

    public PredicateComposite(final List<C> predicates) {
        super(predicates);
    }

    @JsonProperty("predicates")
    public List<C> getComponents() {
        return super.getComponents();
    }

    /**
     * Apply the predicate components in turn, returning false if any fail the test.
     * @param input Input value
     * @return True if all components pass, otherwise false.
     */
    @Override
    public boolean test(final I input) {
        for (final C predicate : components) {
            if (!predicate.test(input)) {
                return false;
            }
        }
        return true;
    }
}
