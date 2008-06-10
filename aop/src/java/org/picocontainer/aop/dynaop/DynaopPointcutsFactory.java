/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.picocontainer.aop.dynaop;

import java.lang.reflect.Method;

import org.picocontainer.aop.ClassPointcut;
import org.picocontainer.aop.MethodPointcut;
import org.picocontainer.aop.defaults.AbstractPointcutsFactory;

import dynaop.Pointcuts;

/**
 * Implements the <code>org.picocontainer.aop.PointcutsFactory</code>
 * interface using dynaop.
 *
 * @author Stephen Molitor
 */
public class  DynaopPointcutsFactory extends AbstractPointcutsFactory {

    public ClassPointcut allClasses() {
        return new DynaopClassPointcut(Pointcuts.ALL_CLASSES);
    }

    public ClassPointcut instancesOf(Class type) {
        return new DynaopClassPointcut(Pointcuts.instancesOf(type));
    }

    public ClassPointcut className(String regex) {
            return new DynaopClassPointcut(Pointcuts.className(regex));
    }

    public ClassPointcut oneClass(Class clazz) {
        return new DynaopClassPointcut(Pointcuts.singleton(clazz));
    }

    public ClassPointcut packageName(String packageName) {
        return new DynaopClassPointcut(Pointcuts.packageName(packageName));
    }

    public ClassPointcut intersection(ClassPointcut a, ClassPointcut b) {
        return new DynaopClassPointcut(Pointcuts.intersection(toDynaopClassCut(a), toDynaopClassCut(b)));
    }

    public ClassPointcut union(ClassPointcut a, ClassPointcut b) {
        return new DynaopClassPointcut(Pointcuts.union(toDynaopClassCut(a), toDynaopClassCut(b)));
    }

    public ClassPointcut not(ClassPointcut classPointcut) {
        return new DynaopClassPointcut(Pointcuts.not(toDynaopClassCut(classPointcut)));
    }

    public MethodPointcut allMethods() {
        return new DynaopMethodPointcut(Pointcuts.ALL_METHODS);
    }

    public MethodPointcut getMethods() {
        return new DynaopMethodPointcut(Pointcuts.GET_METHODS);
    }

    public MethodPointcut isMethods() {
        return new DynaopMethodPointcut(Pointcuts.IS_METHODS);
    }

    public MethodPointcut setMethods() {
        return new DynaopMethodPointcut(Pointcuts.SET_METHODS);
    }

    public MethodPointcut objectMethods() {
        return new DynaopMethodPointcut(Pointcuts.OBJECT_METHODS);
    }

    public MethodPointcut returnType(ClassPointcut classPointcut) {
        return new DynaopMethodPointcut(Pointcuts.returnType(toDynaopClassCut(classPointcut)));
    }

    public MethodPointcut signature(String regexp) {
            return new DynaopMethodPointcut(Pointcuts.signature(regexp));
    }

    public MethodPointcut oneMethod(Method method) {
        return new DynaopMethodPointcut(Pointcuts.singleton(method));
    }

    public MethodPointcut declaringClass(ClassPointcut classPointcut) {
        return new DynaopMethodPointcut(Pointcuts.declaringClass(toDynaopClassCut(classPointcut)));
    }

    public MethodPointcut membersOf(Class clazz) {
        return new DynaopMethodPointcut(Pointcuts.membersOf(clazz));
    }

    public MethodPointcut intersection(MethodPointcut a, MethodPointcut b) {
        return new DynaopMethodPointcut(Pointcuts.intersection(toDynaopMethodCut(a), toDynaopMethodCut(b)));
    }

    public MethodPointcut union(MethodPointcut a, MethodPointcut b) {
        return new DynaopMethodPointcut(Pointcuts.union(toDynaopMethodCut(a), toDynaopMethodCut(b)));
    }

    public MethodPointcut not(MethodPointcut methodPointcut) {
        return new DynaopMethodPointcut(Pointcuts.not(toDynaopMethodCut(methodPointcut)));
    }

    private static dynaop.ClassPointcut toDynaopClassCut(final ClassPointcut cut) {
        // The purpose of the anonymous inner class adapter below is to allow
        // users to use union, intersection and not with custom pointcuts (not
        // instances of dynaop.ClassPointcut). Now we could just wrap cut
        // with the adapter every time, even if it is already a
        // dynaop.ClassPointcut. But the extra level of indirection gets a
        // little
        // confusing when debugging. Thus the instanceof check.
        if (cut instanceof dynaop.ClassPointcut) {
            return (dynaop.ClassPointcut) cut;
        } else {
            return new dynaop.ClassPointcut() {
                public boolean picks(Class clazz) {
                    return cut.picks(clazz);
                }
            };
        }
    }

    private static dynaop.MethodPointcut toDynaopMethodCut(final MethodPointcut cut) {
        // see comment in toDynaopClassCut, above
        if (cut instanceof dynaop.MethodPointcut) {
            return (dynaop.MethodPointcut) cut;
        } else {
            return new dynaop.MethodPointcut() {
                public boolean picks(Method method) {
                    return cut.picks(method);
                }
            };
        }
    }

}