/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.
 * --------------------------------------------------------------------------
 * The software in this package is published under the terms of the BSD style
 * license a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *****************************************************************************/
package org.picocontainer.aop.dynaop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Test;
import org.picocontainer.PicoContainer;
import org.picocontainer.script.AbstractScriptedContainerBuilderTestCase;
import org.picocontainer.script.groovy.GroovyContainerBuilder;
import org.picocontainer.script.testmodel.Dao;
import org.picocontainer.script.testmodel.Identifiable;

/**
 * @author Stephen Molitor
 * @author Paul Hammant
 * @author Aslak Helles&oslash;y
 * @author Mauro Talevi
 */
public class GroovyNodeBuilderAopTest extends AbstractScriptedContainerBuilderTestCase {

    @Test public void testContainerScopedInterceptor() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "" +
                "log = new StringBuffer()\n" +
                "logger = new LoggingInterceptor(log)\n" +
                "\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container() {\n" +
                "    aspect(classCut:cuts.instancesOf(Dao.class), methodCut:cuts.allMethods(), interceptor:logger)\n" +
                "    component(key:Dao, class:DaoImpl)\n" +
                "    component(key:StringBuffer, instance:log)\n" +
                "}\n";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        StringBuffer log = pico.getComponent(StringBuffer.class);
        verifyIntercepted(dao, log);
    }

    @Test public void testContainerScopedPointcutWithNestedAdvices() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "" +
                "log = new StringBuffer()\n" +
                "logger = new LoggingInterceptor(log)\n" +
                "\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container() {\n" +
                "    pointcut(classCut:cuts.instancesOf(Dao.class), methodCut:cuts.allMethods()) {\n" +
                "        aspect(interceptor:logger)\n" +
                "    }\n" +
                "    component(key:Dao, class:DaoImpl)\n" +
                "    component(key:StringBuffer, instance:log)\n" +
                "}\n";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        StringBuffer log = pico.getComponent(StringBuffer.class);
        verifyIntercepted(dao, log);
    }

    @Test public void testContainerScopedContainerSuppliedInterceptor() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container() {\n" +
                "    aspect(classCut:cuts.instancesOf(Dao), methodCut:cuts.allMethods(), interceptorKey:LoggingInterceptor)\n" +
                "    component(key:'log', class:StringBuffer)\n" +
                "    component(LoggingInterceptor)\n" +
                "    component(key:Dao, class:DaoImpl)\n" +
                "}\n";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        StringBuffer log = (StringBuffer) pico.getComponent("log");
        verifyIntercepted(dao, log);
    }

    @Test public void testComponentScopedInterceptor() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "log = new StringBuffer()\n" +
                "logger = new LoggingInterceptor(log)\n" +
                "\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container() {\n" +
                "    component(key:'intercepted', class:DaoImpl) {\n" +
                "        aspect(methodCut:cuts.allMethods(), interceptor:logger)\n" +
                "    }\n" +
                "    component(key:'log', instance:log)\n" +
                "    component(key:'notIntercepted', class:DaoImpl)\n" +
                "}\n";

        PicoContainer pico = buildContainer(script);
        Dao intercepted = (Dao) pico.getComponent("intercepted");
        Dao notIntercepted = (Dao) pico.getComponent("notIntercepted");
        StringBuffer log = (StringBuffer) pico.getComponent("log");

        verifyIntercepted(intercepted, log);
        verifyNotIntercepted(notIntercepted, log);
    }

    @Test public void testContainerScopedMixin() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container() {\n" +
                "    component(key:Dao, class:DaoImpl) \n" +
                "    aspect(classCut:cuts.instancesOf(Dao), mixinClass:IdentifiableMixin)\n" +
                "}";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        verifyMixin(dao);
    }

    @Test public void testExplicitAspectsManagerAndDecorator() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "aspectsManager = new org.picocontainer.aop.dynaop.DynaopAspectsManager()\n" +
                "cuts = aspectsManager.getPointcutsFactory()\n" +
                "decorator = new org.picocontainer.aop.dynaop.DynaopAspectableNodeBuilderDecorator(aspectsManager)\n" +
                "builder = new GroovyNodeBuilder(decorator) \n" +
                "scripted = builder.container() {\n" +
                "    component(key:Dao, class:DaoImpl) \n" +
                "    aspect(classCut:cuts.instancesOf(Dao), mixinClass:IdentifiableMixin)\n" +
                "}";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        verifyMixin(dao);
    }

    @Test public void testCustomComponentFactory() {
        String script = "" +
                "package org.picocontainer.script.groovy\n" +
                "import org.picocontainer.script.testmodel.*\n" +
                "import org.picocontainer.aop.dynaop.*\n" +
                "intLog = new StringBuffer()\n" +
                "logger = new LoggingInterceptor(intLog)\n" +
                "componentFactoryLog = new StringBuffer()\n" +
                "componentFactory = new TestInjection(componentFactoryLog)\n" +
                "cuts = new DynaopPointcutsFactory()\n" +
                "builder = new DynaopGroovyNodeBuilder()\n" +
                "scripted = builder.container(componentFactory:componentFactory) {\n" +
                "    aspect(classCut:cuts.instancesOf(Dao.class), methodCut:cuts.allMethods(), interceptor:logger)\n" +
                "    component(key:Dao, class:DaoImpl)\n" +
                "    component(key:'intLog', instance:intLog)\n" +
                "    component(key:'componentFactoryLog', instance:componentFactoryLog)\n" +
                "}";

        PicoContainer pico = buildContainer(script);
        Dao dao = pico.getComponent(Dao.class);
        StringBuffer intLog = (StringBuffer) pico.getComponent("intLog");
        verifyIntercepted(dao, intLog);
        StringBuffer componentFactoryLog = (StringBuffer) pico.getComponent("componentFactoryLog");
        assertEquals("called", componentFactoryLog.toString());
    }

    private PicoContainer buildContainer(String script) {
        GroovyContainerBuilder builder = new GroovyContainerBuilder(new StringReader(script), getClass().getClassLoader());
        return buildContainer(builder, null, "SOME_SCOPE");
    }

    private void verifyIntercepted(Dao dao, StringBuffer log) {
        String before = log.toString();
        String data = dao.loadData();
        assertEquals("data", data);
        assertEquals(before + "startend", log.toString());
    }

    private void verifyNotIntercepted(Dao dao, StringBuffer log) {
        String before = log.toString();
        String data = dao.loadData();
        assertEquals("data", data);
        assertEquals(before, log.toString());
    }

    private void verifyMixin(Object component) {
        assertTrue(component instanceof Identifiable);
        Identifiable identifiable = (Identifiable) component;
        identifiable.setId("id");
        assertEquals("id", identifiable.getId());
    }

}