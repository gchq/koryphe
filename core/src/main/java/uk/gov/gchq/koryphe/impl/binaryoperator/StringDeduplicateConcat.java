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
package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.binaryoperator.KorypheBinaryOperator;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * A <code>StringDeduplicateConcat</code> is a {@link KorypheBinaryOperator} that takes in
 * {@link String}s, potentially with separators, and concatenates them together, omitting
 * any duplicate substrings.
 * The default separator is a comma, you can set a custom separator
 * using setSeparator(String).
 */
@Since("1.0.0")
public class StringDeduplicateConcat extends KorypheBinaryOperator<String> {

    private static final String DEFAULT_SEPARATOR = ",";
    private String separator = DEFAULT_SEPARATOR;
    private Pattern p = Pattern.compile(DEFAULT_SEPARATOR);

    @Override
    protected String _apply(final String a, final String b) {
        final Set<String> set = new LinkedHashSet<>();

        Collections.addAll(set, p.split(StringUtils.removeStart(a, separator)));
        Collections.addAll(set, p.split(StringUtils.removeStart(b, separator)));

        return StringUtils.join(set, separator);
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(final String separator) {
        this.separator = separator;
        p = Pattern.compile(separator);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final StringDeduplicateConcat that = (StringDeduplicateConcat) obj;
        return new EqualsBuilder()
                .append(separator, that.separator)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(separator)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("separator", separator)
                .toString();
    }
}
