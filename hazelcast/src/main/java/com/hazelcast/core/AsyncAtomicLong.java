/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.core;

/**
 * A {@link IAtomicLong} that exposes its operations using a {@link com.hazelcast.core.CompletableFuture}
 * so it can be used in the reactive programming model approach.
 */
public interface AsyncAtomicLong extends IAtomicLong {

    CompletableFuture<Long> asyncAddAndGet(long delta);

    CompletableFuture<Boolean> asyncCompareAndSet(long expect, long update);

    CompletableFuture<Long> asyncDecrementAndGet();

    CompletableFuture<Long> asyncGet();

    CompletableFuture<Long> asyncGetAndAdd(long delta);

    CompletableFuture<Long> asyncGetAndSet(long newValue);

    CompletableFuture<Long> asyncIncrementAndGet();

    CompletableFuture<Long> asyncGetAndIncrement();

    CompletableFuture<Void> asyncSet(long newValue);

    CompletableFuture<Void> asyncAlter(Function<Long, Long> function);

    CompletableFuture<Long> asyncAlterAndGet(Function<Long, Long> function);

    CompletableFuture<Long> asyncGetAndAlter(Function<Long, Long> function);

    <R> CompletableFuture<R> asyncApply(Function<Long, R> function);
}