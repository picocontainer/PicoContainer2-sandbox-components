/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.nanocontainer.integrationkit;

import org.picocontainer.ObjectReference;
import org.picocontainer.PicoContainer;

/**
 * The responsibility of a ContainerBuilder is to <em>instantiate</em> and <em>compose</em> containers.
 * (Composition means assembly (registration) and configuration (setting primitive parameters) of
 * components).
 *
 * @author Joe Walnes
 */
public interface ContainerBuilder {

    /**
     * Create, assemble, init and start a new PicoContainer and store it
     * at a given reference.
     *
     * @param containerRef       Where to store the new container.
     * @param parentContainerRef reference to a container that may be used as a parent to the new container (may be null).
     * @param compositionScope   Hint about the scope for composition.
     * @param addChildToParent   Add the child to the parent
     */
    void buildContainer(ObjectReference<PicoContainer> containerRef, ObjectReference<PicoContainer> parentContainerRef, Object compositionScope, boolean addChildToParent);

    /**
     * Locate a container at the given reference so it can be stopped,
     * destroyed and removed.
     *
     * @param containerRef Where the container is stored.
     */
    void killContainer(ObjectReference<PicoContainer> containerRef);

}

