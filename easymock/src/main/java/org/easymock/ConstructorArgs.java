/**
 * Copyright 2001-2013 the original author or authors.
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
package org.easymock;

import org.checkerframework.dataflow.qual.Pure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.checkerframework.dataflow.qual.Impure;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Class wrapping arguments to create a partial class mock that gets
 * instantiated by calling one of its constructors
 * 
 * @author Henri Tremblay
 */
public class ConstructorArgs {

    private final Constructor<?> constructor;

    private final Object[] initArgs;

    /**
     * @param constructor
     *            Constructor to be called when creating the mock
     * @param initArgs
     *            Arguments passed to the constructor
     */
    @Impure
    public ConstructorArgs(final Constructor<?> constructor, final Object... initArgs) {
        this.constructor = constructor;
        this.initArgs = initArgs;

        validateArgs();
    }

    @Impure
    private void validateArgs() {

        final Class<?>[] paramTypes = constructor.getParameterTypes();

        if (initArgs.length != paramTypes.length) {
            throw new IllegalArgumentException("Number of provided arguments doesn't match constructor ones");
        }

        for (int i = 0; i < initArgs.length; i++) {

            final Class<?> paramType = paramTypes[i];
            final Object arg = initArgs[i];

            if (paramType.isPrimitive()) {
                if (arg == null) {
                    throw new IllegalArgumentException("Null argument for primitive param " + i);
                }

                try {
                    final Field field = arg.getClass().getDeclaredField("TYPE");
                    final Class<?> argType = (Class<?>) field.get(null);

                    if (paramType.equals(argType)) {
                        continue;
                    }
                } catch (final Exception e) {
                    throw throwException(paramType, arg);
                }

                throw throwException(paramType, arg);
            }
            if (arg == null) {
                continue;
            }
            if (!paramType.isAssignableFrom(arg.getClass())) {
                throw throwException(paramType, arg);
            }
        }
    }

    @SideEffectFree
    private IllegalArgumentException throwException(final Class<?> paramType, final Object arg) {
        return new IllegalArgumentException(arg + " isn't of type " + paramType);
    }

    /**
     * @return arguments to be passed to the constructor
     */
    @Pure
    public Object[] getInitArgs() {
        return initArgs;
    }

    /**
     * @return constructor to be called
     */
    @Pure
    public Constructor<?> getConstructor() {
        return constructor;
    }
}
