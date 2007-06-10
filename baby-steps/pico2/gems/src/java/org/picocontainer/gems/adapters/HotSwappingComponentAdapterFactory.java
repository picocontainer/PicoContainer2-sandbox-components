/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/

package org.picocontainer.gems.adapters;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.ComponentCharacteristic;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.LifecycleStrategy;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.injectors.AnyInjectionFactory;
import org.picocontainer.behaviors.AbstractBehaviorFactory;


/**
 * Hides implementation.
 * 
 * @author Paul Hammant
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 * @see HotSwappingComponentAdapter
 */
public class HotSwappingComponentAdapterFactory extends AbstractBehaviorFactory {

    public HotSwappingComponentAdapterFactory() {
        forThis(new AnyInjectionFactory());
    }

    public ComponentAdapter createComponentAdapter(ComponentMonitor componentMonitor, LifecycleStrategy lifecycleStrategy, ComponentCharacteristic componentCharacteristic, Object componentKey, Class componentImplementation, Parameter... parameters)
            throws PicoIntrospectionException, PicoInitializationException {
        ComponentAdapter componentAdapter = super.createComponentAdapter(componentMonitor, lifecycleStrategy, componentCharacteristic, componentKey, componentImplementation, parameters);
        return new HotSwappingComponentAdapter(componentAdapter);
    }
}
