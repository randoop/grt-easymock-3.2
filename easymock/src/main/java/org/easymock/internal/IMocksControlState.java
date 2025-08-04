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

import org.checkerframework.dataflow.qual.SideEffectFree;
import org.checkerframework.dataflow.qual.Impure;
import org.easymock.IAnswer;

/**
 * @author OFFIS, Tammo Freese
 */
public interface IMocksControlState {

    @Impure
    Object invoke(Invocation invocation) throws Throwable;

    @SideEffectFree
    @Impure
    void assertRecordState();

    @Impure
    void andReturn(Object value);

    @Impure
    void andThrow(Throwable throwable);

    @Impure
    void andAnswer(IAnswer<?> answer);

    @Impure
    void andDelegateTo(Object answer);

    @Impure
    void andStubReturn(Object value);

    @Impure
    void andStubThrow(Throwable throwable);

    @Impure
    void andStubAnswer(IAnswer<?> answer);

    @Impure
    void andStubDelegateTo(Object delegateTo);

    @Impure
    void asStub();

    @Impure
    void times(Range range);

    @Impure
    void checkOrder(boolean value);

    @Impure
    void makeThreadSafe(boolean threadSafe);

    @Impure
    void checkIsUsedInOneThread(boolean shouldBeUsedInOneThread);

    @Impure
    void replay();

    @Impure
    void verify();
}
