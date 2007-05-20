/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Aslak Hellesoy and Paul Hammant   *
 *****************************************************************************/
package org.picocontainer.adapters;

import org.picocontainer.adapters.ConstructorInjectionComponentAdapterFactory;
import org.picocontainer.monitors.NullComponentMonitor;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.adapters.ConstructorInjectionComponentAdapter;
import org.picocontainer.tck.AbstractComponentAdapterFactoryTestCase;
import org.picocontainer.tck.AbstractComponentAdapterTestCase.RecordingLifecycleStrategy;
import org.picocontainer.testmodel.NullLifecycle;
import org.picocontainer.testmodel.RecordingLifecycle;
import org.picocontainer.testmodel.RecordingLifecycle.One;
import org.picocontainer.lifecycle.NullLifecycleStrategy;

/**
 * @author Mauro Talevi
 * @version $Revision:  $
 */
public class ConstructorInjectionComponentAdapterFactoryTestCase extends AbstractComponentAdapterFactoryTestCase {
    protected void setUp() throws Exception {
        picoContainer = new DefaultPicoContainer(createComponentAdapterFactory());
    }

    protected ComponentAdapterFactory createComponentAdapterFactory() {
        return new ConstructorInjectionComponentAdapterFactory();
    }

    public void testCustomLifecycleCanBeInjected() throws NoSuchMethodException {
        RecordingLifecycleStrategy strategy = new RecordingLifecycleStrategy(new StringBuffer());
        ConstructorInjectionComponentAdapterFactory caf =
            new ConstructorInjectionComponentAdapterFactory();
        ConstructorInjectionComponentAdapter cica =  (ConstructorInjectionComponentAdapter)
        caf.createComponentAdapter(new NullComponentMonitor(), strategy, null, NullLifecycle.class, NullLifecycle.class);
        One one = new RecordingLifecycle.One(new StringBuffer());
        cica.start(one);
        cica.stop(one);        
        cica.dispose(one);
        assertEquals("<start<stop<dispose", strategy.recording());
    }    

}