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

import com.thoughtworks.proxy.ProxyFactory;
import com.thoughtworks.proxy.factory.StandardProxyFactory;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.Parameter;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.ComponentCharacteristic;
import org.picocontainer.adapters.AnyInjectionComponentAdapterFactory;
import org.picocontainer.adapters.DecoratingComponentAdapterFactory;
import org.picocontainer.defaults.AssignabilityRegistrationException;
import org.picocontainer.defaults.ComponentAdapterFactory;
import org.picocontainer.defaults.NotConcreteRegistrationException;


/**
 * Hides implementation.
 * 
 * @author Paul Hammant
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 * @see HotSwappingComponentAdapter
 */
public class HotSwappingComponentAdapterFactory extends DecoratingComponentAdapterFactory {
    private final ProxyFactory proxyFactory;

    public HotSwappingComponentAdapterFactory() {
        this(new AnyInjectionComponentAdapterFactory());
    }

    public HotSwappingComponentAdapterFactory(ComponentAdapterFactory delegate) {
        this(delegate, new StandardProxyFactory());
    }

    public HotSwappingComponentAdapterFactory(ComponentAdapterFactory delegate, ProxyFactory proxyFactory) {
        super(delegate);
        this.proxyFactory = proxyFactory;
    }

    public ComponentAdapter createComponentAdapter(ComponentCharacteristic registerationCharacteristic, Object componentKey, Class componentImplementation, Parameter... parameters)
            throws PicoIntrospectionException, AssignabilityRegistrationException, NotConcreteRegistrationException {
        ComponentAdapter componentAdapter = super.createComponentAdapter(registerationCharacteristic, componentKey, componentImplementation, parameters);
        return new HotSwappingComponentAdapter(componentAdapter, proxyFactory);
    }
}
