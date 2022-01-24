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
package uk.gov.gchq.koryphe.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public final class CustomObj implements Comparable<CustomObj> {
    public final static String value = "1";

    @JsonGetter
    public String getValue() {
        return value;
    }

    @JsonSetter
    public void setValue(final String value) {
        // Do nothing
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CustomObj customObj = (CustomObj) o;

        return value != null ? value.equals(customObj.value) : customObj.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CustomObj{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public int compareTo(final CustomObj o) {
        return 0;
    }
}
