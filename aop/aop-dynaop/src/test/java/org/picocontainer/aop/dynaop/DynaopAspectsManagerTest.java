/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.
 * --------------------------------------------------------------------------
 * The software in this package is published under the terms of the BSD style
 * license a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *****************************************************************************/
package org.picocontainer.aop.dynaop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;
import org.picocontainer.ComponentFactory;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.aop.AbstractAopTest;
import org.picocontainer.aop.AspectsManager;
import org.picocontainer.aop.ClassPointcut;
import org.picocontainer.aop.ComponentPointcut;
import org.picocontainer.aop.LoggingInterceptor;
import org.picocontainer.aop.MethodPointcut;
import org.picocontainer.aop.PointcutsFactory;
import org.picocontainer.aop.behaviours.Aspecting;
import org.picocontainer.aop.dynaop.DynaopAspectsManager;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.script.testmodel.AnotherInterface;
import org.picocontainer.script.testmodel.Dao;
import org.picocontainer.script.testmodel.DaoImpl;
import org.picocontainer.script.testmodel.IdGenerator;
import org.picocontainer.script.testmodel.IdGeneratorImpl;
import org.picocontainer.script.testmodel.Identifiable;
import org.picocontainer.script.testmodel.IdentifiableMixin;
import org.picocontainer.script.testmodel.OrderEntity;
import org.picocontainer.script.testmodel.OrderEntityImpl;

/**
 * @author Stephen Molitor
 */
public final class DynaopAspectsManagerTest extends AbstractAopTest {

    private final AspectsManager aspects = new DynaopAspectsManager();
    private final ComponentFactory componentFactory = new Caching().wrap(new Aspecting(aspects).wrap(new ConstructorInjection()));
    private final MutablePicoContainer pico = new DefaultPicoContainer(componentFactory);
    private final PointcutsFactory cuts = aspects.getPointcutsFactory();

    @Test public void testInterceptor() {
        StringBuffer log = new StringBuffer();
        aspects.registerInterceptor(cuts.instancesOf(Dao.class), cuts.allMethods(), new LoggingInterceptor(log));
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyIntercepted(dao, log);
    }

    @Test public void testContainerSuppliedInterceptor() {
        aspects.registerInterceptor(cuts.instancesOf(Dao.class), cuts.allMethods(), LoggingInterceptor.class);

        pico.addComponent("log", StringBuffer.class);
        pico.addComponent(LoggingInterceptor.class);
        pico.addComponent(Dao.class, DaoImpl.class);

        Dao dao = pico.getComponent(Dao.class);
        StringBuffer log = (StringBuffer) pico.getComponent("log");
        verifyIntercepted(dao, log);
    }

    @Test public void testComponentInterceptor() {
        StringBuffer log = new StringBuffer();

        aspects.registerInterceptor(cuts.component("intercepted"), cuts.allMethods(), new LoggingInterceptor(log));
        pico.addComponent("intercepted", DaoImpl.class);
        pico.addComponent("notIntercepted", DaoImpl.class);

        Dao intercepted = (Dao) pico.getComponent("intercepted");
        Dao notIntercepted = (Dao) pico.getComponent("notIntercepted");

        verifyIntercepted(intercepted, log);
        verifyNotIntercepted(notIntercepted, log);
    }

    @Test public void testContainerSuppliedComponentInterceptor() {
        aspects.registerInterceptor(cuts.component("intercepted"), cuts.allMethods(), LoggingInterceptor.class);

        pico.addComponent("log", StringBuffer.class);
        pico.addComponent(LoggingInterceptor.class);
        pico.addComponent("intercepted", DaoImpl.class);
        pico.addComponent("notIntercepted", DaoImpl.class);

        StringBuffer log = (StringBuffer) pico.getComponent("log");
        Dao intercepted = (Dao) pico.getComponent("intercepted");
        Dao notIntercepted = (Dao) pico.getComponent("notIntercepted");

        verifyIntercepted(intercepted, log);
        verifyNotIntercepted(notIntercepted, log);
    }

    @Test public void testMixin() {
        aspects.registerMixin(cuts.instancesOf(Dao.class), IdentifiableMixin.class);
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyMixin(dao);
        assertTrue(dao instanceof AnotherInterface);
    }

    @Test public void testContainerSuppliedMixin() {
        aspects.registerMixin(cuts.instancesOf(OrderEntity.class), IdentifiableMixin.class);
        pico.addComponent("order1", OrderEntityImpl.class);
        pico.addComponent("order2", OrderEntityImpl.class);

        // register mixin dependency:
        pico.addComponent(IdGenerator.class, IdGeneratorImpl.class);

        Identifiable order1 = (Identifiable) pico.getComponent("order1");
        Identifiable order2 = (Identifiable) pico.getComponent("order2");

        assertEquals(1, order1.getId());
        assertEquals(2, order2.getId());

        // order1 and order2 do NOT share the same mixin instance (usually a
        // good thing),
        // although their mixin instances do share the same IdGenerator
        order1.setId(42);
        assertEquals(42, order1.getId());
        assertEquals(2, order2.getId());
    }

    @Test public void testContainerSuppliedMixinWithMixinExplicitlyRegistered() {
        aspects.registerMixin(cuts.instancesOf(OrderEntity.class), IdentifiableMixin.class);
        pico.addComponent(IdentifiableMixin.class);
        pico.addComponent("order1", OrderEntityImpl.class);
        pico.addComponent("order2", OrderEntityImpl.class);

        Identifiable order1 = (Identifiable) pico.getComponent("order1");
        Identifiable order2 = (Identifiable) pico.getComponent("order2");

        assertEquals(1, order1.getId());
        assertEquals(1, order2.getId());

        // order1 and order2 share the same IdentifiableMixin object (not
        // usually what you want!)
        order1.setId(42);
        assertEquals(42, order1.getId());
        assertEquals(42, order2.getId());
    }

    @Test public void testComponentMixin() {
        pico.addComponent("hasMixin", DaoImpl.class);
        pico.addComponent("noMixin", DaoImpl.class);

        aspects.registerMixin(cuts.component("hasMixin"), IdentifiableMixin.class);

        Dao hasMixin = (Dao) pico.getComponent("hasMixin");
        Dao noMixin = (Dao) pico.getComponent("noMixin");

        verifyMixin(hasMixin);
        verifyNoMixin(noMixin);
        assertTrue(hasMixin instanceof AnotherInterface);
    }

    // weird. what is this suposed to do? does it rely on non-caching ?
    public void doNOT_testContainerSuppliedComponentMixin() {
        aspects.registerMixin(cuts.componentName("hasMixin*"), new Class[]{Identifiable.class},
                IdentifiableMixin.class);

        pico.addComponent("hasMixin1", OrderEntityImpl.class);
        pico.addComponent("hasMixin2", OrderEntityImpl.class);
        pico.addComponent("noMixin", OrderEntityImpl.class);
        pico.addComponent(IdGenerator.class, IdGeneratorImpl.class);

        Identifiable hasMixin1 = (Identifiable) pico.getComponent("hasMixin1");
        Identifiable hasMixin2 = (Identifiable) pico.getComponent("hasMixin1");
        OrderEntity noMixin = (OrderEntity) pico.getComponent("noMixin");

        assertFalse(noMixin instanceof Identifiable);
        assertEquals(1, hasMixin1.getId());
        assertEquals(2, hasMixin2.getId());

        hasMixin1.setId(42);
        assertEquals(42, hasMixin1.getId());
        assertEquals(2, hasMixin2.getId());
    }

    @Test public void testMixinExplicitInterfaces() {
        aspects.registerMixin(cuts.instancesOf(Dao.class), new Class[]{Identifiable.class}, IdentifiableMixin.class);
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyMixin(dao);
        assertFalse(dao instanceof AnotherInterface);
    }

    @Test public void testComponentMixinExplicitInterfaces() {
        pico.addComponent("hasMixin", DaoImpl.class);
        pico.addComponent("noMixin", DaoImpl.class);

        aspects.registerMixin(cuts.component("hasMixin"), new Class[]{Identifiable.class}, IdentifiableMixin.class);

        Dao hasMixin = (Dao) pico.getComponent("hasMixin");
        Dao noMixin = (Dao) pico.getComponent("noMixin");

        verifyMixin(hasMixin);
        verifyNoMixin(noMixin);

        assertFalse(hasMixin instanceof AnotherInterface);
    }

    @Test public void testCustomClassPointcut() {
        StringBuffer log = new StringBuffer();

        ClassPointcut customCut = new ClassPointcut() {
            public boolean picks(Class clazz) {
                return true;
            }
        };

        aspects.registerInterceptor(customCut, cuts.allMethods(), new LoggingInterceptor(log));
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyIntercepted(dao, log);
    }

    @Test public void testCustomMethodPointcut() {
        StringBuffer log = new StringBuffer();

        MethodPointcut customCut = new MethodPointcut() {
            public boolean picks(Method method) {
                return true;
            }
        };

        aspects.registerInterceptor(cuts.instancesOf(Dao.class), customCut, new LoggingInterceptor(log));
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyIntercepted(dao, log);
    }

    @Test public void testCustomComponentPointcut() {
        StringBuffer log = new StringBuffer();

        ComponentPointcut customCut = new ComponentPointcut() {
            public boolean picks(Object componentKey) {
                return true;
            }
        };

        aspects.registerInterceptor(customCut, cuts.allMethods(), new LoggingInterceptor(log));
        pico.addComponent(Dao.class, DaoImpl.class);
        Dao dao = pico.getComponent(Dao.class);
        verifyIntercepted(dao, log);
    }

}