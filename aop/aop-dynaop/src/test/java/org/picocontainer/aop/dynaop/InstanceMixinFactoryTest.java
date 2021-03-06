/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.
 * --------------------------------------------------------------------------
 * The software in this package is published under the terms of the BSD style
 * license a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *****************************************************************************/
package org.picocontainer.aop.dynaop;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.picocontainer.aop.dynaop.InstanceMixinFactory;

import dynaop.MixinFactory;

/**
 * @author Stephen Molitor
 */
public class InstanceMixinFactoryTest {

    @Test public void testCreate() {
        Object instance = "foo";
        MixinFactory factory = new InstanceMixinFactory(instance);
        assertSame(instance, factory.create(null));
    }

}
