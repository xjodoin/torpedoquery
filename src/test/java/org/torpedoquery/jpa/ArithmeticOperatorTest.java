/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
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
package org.torpedoquery.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.torpedoquery.jpa.test.bo.Entity;

import static org.torpedoquery.jpa.Torpedo.*;

public class ArithmeticOperatorTest {

	@Test
	public void testPlusOperator()
	{
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(from.getIntegerField()).plus(from.getPrimitiveInt()));
		Assert.assertEquals("select entity_0.integerField + entity_0.primitiveInt from Entity entity_0", select.getQuery()); 
		
	}
	
	@Test
	public void testPlusOperatorWithLeftFunction()
	{
		//I know it's suppose to be with group by...
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(sum(from.getIntegerField())).plus(from.getPrimitiveInt()));
		Assert.assertEquals("select sum(entity_0.integerField) + entity_0.primitiveInt from Entity entity_0", select.getQuery()); 
		
	}
	
	@Test
	public void testPlusOperatorWithRightFunction()
	{
		//I know it's suppose to be with group by...
		Entity from = from(Entity.class);
		Query<Integer> select = select(operation(from.getIntegerField()).plus(sum(from.getPrimitiveInt())));
		Assert.assertEquals("select entity_0.integerField + sum(entity_0.primitiveInt) from Entity entity_0", select.getQuery()); 
		
	}
	
}
