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

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.ComponentCharacteristic;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.defaults.LifecycleStrategy;
import org.picocontainer.adapters.AbstractBehaviorDecoratorFactory;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class SynchronizedBehaviorFactory extends AbstractBehaviorDecoratorFactory {

    public ComponentAdapter createComponentAdapter(ComponentMonitor componentMonitor, LifecycleStrategy lifecycleStrategy, ComponentCharacteristic registerationCharacteristic, Object componentKey, Class componentImplementation, Parameter... parameters) {
        return new SynchronizedBehaviorAdapter(super.createComponentAdapter(componentMonitor, lifecycleStrategy, registerationCharacteristic, componentKey, componentImplementation, parameters));
    }
}