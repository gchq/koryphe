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

package uk.gov.gchq.koryphe.signature;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.function.WrappedBiFunction;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.lang.annotation.Annotation;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A <code>Signature</code> is the type metadata for the input or output of a {@link java.util.function.Function}.
 */
public abstract class Signature {
    private static final Logger LOGGER = LoggerFactory.getLogger(Signature.class);

    private static final boolean INPUT_SIGNATURE = true;
    private static final boolean OUTPUT_SIGNATURE = false;

    /**
     * Tests whether this <code>Signature</code> is compatible with the types supplied.
     *
     * @param arguments Class or Tuple of classes to test.
     * @return ValidationResult containing the isValid flag and errors messages.
     */
    public abstract ValidationResult assignable(Class<?>... arguments);

    public abstract Class[] getClasses();

    public abstract Integer getNumClasses();

    /**
     * Get the input signature of a predicate.
     *
     * @param predicate the predicate.
     * @return Input signature.
     */
    public static Signature getInputSignature(final Predicate predicate) {
        return createSignatureFromTypeVariable(predicate, Predicate.class, INPUT_SIGNATURE);
    }

    /**
     * Get the input signature of a function.
     *
     * @param function the function.
     * @return Input signature.
     */
    public static Signature getInputSignature(final Function function) {
        return createSignatureFromTypeVariable(function, Function.class, INPUT_SIGNATURE);
    }

    /**
     * Get the input signature of a BiFunction.
     *
     * @param function the BiFunction (second argument must be the same type as the output).
     * @param <F>      the type of the BiFunction
     * @param <I>      the first input type of the BiFunction
     * @param <O>      the second input type and output type of the BiFunction
     * @return Input signature
     */
    public static <F extends BiFunction<I, O, O>, I, O> Signature getInputSignature(final F function) {
        return createSignatureFromTypeVariable(function, BiFunction.class, INPUT_SIGNATURE);
    }

    /**
     * Get the output signature of a function.
     *
     * @param function Function.
     * @return Output signature.
     */
    public static Signature getOutputSignature(final Function function) {
        return createSignatureFromTypeVariable(function, Function.class, OUTPUT_SIGNATURE);
    }

    /**
     * Get the output signature of a function.
     *
     * @param function BiFunction.
     * @param <F>      the type of the BiFunction
     * @param <I>      the first input type of the BiFunction
     * @param <O>      the second input type and output type of the BiFunction
     * @return Output signature
     */
    public static <F extends BiFunction<I, O, O>, I, O> Signature getOutputSignature(final F function) {
        return createSignatureFromTypeVariable(function, BiFunction.class, OUTPUT_SIGNATURE);
    }

    private static Signature createSignatureFromTypeVariable(final Object input, final Class functionClass, final boolean isInput) {
        final Map<TypeVariable<?>, Type> typeArgs = createTypeArgsFor(input, functionClass);
        final Method targetMethod = getTargetedMethodIn(functionClass);
        final Type targetMethodType = isInput ? targetMethod.getGenericParameterTypes()[0] : targetMethod.getGenericReturnType();
        final Type inputType = mapTargetMethodTypeToRequiredType(targetMethodType, typeArgs);

        return createSignature(input, inputType, typeArgs, isInput);
    }

    private static Method getTargetedMethodIn(final Class<?> clazz) {
        try {
            if (!isAnnotatedFunctionalInterface(clazz)) {
                throw new IllegalArgumentException(format("Unable to determine target method for %s; it is not a %s.", clazz, FunctionalInterface.class));
            }
            for (final Method method : clazz.getDeclaredMethods()) {
                if (Modifier.isAbstract(method.getModifiers())) {
                    return clazz.getMethod(method.getName(), method.getParameterTypes());
                }
            }
        } catch (final NoSuchMethodException exception) {
            throw new IllegalArgumentException(format("Could not determine target method in %s.", clazz), exception);
        }

        throw new IllegalArgumentException(format("Could not determine target method in %s.", clazz));
    }

    private static boolean isAnnotatedFunctionalInterface(final Class<?> clazz) {
        return Stream.of(clazz.getAnnotations()).map(Annotation::annotationType).anyMatch(FunctionalInterface.class::equals);
    }

    private static Signature createSignature(final Object input, final Type type, final Map<TypeVariable<?>, Type> typeArgs, final boolean isInput) {
        Type typeForInput = type;
        if (type instanceof TypeVariable) {
            final TypeVariable typeVariable = TypeVariable.class.cast(type);
            final Type boundedDefaultType = getBoundedDefaultType(typeVariable);
            if (!Object.class.equals(boundedDefaultType) || Function.class.equals(getClassFrom(typeVariable.getGenericDeclaration()))) {
                typeForInput = boundedDefaultType;
            }
        }

        final Class clazz = getTypeClass(typeForInput, typeArgs);

        if (Tuple.class.isAssignableFrom(clazz)) {
            final TypeVariable[] tupleTypes = getTypeClass(type, typeArgs).getTypeParameters();
            if (tupleTypes.length > 0) {
                final Map<TypeVariable<?>, Type> classTypeArgs = TypeUtils.getTypeArguments(type, clazz);
                Collection<? extends Type> types = TypeUtils.getTypeArguments(type, clazz).values();
                Class[] classes = new Class[types.size()];
                int i = 0;
                for (final TypeVariable tupleType : tupleTypes) {
                    classes[i++] = getTypeClass(classTypeArgs.get(tupleType), typeArgs);
                }
                return new TupleSignature(input, clazz, classes, isInput);
            }
        }
        return new SingletonSignature(input, clazz, isInput);
    }

    private static Map<TypeVariable<?>, Type> createTypeArgsFor(final Object input, final Class<?> functionClass) {
        final Map<TypeVariable<?>, Type> typeArgs = TypeUtils.getTypeArguments(input.getClass(), functionClass);

        if (WrappedBiFunction.class.isAssignableFrom(input.getClass())) {
            final Map<TypeVariable<?>, Type> wrappedBiFunctionTypeArgs = getWrappedBiFunctionTypeArgMapping((WrappedBiFunction) input);
            final Map<TypeVariable<?>, Type> inputToWrappedBiFunctionTypeArgs = createInputToWrappedBiFunctionTypeArgMapping(input, typeArgs, wrappedBiFunctionTypeArgs);
            typeArgs.putAll(inputToWrappedBiFunctionTypeArgs);
        }

        return typeArgs;
    }

    private static Map<TypeVariable<?>, Type> createInputToWrappedBiFunctionTypeArgMapping(final Object input, final Map<TypeVariable<?>, Type> typeArgs, final Map<TypeVariable<?>, Type> wrappedBiFunctionTypeArgs) {
        final Map<String, TypeVariable> inputTypeVariableMap = new HashMap<>();

        for (final Type type : typeArgs.values()) {
            if (TypeVariable.class.isAssignableFrom(type.getClass())) {
                final TypeVariable typeVariable = TypeVariable.class.cast(type);
                if (input.getClass().equals(getClassFrom(typeVariable.getGenericDeclaration()))) {
                    inputTypeVariableMap.put(typeVariable.getName(), typeVariable);
                }
            }
        }

        final Map<TypeVariable<?>, Type> inputToWrappedBiFunctionTypeArgMapping = new HashMap<>(inputTypeVariableMap.size());

        for (final Map.Entry<TypeVariable<?>, Type> wrappedBiFunctionEntry : wrappedBiFunctionTypeArgs.entrySet()) {
            if (inputTypeVariableMap.containsKey(wrappedBiFunctionEntry.getKey().getName())) {
                final TypeVariable<?> inputTypeVariable = inputTypeVariableMap.get(wrappedBiFunctionEntry.getKey().getName());
                final Type wrappedBiFunctionType = wrappedBiFunctionEntry.getValue();
                inputToWrappedBiFunctionTypeArgMapping.put(inputTypeVariable, wrappedBiFunctionType);
            }
        }

        return inputToWrappedBiFunctionTypeArgMapping;
    }

    private static Map<TypeVariable<?>, Type> getWrappedBiFunctionTypeArgMapping(final WrappedBiFunction input) {
        final BiFunction wrappedBiFunction = input.getFunction();
        final Map<TypeVariable<?>, Type> wrappedBiFunctionTypeMap = TypeUtils.getTypeArguments(wrappedBiFunction.getClass(), BiFunction.class);

        final Map<TypeVariable<?>, Type> mapping = new HashMap<>();
        for (final Map.Entry<TypeVariable<?>, Type> entry : wrappedBiFunctionTypeMap.entrySet()) {
            if (BiFunction.class.equals(getClassFrom(entry.getKey().getGenericDeclaration()))) {
                mapping.put(entry.getKey(), entry.getValue());
            }
        }
        return mapping;
    }

    private static Class<?> getClassFrom(final GenericDeclaration genericDeclaration) {
        if (genericDeclaration instanceof Class) {
            return Class.class.cast(genericDeclaration);
        }
        return Object.class;
    }

    private static Type mapTargetMethodTypeToRequiredType(final Type targetMethodType, final Map<TypeVariable<?>, Type> typeArgs) {
        if (targetMethodType instanceof ParameterizedType || targetMethodType instanceof Class) {
            return targetMethodType;
        } else {
            if (targetMethodType instanceof TypeVariable) {
                return typeArgs.containsKey(targetMethodType)
                        ? mapTargetMethodTypeToRequiredType(typeArgs.get(targetMethodType), typeArgs)
                        : targetMethodType;
            }
        }
        return targetMethodType;
    }

    private static Type getBoundedDefaultType(final TypeVariable typeVariable) {
        final Type[] bounds = typeVariable.getBounds();
        return (bounds.length > 0) ? bounds[0] : Object.class;
    }

    protected static Class getTypeClass(final Type type, final Map<TypeVariable<?>, Type> typeArgs) {
        Type rawType = type;
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType) type).getRawType();
        }

        if (rawType instanceof Class) {
            return (Class) rawType;
        }

        if (rawType instanceof TypeVariable) {
            final Type t = typeArgs.get(rawType);
            if (null != t) {
                return getTypeClass(t, typeArgs);
            }
        }

        try {
            return Class.forName(rawType.getTypeName());
        } catch (final ClassNotFoundException e) {
            // cannot resolve - default to UnknownGenericType;
            return UnknownGenericType.class;
        }
    }

    public static class UnknownGenericType {
    }
}

