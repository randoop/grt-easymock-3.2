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
package org.easymock.internal;

import org.checkerframework.dataflow.qual.Impure;
import org.checkerframework.dataflow.qual.SideEffectFree;
import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

import org.easymock.IAnswer;

/**
 * @author OFFIS, Tammo Freese
 */
public class ReplayState implements IMocksControlState, Serializable {

    private static final long serialVersionUID = 6314142602251047572L;

    private final IMocksBehavior behavior;

    private final ReentrantLock lock = new ReentrantLock();

    @SideEffectFree
    public ReplayState(final IMocksBehavior behavior) {
        this.behavior = behavior;
    }

    @Impure
    public Object invoke(final Invocation invocation) throws Throwable {

        behavior.checkThreadSafety();

        if (behavior.isThreadSafe()) {
            // If thread safe, synchronize the mock
            lock.lock();
            try {
                return invokeInner(invocation);
            } finally {
                lock.unlock();
            }
        }

        return invokeInner(invocation);
    }

    @Impure
    private Object invokeInner(final Invocation invocation) throws Throwable {
        LastControl.pushCurrentInvocation(invocation);
        try {
            final Result result = behavior.addActual(invocation);
            try {
                return result.answer();
            } catch (final Throwable t) {
                if (result.shouldFillInStackTrace()) {
                    throw new ThrowableWrapper(t);
                }
                throw t;
            }
        } finally {
            LastControl.popCurrentInvocation();
        }
    }

    @Impure
    public void verify() {
        behavior.verify();
    }

    @SideEffectFree
    @Impure
    public void replay() {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void callback(final Runnable runnable) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void checkOrder(final boolean value) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void makeThreadSafe(final boolean threadSafe) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void checkIsUsedInOneThread(final boolean shouldBeUsedInOneThread) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andReturn(final Object value) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andThrow(final Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andAnswer(final IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andDelegateTo(final Object answer) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andStubReturn(final Object value) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andStubThrow(final Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andStubAnswer(final IAnswer<?> answer) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void andStubDelegateTo(final Object delegateTo) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void asStub() {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void times(final Range range) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void setDefaultReturnValue(final Object value) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void setDefaultThrowable(final Throwable throwable) {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    public void setDefaultVoidCallable() {
        throwWrappedIllegalStateException();
    }

    @SideEffectFree
    @Impure
    private void throwWrappedIllegalStateException() {
        throw new RuntimeExceptionWrapper(new IllegalStateException(
                "This method must not be called in replay state."));
    }

    @SideEffectFree
    @Impure
    public void assertRecordState() {
        throwWrappedIllegalStateException();
    }
}
