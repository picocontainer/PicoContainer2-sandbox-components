/*******************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved. 
 * ---------------------------------------------------------------------------
 * The software in this package is published under the terms of the BSD style
 * license a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 ******************************************************************************/
package org.picocontainer.web.script;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.script.groovy.GroovyContainerBuilder;

/**
 * @author Mauro Talevi
 */
public class ScriptedWebappComposerTestCase {

    @Test
    public void canComposedHierarchyWithDefaultConfiguration() {
        assertComposedHierarchy(new ScriptedWebappComposer());
    }

    @Test
    public void canComposedHierarchyWithCustomConfiguration() {
        assertComposedHierarchy(new ScriptedWebappComposer(GroovyContainerBuilder.class.getName(),
                "pico-application.groovy", "pico-session.groovy", "pico-request.groovy"));
    }

    @Test(expected = PicoCompositionException.class)
    public void cannotComposedHierarchyWithUnknownResource() {
        assertComposedHierarchy(new ScriptedWebappComposer(ScriptedWebappComposer.DEFAULT_CONTAINER_BUILDER,
                "unknown-resource.xml", null, null));
    }

    private void assertComposedHierarchy(ScriptedWebappComposer composer) {
        MutablePicoContainer applicationContainer = new DefaultPicoContainer();
        composer.composeApplication(applicationContainer);
        assertNotNull(applicationContainer.getComponent("applicationScopedInstance"));
        assertNotNull(applicationContainer.getComponent("testFoo"));

        MutablePicoContainer sessionContainer = new DefaultPicoContainer(applicationContainer);

        composer.composeSession(sessionContainer);
        assertNotNull(sessionContainer.getComponent("applicationScopedInstance"));
        assertNotNull(sessionContainer.getComponent("sessionScopedInstance"));

        MutablePicoContainer requestContainer = new DefaultPicoContainer(sessionContainer);
        composer.composeRequest(requestContainer);
        assertNotNull(requestContainer.getComponent("applicationScopedInstance"));
        assertNotNull(requestContainer.getComponent("sessionScopedInstance"));
        assertNotNull(requestContainer.getComponent("requestScopedInstance"));
        assertNotNull(requestContainer.getComponent("testFooHierarchy"));
    }

}