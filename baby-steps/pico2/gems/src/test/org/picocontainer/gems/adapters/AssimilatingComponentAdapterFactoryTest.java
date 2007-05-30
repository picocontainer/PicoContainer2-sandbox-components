/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Joerg Schaibe                                            *
 *****************************************************************************/

package org.picocontainer.gems.adapters;

import org.picocontainer.defaults.ComponentFactory;
import org.picocontainer.adapters.ConstructorInjectionComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.DuplicateComponentKeyRegistrationException;
import org.picocontainer.tck.AbstractComponentAdapterFactoryTestCase;
import org.picocontainer.testmodel.AlternativeTouchable;
import org.picocontainer.testmodel.CompatibleTouchable;
import org.picocontainer.testmodel.SimpleTouchable;
import org.picocontainer.testmodel.Touchable;

import java.util.List;


/**
 * @author J&ouml;rg Schaible
 */
public class AssimilatingComponentAdapterFactoryTest extends AbstractComponentAdapterFactoryTestCase {

    /**
     * @see org.picocontainer.tck.AbstractComponentAdapterFactoryTestCase#createComponentAdapterFactory()
     */
    protected ComponentFactory createComponentAdapterFactory() {
        return new AssimilatingComponentAdapterFactory(Touchable.class).forThis(new ConstructorInjectionComponentAdapterFactory());
    }

    /**
     * Test automatic assimilation of registered components.
     */
    public void testAutomaticAssimilation() {
        picoContainer = new DefaultPicoContainer(createComponentAdapterFactory());
        picoContainer.addComponent(SimpleTouchable.class);
        picoContainer.addComponent(AlternativeTouchable.class);
        picoContainer.addComponent(CompatibleTouchable.class);
        final List list = picoContainer.getComponents(Touchable.class);
        assertEquals(3, list.size());
    }

    /**
     * Test automatic assimilation of registered components.
     */
    public void testOnlyOneTouchableComponentKeyPossible() {
        picoContainer = new DefaultPicoContainer(createComponentAdapterFactory());
        picoContainer.addComponent(Touchable.class, SimpleTouchable.class);
        try {
            picoContainer.addComponent(CompatibleTouchable.class);
            fail("DuplicateComponentKeyRegistrationException expected");
        } catch (final DuplicateComponentKeyRegistrationException e) {
            // fine
        }
    }

    /**
     * Test automatic assimilation of registered components.
     */
    public void testMultipleAssimilatedComponentsWithUserDefinedKeys() {
        picoContainer = new DefaultPicoContainer(createComponentAdapterFactory());
        picoContainer.addComponent(Touchable.class, SimpleTouchable.class);
        picoContainer.addComponent("1", CompatibleTouchable.class);
        picoContainer.addComponent("2", CompatibleTouchable.class);
        picoContainer.addComponent("3", CompatibleTouchable.class);
        final List list = picoContainer.getComponents(Touchable.class);
        assertEquals(4, list.size());
    }
}
