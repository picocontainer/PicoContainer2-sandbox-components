/*****************************************************************************
 * Copyright (C) NanoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.nanocontainer.nanowar;

import org.picocontainer.ObjectReference;

import javax.servlet.ServletContext;

/**
 * References an object that lives as an attribute of the
 * ServletContext (application scope)
 *
 * @author Joe Walnes
 */
public final class ApplicationScopeReference<T> implements ObjectReference<T> {

    private final ServletContext context;
    private final String key;

    public ApplicationScopeReference(ServletContext context, String key) {
        this.context = context;
        this.key = key;
    }

    public void set(T item) {
        context.setAttribute(key, item);
    }

    public T get() {
        return (T) context.getAttribute(key);
    }

}
