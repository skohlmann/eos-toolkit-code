/* Copyright (c) 2008 Sascha Kohlmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.eos.config;


import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsAbstract;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsInterface;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsAnnotation;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsArray;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsEnum;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsStatic;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsNotStatic;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsMemberClass;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsPrimitive;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.IsPlainOldJavaClassPredicate;
import net.sf.eos.config.InjectionSupport.DefaultInjectionSupport.InjectParameterRulePredicate;

/**
 * @author Sascha Kohlmann
 */
public class DefaultInjectionSupportInjectRulesPredicatesTest {

    @Test
    public void isAnnotation() {
        assertTrue(new IsAnnotation().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void isAnnotationFailsWithPlainClass() {
        assertFalse(new IsAnnotation().evaluate(this.getClass()));
    }

    @Test
    public void isAnnotationFailsWithEnum() {
        assertFalse(new IsAnnotation().evaluate(SimpleEnum.class));
    }

    @Test
    public void isAnnotationFailsWithInterface() {
        assertFalse(new IsAnnotation().evaluate(SimpleInterface.class));
    }

    @Test
    public void isAnnotationFailsWithArrayType() {
        assertFalse(new IsAnnotation().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isAnnotationFailsWithPrimitiveType() {
        assertFalse(new IsAnnotation().evaluate(Void.TYPE));
    }

    @Test
    public void isInterface() {
        assertTrue(new IsInterface().evaluate(SimpleInterface.class));
    }

    @Test
    public void isInterfaceFailsWithPlainClass() {
        assertFalse(new IsInterface().evaluate(this.getClass()));
    }

    @Test
    public void isInterfaceFailsWithEnum() {
        assertFalse(new IsInterface().evaluate(SimpleEnum.class));
    }

    @Test
    public void isInterfaceFailsWithArrayType() {
        assertFalse(new IsInterface().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isInterfaceFailsWithPrimitiveType() {
        assertFalse(new IsInterface().evaluate(Void.TYPE));
    }

    @Test
    public void isEnum() {
        assertTrue(new IsEnum().evaluate(SimpleEnum.class));
    }

    @Test
    public void isEnumFailsWithPlainClass() {
        assertFalse(new IsEnum().evaluate(this.getClass()));
    }

    @Test
    public void isEnumFailsWithAnnotation() {
        assertFalse(new IsEnum().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void isEnumFailsWithInterface() {
        assertFalse(new IsEnum().evaluate(SimpleInterface.class));
    }

    @Test
    public void isEnumFailsWithArrayType() {
        assertFalse(new IsEnum().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isEnumFailsWithPrimitiveType() {
        assertFalse(new IsEnum().evaluate(Void.TYPE));
    }

    @Test
    public void isPrimitive() {
        assertTrue(new IsPrimitive().evaluate(Void.TYPE));
    }

    @Test
    public void isPrimitiveFailsWithPlainClass() {
        assertFalse(new IsPrimitive().evaluate(this.getClass()));
    }

    @Test
    public void isPrimitiveFailsWithAnnotation() {
        assertFalse(new IsPrimitive().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void isPrimitiveFailsWithInterface() {
        assertFalse(new IsPrimitive().evaluate(SimpleInterface.class));
    }

    @Test
    public void isPrimitiveFailsWithArrayType() {
        assertFalse(new IsPrimitive().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isPrimitiveFailsWithEnum() {
        assertFalse(new IsPrimitive().evaluate(SimpleEnum.class));
    }

    @Test
    public void isArray() {
        assertTrue(new IsArray().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isArrayFailsWithPlainClass() {
        assertFalse(new IsArray().evaluate(this.getClass()));
    }

    @Test
    public void isArrayFailsWithAnnotation() {
        assertFalse(new IsArray().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void isArrayFailsWithInterface() {
        assertFalse(new IsArray().evaluate(SimpleInterface.class));
    }

    @Test
    public void isArrayFailsWithPrimitiveType() {
        assertFalse(new IsArray().evaluate(Void.TYPE));
    }

    @Test
    public void isArrayFailsWithEnum() {
        assertFalse(new IsArray().evaluate(SimpleEnum.class));
    }

    @Test
    public void isAbstract() {
        assertTrue(new IsAbstract().evaluate(AbstractClass.class));
    }

    @Test
    public void isAbstractFailsWithNonAbstractClass() {
        assertFalse(new IsAbstract().evaluate(this.getClass()));
    }

    @Test
    public void isStatic() {
        assertTrue(new IsStatic().evaluate(SimpleClass.class));
    }

    @Test
    public void isStaticFailsWithNonStaticClass() {
        assertFalse(new IsStatic().evaluate(SimpleNonStaticClass.class));
    }

    @Test
    public void isNotStatic() {
        assertFalse(new IsNotStatic().evaluate(SimpleClass.class));
    }

    @Test
    public void isNotStaticFailsWithNonStaticClass() {
        assertTrue(new IsNotStatic().evaluate(SimpleNonStaticClass.class));
    }

    @Test
    public void isPlainOldJavaClassPredicate() {
        assertTrue(new IsPlainOldJavaClassPredicate().evaluate(SimpleClass.class));
    }

    @Test
    public void isPlainOldJavaClassPredicateFailsWithAnnotationClass() {
        assertFalse(new IsPlainOldJavaClassPredicate().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void isPlainOldJavaClassPredicateFailsWithEnum() {
        assertFalse(new IsPlainOldJavaClassPredicate().evaluate(SimpleEnum.class));
    }

    @Test
    public void isPlainOldJavaClassPredicateFailsWithInterface() {
        assertFalse(new IsPlainOldJavaClassPredicate().evaluate(SimpleInterface.class));
    }

    @Test
    public void isPlainOldJavaClassPredicateFailsWithArrayType() {
        assertFalse(new IsPlainOldJavaClassPredicate().evaluate(new Object[0].getClass()));
    }

    @Test
    public void isPlainOldJavaClassPredicateFailsWithPrimitiveType() {
        assertFalse(new IsPlainOldJavaClassPredicate().evaluate(Void.TYPE));
    }


    
    @Test
    public void injectViolationTestRuleChainPredicate() {
        assertTrue(new InjectParameterRulePredicate().evaluate(SimpleClass.class));
    }

    @Test
    public void injectViolationTestRuleChainPredicateFailsWithAnnotationClass() {
        assertFalse(new InjectParameterRulePredicate().evaluate(SimpleAnnotation.class));
    }

    @Test
    public void injectViolationTestRuleChainPredicateFailsWithEnum() {
        assertFalse(new InjectParameterRulePredicate().evaluate(SimpleEnum.class));
    }

    @Test
    public void injectViolationTestRuleChainPredicateFailsWithInterface() {
        assertFalse(new InjectParameterRulePredicate().evaluate(SimpleInterface.class));
    }

    @Test
    public void injectViolationTestRuleChainPredicateFailsWithArrayType() {
        assertFalse(new InjectParameterRulePredicate().evaluate(new Object[0].getClass()));
    }

    @Test
    public void injectViolationTestRuleChainPredicateFailsWithPrimitiveType() {
        assertFalse(new InjectParameterRulePredicate().evaluate(Void.TYPE));
    }

    @Test
    public void isMemberClassWithStaticClass() {
        assertTrue(new IsMemberClass().evaluate(SimpleClass.class));
    }

    @Test
    public void isMemberClassWithLocalClass() {
        assertTrue(new IsMemberClass().evaluate(SimpleNonStaticClass.class));
    }

    @Test
    public void isMemberClassFailsNoMemberClass() {
        assertFalse(new IsMemberClass().evaluate(ToInject.class));
    }

    public class SimpleNonStaticClass {}
    public static class SimpleClass {}
    public static enum SimpleEnum {}
    public static @interface SimpleAnnotation {}
    public static interface SimpleInterface {}
    public static abstract class AbstractClass {}
}
