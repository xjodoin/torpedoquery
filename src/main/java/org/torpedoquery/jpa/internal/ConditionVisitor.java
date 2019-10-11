package org.torpedoquery.jpa.internal;

import org.torpedoquery.jpa.internal.conditions.AndCondition;
import org.torpedoquery.jpa.internal.conditions.BetweenCondition;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.conditions.EmptyLogicalCondition;
import org.torpedoquery.jpa.internal.conditions.EqualCondition;
import org.torpedoquery.jpa.internal.conditions.EqualPolymorphicCondition;
import org.torpedoquery.jpa.internal.conditions.GroupingCondition;
import org.torpedoquery.jpa.internal.conditions.GtCondition;
import org.torpedoquery.jpa.internal.conditions.GteCondition;
import org.torpedoquery.jpa.internal.conditions.InCondition;
import org.torpedoquery.jpa.internal.conditions.InSubQueryCondition;
import org.torpedoquery.jpa.internal.conditions.IsEmptyCondition;
import org.torpedoquery.jpa.internal.conditions.IsNotEmptyCondition;
import org.torpedoquery.jpa.internal.conditions.IsNotNullCondition;
import org.torpedoquery.jpa.internal.conditions.IsNullCondition;
import org.torpedoquery.jpa.internal.conditions.LikeCondition;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;
import org.torpedoquery.jpa.internal.conditions.LtCondition;
import org.torpedoquery.jpa.internal.conditions.LteCondition;
import org.torpedoquery.jpa.internal.conditions.MemberOfCondition;
import org.torpedoquery.jpa.internal.conditions.NotEqualCondition;
import org.torpedoquery.jpa.internal.conditions.NotEqualPolymorphicCondition;
import org.torpedoquery.jpa.internal.conditions.OrCondition;

public interface ConditionVisitor<T> {

	T visit(AndCondition andCondition);

	T visit(BetweenCondition betweenCondition);

	T visit(EqualCondition equalCondition);

	T visit(OrCondition orCondition);

	T visit(InCondition inCondition);

	T visit(EqualPolymorphicCondition equalPolymorphicCondition);

	T visit(GtCondition gtCondition);

	T visit(GteCondition gteCondition);

	T visit(InSubQueryCondition inSubQueryCondition);

	T visit(IsEmptyCondition isEmptyCondition);

	T visit(IsNotEmptyCondition isNotEmptyCondition);

	T visit(IsNotNullCondition isNotNullCondition);

	T visit(IsNullCondition isNullCondition);

	T visit(LtCondition ltCondition);

	T visit(LikeCondition likeCondition);

	T visit(NotEqualCondition notEqualCondition);

	T visit(LteCondition lteCondition);

	T visit(GroupingCondition groupingCondition);

	T visit(MemberOfCondition memberOfCondition);

	T visit(NotEqualPolymorphicCondition<?> notEqualPolymorphicCondition);

	T visit(LogicalCondition<?> logicalCondition);

	T visit(EmptyLogicalCondition emptyLogicalCondition);

	T visit(ConditionBuilder<?> conditionBuilder);


}
