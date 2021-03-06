/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by various                           *
 *****************************************************************************/
package org.nanocontainer.testmodel;



/**
 * @author Stephen Molitor
 * @version $Revision: 1631 $
 */
public class IdGeneratorImpl implements IdGenerator {

    private int nextId = 0;

    public Integer nextId() {
        return ++nextId;
    }

}