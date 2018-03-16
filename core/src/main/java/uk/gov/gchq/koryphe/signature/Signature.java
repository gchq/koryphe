/*
 * Copyright 2017-2018 Crown Copyright
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

import uk.gov.gchq.koryphe.ValidationResult;
import uk.gov.gchq.koryphe.tuple.Tuple;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A <code>Signature</code> is the type metadata for the input or output of a {@link java.util.function.Function}.
 */
public abstract class Signature {
    /**
     * Tests whether this <code>Signature</code> is compatible with the types supplied.
     *
     * @param arguments Class or Tuple of classes to test.
     * @return ValidationResult containing the isValid flag and errors messages.
     */
    public abstract ValidationResult assignable(final Class<?>... arguments);

    public abstract Class[] getClasses();

    public abstract Integer getNumClasses();

    /**
     * Get the input signature of a function.
     *
     * @param function Function.
     * @return Input signature.
     */
    public static Signature getInputSignature(final Predicate function) {
        return createSignatureFromTypeVariable(function, Predicate.class, 0, true);
    }

    /**
     * Get the input signature of a function.
     *
     * @param function Function.
     * @return Input signature.
     */
    public static Signature getInputSignature(final Function function) {
        return createSignatureFromTypeVariable(function, Function.class, 0, true);
    }

    /**
     * Get the input signature of a BiFunction.
     *
     * @param function BiFunction.
     * @param <F>      the type of the BiFunction
     * @param <I>      the first input type of the BiFunction
     * @param <O>      the second input type and output type of the BiFunction
     * @return Input signature
     */
    public static <F extends BiFunction<I, O, O>, I, O> Signature getInputSignature(final F function) {
        return createSignatureFromTypeVariable(function, BiFunction.class, 0, true);
    }

    /**
     * Get the output signature of a function.
     *
     * @param function Function.
     * @return Output signature.
     */
    public static Signature getOutputSignature(final Function function) {
        return createSignatureFromTypeVariable(function, Function.class, 1, false);
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
        return createSignatureFromTypeVariable(function, BiFunction.class, 2, false);
    }

    /**
     * Create a <code>Signature</code> for the type variable at the given index.
     *
     * @param input             Function to create signature for.
     * @param functionClass     The input class
     * @param typeVariableIndex 0 for I or 1 for O.
     * @param isInput           if true then it is an input signature otherwise it is an output signature
     * @return Signature of the type variable.
     */
    private static Signature createSignatureFromTypeVariable(final Object input, final Class functionClass, final int typeVariableIndex, final boolean isInput) {
        TypeVariable<?> tv = functionClass.getTypeParameters()[typeVariableIndex];
        final Map<TypeVariable<?>, Type> typeArgs = TypeUtils.getTypeArguments(input.getClass(), functionClass);
        Type type = typeArgs.get(tv);
        return createSignature(input, type, typeArgs, isInput);
    }

    private static Signature createSignature(final Object input, final Type type, final Map<TypeVariable<?>, Type> typeArgs, final boolean isInput) {
        final Class clazz = getTypeClass(type, typeArgs);

        if (Tuple.class.isAssignableFrom(clazz)) {
            final TypeVariable[] tupleTypes = getTypeClass(type, typeArgs).getTypeParameters();
            final Map<TypeVariable<?>, Type> classTypeArgs = TypeUtils.getTypeArguments(type, clazz);
            Collection<? extends Type> types = TypeUtils.getTypeArguments(type, clazz).values();
            Class[] classes = new Class[types.size()];
            int i = 0;
            for (final TypeVariable tupleType : tupleTypes) {
                classes[i++] = getTypeClass(classTypeArgs.get(tupleType), typeArgs);
            }

            return new TupleSignature(input, clazz, classes, isInput);
        }

        return new SingletonSignature(input, clazz, isInput);
    }

    protected static Class getTypeClass(final Type type, final Map<TypeVariable<?>, Type> typeArgs) {
        Type rawType = type;
        if (type instanceof ParameterizedType) {
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
        // cannot resolve - default to UnknownGenericType;
        return UnknownGenericType.class;
    }

    public static class UnknownGenericType {
    }
}
