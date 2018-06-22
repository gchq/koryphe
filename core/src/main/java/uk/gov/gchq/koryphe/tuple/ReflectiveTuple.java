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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * This class wraps any object and treats it as a {@link Tuple} by using reflection to call fields or getMethods for the encapsulated object.
 * Reflection is expensive and this should be used for testing or as a temporary solution.
 */
public class ReflectiveTuple implements Tuple<String> {
    public static final String SELECTION_FOUND_WITH_DIFFERENT_CASE = "There is no public reflection found for invoking field/method name: %s, but a method was found with different case: %s";
    public static final String SELECTION_S_DOES_NOT_EXIST = "Selection: %s does not exist.";
    public static final String SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION = "selection: %s, exists but retrieving the value caused InvocationTargetException";
    private final Object item;
    private static final Cache cache = new Cache();

    /**
     * @param item object to wrap
     */
    public ReflectiveTuple(final Object item) {
        this.item = item;
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

    private Optional<Object> getSelection(final String trimmedReference) {
        Optional<Object> selection;
        try {
            //invoked value can be null
            selection = getFieldSelection(item, trimmedReference);
        } catch (final IllegalAccessException | NoSuchFieldException ignore1) {
            selection = getMethodSelection(item, trimmedReference);
        }
        return selection;
    }

    private Optional<Object> getFieldSelection(final Object item, final String reference) throws IllegalAccessException, NoSuchFieldException {
        final Field field = getField(item.getClass(), reference);
        //invoked value can be null
        return Optional.ofNullable(field.get(item));
    }

    private Optional<Object> getMethodSelection(final Object item, final String trimmedReference) {
        final Optional<Object> selection;
        try {
            selection = getMethodSelectionThrows(item, trimmedReference);
        } catch (final IllegalAccessException ignore) {
            throw new RuntimeException(String.format(SELECTION_S_DOES_NOT_EXIST, trimmedReference));
        } catch (final InvocationTargetException ignore) {
            throw new RuntimeException(String.format(SELECTION_EXISTS_CAUSED_INVOCATION_TARGET_EXCEPTION, trimmedReference));
        } catch (final NoSuchMethodException ignore) {
            throw processMissingSelectionException(item.getClass(), trimmedReference);
        }
        return selection;
    }

    private Optional<Object> getMethodSelectionThrows(final Object item, final String reference) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getMethod(item.getClass(), sanitiseGetMethodName(reference));
        //invoked value can be null
        return Optional.ofNullable(method.invoke(item));
    }

    private Field getField(final Class<?> aClass, final String reference) throws NoSuchFieldException {
        Field rtn;
        if (cache.contains(aClass, reference)) {
            AccessibleObject ao = cache.get(aClass, reference);
            if (ao instanceof Field) {
                rtn = ((Field) ao);
            } else {
                //There is method in cached, but a field is requested.
                rtn = aClass.getField(reference);
            }
        } else {
            rtn = aClass.getField(reference);
            cache.put(aClass, reference, rtn);
        }
        return rtn;
    }

    private Method getMethod(final Class<?> aClass, final String methodName) throws NoSuchMethodException {
        Method rtn;
        if (cache.contains(aClass, methodName)) {
            AccessibleObject ao = cache.get(aClass, methodName);
            if (ao instanceof Method) {
                rtn = ((Method) ao);
            } else {
                //There is field in cached, but a method is requested.
                rtn = aClass.getMethod(methodName);
            }
        } else {
            rtn = aClass.getMethod(methodName);
            cache.put(aClass, methodName, rtn);
        }
        return rtn;
    }

    private String sanitiseGetMethodName(final String reference) {
        return reference.toLowerCase(Locale.getDefault()).startsWith("get") ? reference : prefixGet(reference);
    }

    private String prefixGet(final String trim) {
        return "get" + Character.toUpperCase(trim.charAt(0)) + trim.substring(1);
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
                    continue;
                } catch (NoSuchMethodException e) {
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

    private static class Cache {
        Map<Class, Map<String, AccessibleObject>> cache = new HashMap<>();

        public boolean contains(final Class<? extends Object> itemClass, final String reference) {
            requireNonNull(itemClass);
            requireNonNull(reference);
            return cache.entrySet()
                    .stream()
                    .filter(e -> e.getKey().equals(itemClass))
                    .filter(e -> e.getValue().containsValue(reference))
                    .findAny()
                    .isPresent();
        }

        public AccessibleObject get(final Class<? extends Object> itemClass, final String reference) {
            requireNonNull(itemClass);
            requireNonNull(reference);
            final Optional<AccessibleObject> rtn = cache.entrySet()
                    .stream()
                    .filter(e -> e.getKey().equals(itemClass))
                    .filter(e -> e.getValue().containsValue(reference))
                    .map(e -> e.getValue().get(reference))
                    .findAny();

            return rtn.isPresent() ? rtn.get() : null;

        }

        public AccessibleObject put(final Class<? extends Object> itemClass, final String reference, final AccessibleObject accessibleObject) {
            cache.putIfAbsent(itemClass, new HashMap<>());
            final Map<String, AccessibleObject> accessibleObjectMap = cache.get(itemClass);
            return accessibleObjectMap.put(reference, accessibleObject);
        }
    }
}
