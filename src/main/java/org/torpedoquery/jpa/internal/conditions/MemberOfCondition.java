/**
 * Copyright (C) 2011 Xavier Jodoin (xavier@jodoin.me)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.torpedoquery.jpa.internal.conditions;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;
public class MemberOfCondition<T> extends AbstractCondition<T> {
    /**
     * <p>Constructor for MemberOfCondition.</p>
     *
     * @param selector a {@link org.torpedoquery.jpa.internal.Selector} object.
     * @param parameter a {@link org.torpedoquery.jpa.internal.Parameter} object.
     */
    public MemberOfCondition(Selector selector, Parameter parameter) {
        super(selector, Collections.singletonList(parameter));
    }

    /** {@inheritDoc} */
    @Override
    public String createQueryFragment(AtomicInteger increment) {
        return getParameters().get(0).generate(increment) + " member of " + getSelector().createQueryFragment(increment);
    }
}
