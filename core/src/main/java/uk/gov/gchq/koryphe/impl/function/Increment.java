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

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import uk.gov.gchq.koryphe.Since;
import uk.gov.gchq.koryphe.Summary;
import uk.gov.gchq.koryphe.function.KorypheFunction;

import java.util.Arrays;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * A <code>Increment</code> is a {@link java.util.function.Function} that takes
 * a number and adds a given number to it.
 * Note the output number type will match the increment type.
 * So if you start with an input value of int 1 and then increment it with a double 2.0.
 * The result will be a double 3.0.
 */
@Since("1.8.0")
@Summary("Adds a given number to the input")
public class Increment extends KorypheFunction<Number, Number> {
    private Number increment;
    private Type type;

    public Increment() {
    }

    public Increment(final Number increment) {
        setIncrement(increment);
    }

    @Override
    public Number apply(final Number input) {
        if (isNull(input)) {
            return increment;
        }

        if (isNull(increment) || isNull(type)) {
            return input;
        }

        Number result;
        switch (type) {
            case INTEGER:
                result = ((Integer) increment) + input.intValue();
                result = result.intValue();
                break;
            case LONG:
                result = ((Long) increment) + input.longValue();
                result = result.longValue();
                break;
            case SHORT:
                result = ((Short) increment) + input.shortValue();
                result = result.shortValue();
                break;
            case DOUBLE:
                result = ((Double) increment) + input.doubleValue();
                result = result.doubleValue();
                break;
            case FLOAT:
                result = ((Float) increment) + input.floatValue();
                result = result.floatValue();
                break;
            default:
                throw new IllegalArgumentException("Unrecognised Number type: " + increment.getClass()
                        + ". Allowed types: " + Arrays.toString(Type.values()));
        }

        return result;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public Number getIncrement() {
        return increment;
    }

    public void setIncrement(final Number increment) {
        this.increment = increment;
        if (nonNull(this.increment)) {
            if (increment instanceof Integer) {
                type = Type.INTEGER;
            } else if (increment instanceof Long) {
                type = Type.LONG;
            } else if (increment instanceof Short) {
                type = Type.SHORT;
            } else if (increment instanceof Double) {
                type = Type.DOUBLE;
            } else if (increment instanceof Float) {
                type = Type.FLOAT;
            } else {
                throw new IllegalArgumentException("Unrecognised Number type: " + increment.getClass()
                        + ". Allowed types: " + Arrays.toString(Type.values()));
            }
        } else {
            type = null;
        }
    }

    private enum Type {
        INTEGER,
        LONG,
        SHORT,
        DOUBLE,
        FLOAT
    }
}
