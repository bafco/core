package org.jboss.weld.tests.unit.reflection.util;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import javax.enterprise.util.TypeLiteral;

import junit.framework.Assert;

import org.jboss.weld.resolution.BeanTypeAssignabilityRules;
import org.jboss.weld.resolution.EventTypeAssignabilityRules;
import org.jboss.weld.util.reflection.Reflections;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
public class Weld1102Test {

    @Test
    @Ignore
    public <B extends Bar> void test1() throws Exception {
        Assert.assertTrue("Required type Foo<Bar<Integer>> should match bean type Foo<B extends Bar>",
            requiredTypeMatchesBeanType(
                new TypeLiteral<Foo<Bar<Integer>>>() {
                },
                new TypeLiteral<Foo<B>>() {
                }
            ));
    }

    @Test
    @Ignore
    public <B extends Bar> void test2() throws Exception {
        Assert.assertTrue("Required type Foo<Bar<Baz>> should match bean type Foo<B extends Bar>",
            requiredTypeMatchesBeanType(
                new TypeLiteral<Foo<Bar<Baz>>>() {
                },
                new TypeLiteral<Foo<B>>() {
                }
            ));
    }

    @Test
    public <B extends Bar<Integer>> void test3() throws Exception {
        Assert.assertTrue("Required type Foo<Bar<Integer>>  should match bean type Foo<B extends Bar<Integer>>",
            requiredTypeMatchesBeanType(
                new TypeLiteral<Foo<Bar<Integer>>>() {
                },
                new TypeLiteral<Foo<B>>() {
                }
            ));
    }

    @Test
    @Ignore
    public <B extends Bar<Integer>> void test4() throws Exception {
        Assert.assertTrue("Required type Foo<Bar<Number>> should match bean type Foo<B extends Bar<Integer>>",
            requiredTypeMatchesBeanType(
                new TypeLiteral<Foo<Bar<Number>>>() {
                },
                new TypeLiteral<Foo<B>>() {
                }
            ));
    }

    private boolean requiredTypeMatchesBeanType(TypeLiteral requiredType, TypeLiteral beanType) {
        return BeanTypeAssignabilityRules.instance().matches(requiredType.getType(), beanType.getType());
    }
}
