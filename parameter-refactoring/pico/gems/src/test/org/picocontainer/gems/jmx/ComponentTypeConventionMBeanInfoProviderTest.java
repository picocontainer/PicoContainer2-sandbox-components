/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Joerg Schaible                                           *
 *****************************************************************************/

package org.picocontainer.gems.jmx;

import javax.management.MBeanInfo;

import org.picocontainer.gems.jmx.testmodel.Person;
import org.picocontainer.gems.jmx.testmodel.PersonMBeanInfo;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.DefaultPicoContainer;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;


/**
 * @author J&ouml;rg Schaible
 */
public class ComponentTypeConventionMBeanInfoProviderTest extends MockObjectTestCase {

    private MutablePicoContainer pico;
    private MBeanInfoProvider mBeanProvider;

    protected void setUp() throws Exception {
        super.setUp();
        pico = new DefaultPicoContainer();
        mBeanProvider = new ComponentTypeConventionMBeanInfoProvider();
    }

    public void testMBeanInfoIsDeterminedFromComponentType() {
        final ComponentAdapter componentAdapter = pico.addComponent("JUnit", Person.class).getComponentAdapter("JUnit");
        pico.addComponent(Person.class.getName() + "MBeanInfo", Person.createMBeanInfo());

        final MBeanInfo info = mBeanProvider.provide(pico, componentAdapter);
        assertNotNull(info);
        assertEquals(Person.createMBeanInfo().getDescription(), info.getDescription());
    }

    public void testSpecificMBeanInfoIsFoundByType() {
        final Person person = new Person();

        final Mock mockComponentAdapter = mock(ComponentAdapter.class);
        mockComponentAdapter.stubs().method("getComponentKey").will(returnValue(Person.class));
        mockComponentAdapter.stubs().method("getComponentImplementation").will(returnValue(person.getClass()));

        pico.addAdapter((ComponentAdapter)mockComponentAdapter.proxy());
        pico.addComponent(PersonMBeanInfo.class);

        final MBeanInfo info = mBeanProvider.provide(pico, (ComponentAdapter)mockComponentAdapter.proxy());
        assertNotNull(info);
        assertEquals(Person.createMBeanInfo().getDescription(), info.getDescription());
    }

}
