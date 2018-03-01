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

package uk.gov.gchq.koryphe.impl.predicate;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.predicate.KoryphePredicate;

import java.util.Map;
import java.util.function.Predicate;

/**
 * An <code>MapContainsPredicate</code> is a {@link Predicate} that checks
 * whether a {@link Map} contains a key that matches a given predicate.
 */
@Since("1.0.0")
public class MapContainsPredicate extends KoryphePredicate<Map> {
    private Predicate keyPredicate;

    public MapContainsPredicate() {
        // Required for serialisation
    }

    public MapContainsPredicate(final Predicate keyPredicate) {
        this.keyPredicate = keyPredicate;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
    public Predicate getKeyPredicate() {
        return keyPredicate;
    }

    public void setKeyPredicate(final Predicate keyPredicate) {
        this.keyPredicate = keyPredicate;
    }

    @Override
    public boolean test(final Map input) {
        if (null != input && null != keyPredicate) {
            for (final Object key : input.keySet()) {
                if (keyPredicate.test(key)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final MapContainsPredicate that = (MapContainsPredicate) obj;

        return new EqualsBuilder()
                .append(keyPredicate, that.keyPredicate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 37)
                .append(keyPredicate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("keyPredicate", keyPredicate)
                .toString();
    }
}
