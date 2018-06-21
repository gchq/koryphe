/*
 * Copyright 2018 Crown Copyright
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
package uk.gov.gchq.koryphe.tuple;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectiveTuple implements Tuple<String> {
    public static final String METHOD_FOUND_WITH_DIFFERENT_CASE = "There is no public reflection found for invoking field/method name: %s, but a method was found with different case: %s";
    public static final String SELECTION_S_DOES_NOT_EXIST = "Selection: %s does not exist.";
    public static final String SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION = "selection: %s, exists but retrieving the value caused InvocationTargetException";
    private final Object item;

    /**
     * @param item object to wrap
     */
    public ReflectiveTuple(final Object item) {
        this.item = item;
    }

    @Override
    public Object get(final String reference) {
        Object rtn;

        final String trimmedReference = reference.trim();
        try {
            //invoked value can be null
            rtn = getFieldSelection(item, trimmedReference);
        } catch (final IllegalAccessException | NoSuchFieldException ignore) {
            final Optional<Object> methodSelection = getMethodSelection(item, null, trimmedReference);

            if (methodSelection.isPresent()) {
                try {
                    rtn = methodSelection.get();
                } catch (final NoSuchElementException e) {
                    throw new IllegalStateException("A value should have been invoked/resolved.");
                }
            } else {
                //The invoked value was/is null.
                rtn = null;
            }
        }

        return rtn;
    }

    private Object getFieldSelection(final Object item, final String reference) throws IllegalAccessException, NoSuchFieldException {
        return item.getClass().getField(reference).get(item);
    }

    private Optional<Object> getMethodSelection(final Object item, final ReflectiveOperationException fieldExceptions, final String reference) {

        Optional<Object> methodSelectionValue;

        Optional<Method> method = getMethod(item, reference);
        if (method.isPresent()) {
            try {
                //invoked value can be null
                methodSelectionValue = Optional.ofNullable(method.get().invoke(item));
            } catch (final IllegalAccessException ignore) {
                throw new RuntimeException(String.format(SELECTION_S_DOES_NOT_EXIST, reference));
            } catch (final InvocationTargetException ignore) {
                throw new RuntimeException(String.format(SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION, reference));
            } catch (final NoSuchElementException e) {
                throw new IllegalStateException("Reflection returned a null method", e);
            }
        } else {
            throw new IllegalStateException("Reflected method should have been found or a NoSuchElementException should be thrown");
        }

        return methodSelectionValue;
    }

    private Optional<Method> getMethod(final Object item, final String reference) {
        Optional<Method> methodSelection;
        try {
            methodSelection = Optional.of(item.getClass().getMethod(reference));
        } catch (final NoSuchMethodException ignore) {
            try {
                methodSelection = Optional.of(item.getClass().getMethod(prefixGet(reference)));
            } catch (final NoSuchMethodException ignore2) {
                throw processMissingSelectionException(item.getClass(), reference);
            }
        }
        return methodSelection;
    }

    private String prefixGet(final String trim) {
        return "get" + Character.toUpperCase(trim.charAt(0)) + trim.substring(1);
    }

    private NoSuchElementException processMissingSelectionException(final Class<? extends Object> aClass, final String reference) {
        final String selectionLower = reference.toLowerCase();
        final String getSelectionLower = prefixGet(reference).toLowerCase();
        final Method[] declaredMethods = aClass.getMethods();
        final Field[] declaredFields = aClass.getFields();

        final HashSet<String> names = new HashSet<>();
        names.addAll(Arrays.asList(declaredFields).stream().map(Field::getName).collect(Collectors.toSet()));
        names.addAll(Arrays.asList(declaredMethods).stream().map(Method::getName).collect(Collectors.toSet()));

        String errorString = String.format(SELECTION_S_DOES_NOT_EXIST, reference);
        for (final String name : names) {
            final String lowerName = name.toLowerCase();
            if ((selectionLower.equals(lowerName) || getSelectionLower.equals(lowerName))) {
                errorString = String.format(METHOD_FOUND_WITH_DIFFERENT_CASE, reference, name);
                break;
            }
        }

        return new NoSuchElementException(errorString);

    }

    @Override
    public Iterable<Object> values() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public void put(final String reference, final Object value) {
        throw new UnsupportedOperationException("Not implemented.");
    }
}