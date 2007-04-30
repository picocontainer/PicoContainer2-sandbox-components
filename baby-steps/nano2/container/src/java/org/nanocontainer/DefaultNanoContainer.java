/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by Paul Hammant                                             *
 *****************************************************************************/

package org.nanocontainer;

import java.io.Serializable;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.ComponentMonitorStrategy;
import org.picocontainer.componentadapters.CachingAndConstructorComponentAdapterFactory;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.LifecycleStrategy;

/**
 * This is a MutablePicoContainer that also supports soft composition. i.e. assembly by class name rather that class
 * reference.
 * <p>
 * In terms of implementation it adopts the behaviour of DefaultPicoContainer and DefaultNanoContainer.</p>
 *
 * @author Paul Hammant
 * @author Mauro Talevi
 * @author Michael Rimov
 * @version $Revision$
 */
public class DefaultNanoContainer extends AbstractNanoContainer implements NanoPicoContainer, Serializable,
    ComponentMonitorStrategy {

    public DefaultNanoContainer(ClassLoader classLoader, ComponentAdapterFactory caf, PicoContainer parent) {
        super(new DefaultPicoContainer(caf, parent), classLoader);
    }

    public DefaultNanoContainer(ClassLoader classLoader, PicoContainer parent) {
        super(new DefaultPicoContainer(new CachingAndConstructorComponentAdapterFactory(), parent), classLoader);
    }

    public DefaultNanoContainer(ClassLoader classLoader, MutablePicoContainer pico) {
        super(pico, classLoader);
    }

    public DefaultNanoContainer(ClassLoader classLoader, PicoContainer parent, ComponentMonitor componentMonitor) {
        super(new DefaultPicoContainer(new CachingAndConstructorComponentAdapterFactory(componentMonitor), parent), classLoader);
    }

    public DefaultNanoContainer(ComponentAdapterFactory caf) {
        super(new DefaultPicoContainer(caf, null), DefaultNanoContainer.class.getClassLoader());
    }

    public DefaultNanoContainer(PicoContainer parent) {
        super(new DefaultPicoContainer(parent), DefaultNanoContainer.class.getClassLoader());
    }

    public DefaultNanoContainer(MutablePicoContainer pico) {
        super(pico, DefaultNanoContainer.class.getClassLoader());
    }

    public DefaultNanoContainer(ClassLoader classLoader) {
        super(new DefaultPicoContainer(), classLoader);
    }

    public DefaultNanoContainer() {
        super(new DefaultPicoContainer(), DefaultNanoContainer.class.getClassLoader());
    }


    /**
     * Constructor that provides the same control over the nanocontainer lifecycle strategies
     * as {@link DefaultPicoContainer(ComponentAdapterFactory, LifecycleStrategy, PicoContainer)}.
     * @param componentAdapterFactory ComponentAdapterFactory
     * @param lifecycleStrategyForInstanceRegistrations LifecycleStrategy
     * @param parent PicoContainer may be null if there is no parent.
     * @param cl the Classloader to use.  May be null, in which case DefaultNanoPicoContainer.class.getClassLoader()
     * will be called instead.
     */
    public DefaultNanoContainer(ComponentAdapterFactory componentAdapterFactory,
        LifecycleStrategy lifecycleStrategyForInstanceRegistrations, PicoContainer parent, ClassLoader cl) {

        super(new DefaultPicoContainer(componentAdapterFactory,
            lifecycleStrategyForInstanceRegistrations, parent),
            //Use a default classloader if none is specified.
            (cl != null) ? cl : DefaultNanoContainer.class.getClassLoader());
    }


    protected AbstractNanoContainer createChildContainer() {
        MutablePicoContainer child = getDelegate().makeChildContainer();
        return new DefaultNanoContainer(child);
     }

    public void changeMonitor(ComponentMonitor monitor) {
        ((ComponentMonitorStrategy)getDelegate()).changeMonitor(monitor);
    }

    public ComponentMonitor currentMonitor() {
        return ((ComponentMonitorStrategy)getDelegate()).currentMonitor();
    }

}