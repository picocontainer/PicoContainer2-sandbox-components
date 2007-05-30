/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.picocontainer.adapters;

import org.picocontainer.tck.AbstractComponentAdapterFactoryTestCase;
import org.picocontainer.testmodel.SimpleTouchable;
import org.picocontainer.testmodel.Touchable;
import org.picocontainer.adapters.ConstructorInjectionFactory;
import org.picocontainer.adapters.NonCachingBehaviorFactory;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.ComponentFactory;
import org.picocontainer.ComponentCharacteristics;

import java.util.Map;
import java.util.HashMap;

/**
 * @author <a href="Rafal.Krzewski">rafal@caltha.pl</a>
 * @version $Revision$
 */
public class NonCachingBehaviorFactoryTestCase extends AbstractComponentAdapterFactoryTestCase {
    protected void setUp() throws Exception {
        picoContainer = new DefaultPicoContainer(createComponentAdapterFactory());
    }

    protected ComponentFactory createComponentAdapterFactory() {
        return new NonCachingBehaviorFactory().forThis(new ConstructorInjectionFactory());
    }

    public void testContainerReturnsSameInstanceEachCall() {
        picoContainer.addComponent(Touchable.class, SimpleTouchable.class);
        Touchable t1 = (Touchable) picoContainer.getComponent(Touchable.class);
        Touchable t2 = (Touchable) picoContainer.getComponent(Touchable.class);
        assertNotSame(t1, t2);
    }

    public void testContainerCanFollowNOCACHEDirectiveSelectively() {
        picoContainer.addComponent(Touchable.class, SimpleTouchable.class);
        picoContainer.change(ComponentCharacteristics.CACHE);
        picoContainer.addComponent(Map.class, HashMap.class);
        assertNotSame(picoContainer.getComponent(Touchable.class), picoContainer.getComponent(Touchable.class));
        assertSame(picoContainer.getComponent(Map.class), picoContainer.getComponent(Map.class));
    }

    public void testContainerCanFollowSINGLETONDirectiveSelectively() {
        picoContainer.addComponent(Touchable.class, SimpleTouchable.class);
        picoContainer.change(ComponentCharacteristics.SINGLETON);
        picoContainer.addComponent(Map.class, HashMap.class);
        assertNotSame(picoContainer.getComponent(Touchable.class), picoContainer.getComponent(Touchable.class));
        assertSame(picoContainer.getComponent(Map.class), picoContainer.getComponent(Map.class));
    }



}