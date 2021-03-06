/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.
 * --------------------------------------------------------------------------
 * The software in this package is published under the terms of the BSD style
 * license a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *****************************************************************************/
package org.picocontainer.aop.dynaop;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.aop.dynaop.ContainerSuppliedMixinFactory;
import org.picocontainer.script.testmodel.IdentifiableMixin;

import dynaop.MixinFactory;

/**
 * @author Stephen Molitor
 */
public class ContainerSuppliedMixinFactoryTest {

    private final MutablePicoContainer pico = new DefaultPicoContainer();
    private final MixinFactory mixinFactory = new ContainerSuppliedMixinFactory(pico, IdentifiableMixin.class);

    @Test public void testCreate() {
        Object mixin = mixinFactory.create(null);
        assertTrue(mixin instanceof IdentifiableMixin);
    }

}