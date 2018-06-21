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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * This class wraps any object and treats it as a {@link Tuple} by using reflection to call fields or getMethods for the encapsulated object.
 * Reflection is expensive and this should be used for testing or as a temporary solution.
 */
public class ReflectiveTuple implements Tuple<String> {
    public static final String SELECTION_FOUND_WITH_DIFFERENT_CASE = "There is no public reflection found for invoking field/method name: %s, but a method was found with different case: %s";
    public static final String SELECTION_S_DOES_NOT_EXIST = "Selection: %s does not exist.";
    public static final String SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION = "selection: %s, exists but retrieving the value caused InvocationTargetExcep2tion";
    public static final String S_SHOULD_NOT_BE_THROWN = "While processing %s %s should not be thrown.";
    private final Object item;
    private static final Cache FIELD = new Cache();
    private static final Cache METHOD = new Cache();
    private final Cache field;
    private final Cache method;

    /**
     * @param item object to wrap
     */
    public ReflectiveTuple(final Object item) {
        this(item, FIELD, METHOD);
    }

    public ReflectiveTuple(final Object item, final Cache field, final Cache method) {
        this.item = item;
        this.field = field;
        this.method = method;
    }

    @Override
    public Object get(final String reference) {
        Object rtn;

        Optional<Object> selection = getSelection(reference.trim());
        if (selection.isPresent()) {
            rtn = selection.get();
        } else {
            //The invoked value was resolved and it was/is null.
            rtn = null;
        }

        return rtn;
    }

    private Optional<Object> getSelection(final String reference) {
        Optional<Object> selection;
        try {
            selection = getFieldSelection(item, reference);
        } catch (final IllegalAccessException | NoSuchFieldException ignore1) {
            selection = getMethodSelection(item, reference);
        }
        return selection;
    }

    private Optional<Object> getFieldSelection(final Object item, final String reference) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getField(item.getClass(), reference);
        //invoked value can be null
        return Optional.ofNullable(field.get(item));
    }

    private Optional<Object> getMethodSelection(final Object item, final String reference) {
        final Optional<Object> selection;
        try {
            Method method = null;
            try {
                method = getMethod(item.getClass(), getPrefixRef("get", reference));
            } catch (final Exception e) {
                method = getMethod(item.getClass(), getPrefixRef("is", reference));
            }
            //invoked value can be null
            selection = Optional.ofNullable(method.invoke(item));
        } catch (final IllegalAccessException ignore) {
            throw new RuntimeException(String.format(SELECTION_S_DOES_NOT_EXIST, reference));
        } catch (final InvocationTargetException ignore) {
            throw new RuntimeException(String.format(SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION, reference));
        } catch (final NoSuchMethodException ignore) {
            throw processMissingSelectionException(item.getClass(), reference);
        }

        return selection;
    }

    private Field getField(final Class<?> aClass, final String reference) throws NoSuchFieldException {
        try {
            return (Field) getAccessibleObject(aClass, reference, true);
        } catch (final NoSuchMethodException e) {
            throw new IllegalStateException(String.format(S_SHOULD_NOT_BE_THROWN, "field", NoSuchMethodException.class.getSimpleName()));
        }
    }

    private Method getMethod(final Class<?> aClass, final String methodName) throws NoSuchMethodException {
        try {
            return (Method) getAccessibleObject(aClass, methodName, false);
        } catch (final NoSuchFieldException e) {
            throw new IllegalStateException(String.format(S_SHOULD_NOT_BE_THROWN, "method", NoSuchFieldException.class.getSimpleName()));
        }
    }

    private AccessibleObject getAccessibleObject(final Class<?> aClass, final String reference, final boolean isField) throws NoSuchFieldException, NoSuchMethodException {
        AccessibleObject rtn = isField ? field.get(aClass, reference) : method.get(aClass, reference);

        if (isNull(rtn)) {
            if (isField) {
                rtn = aClass.getField(reference);
                this.field.put(aClass, reference, rtn);
            } else {
                rtn = aClass.getMethod(reference);
                this.method.put(aClass, reference, rtn);
            }
        }
        return rtn;
    }

    private String getPrefixRef(final String prefix, final String reference) {
        final boolean startsWithPrefix = reference.toLowerCase(Locale.getDefault()).startsWith(prefix);
        return startsWithPrefix ? reference : prefix + Character.toUpperCase(reference.charAt(0)) + reference.substring(1);
    }

    private RuntimeException processMissingSelectionException(final Class<? extends Object> aClass, final String reference) {
        final Locale locale = Locale.getDefault();
        final String selectionLower = reference.toLowerCase(locale);
        final HashSet<String> allPublicReflectionNames = getAllPublicReflectionNames(aClass);

        String errorString = String.format(SELECTION_S_DOES_NOT_EXIST, reference);

        for (final String name : allPublicReflectionNames) {
            final String lowerName = name.toLowerCase(locale);
            if (selectionLower.equals(lowerName)) {
                try {
                    aClass.getMethod(reference);
                } catch (final NoSuchMethodException e) {
                    errorString = String.format(SELECTION_FOUND_WITH_DIFFERENT_CASE, reference, name);
                    break;
                }
            }
        }

        return new RuntimeException(errorString);

    }

    private HashSet<String> getAllPublicReflectionNames(final Class<? extends Object> aClass) {
        final HashSet<String> allPublicReflectionNames = new HashSet<>();
        allPublicReflectionNames.addAll(Arrays.asList(aClass.getFields()).stream().map(Field::getName).collect(Collectors.toSet()));
        allPublicReflectionNames.addAll(Arrays.asList(aClass.getMethods()).stream().map(Method::getName).collect(Collectors.toSet()));
        return allPublicReflectionNames;
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
