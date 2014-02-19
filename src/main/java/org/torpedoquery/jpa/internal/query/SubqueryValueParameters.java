/**
 *   Copyright Xavier Jodoin xjodoin@torpedoquery.org
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal.query;

import org.torpedoquery.core.QueryBuilder;
import org.torpedoquery.jpa.Query;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Selector;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SubqueryValueParameters<T> extends SelectorParameter<T> {

    private final QueryBuilder<T> value;

    public SubqueryValueParameters(Query<T> selector) {
        super(selector);
        this.value = (QueryBuilder<T>) selector;
    }

    public List<ValueParameter> getParameters() {
        return value.getValueParameters();
    }

}
