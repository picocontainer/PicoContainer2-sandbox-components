/*****************************************************************************
 * Copyright (c) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Idea by Rachel Davies, Original code by Jon Tirsen                        *
 *****************************************************************************/

package org.picocontainer;

/**
 * This class provides control over the arguments that will be passed to a constructor. It can be used for finer control over
 * what arguments are passed to a particular constructor.
 * 
 * @author Jon Tirs&eacute;n
 * @author Aslak Helles&oslash;y
 * @author Thomas Heller
 * @see MutablePicoContainer#addComponent(Object,Class,Parameter...) a method on the
 *      {@link MutablePicoContainer} interface which allows passing in of an array of {@linkplain Parameter Parameters}.
 * @see org.picocontainer.defaults.ComponentParameter an implementation of this interface that allows you to specify the key
 *      used for resolving the parameter.
 * @see org.picocontainer.defaults.ConstantParameter an implementation of this interface that allows you to specify a constant
 *      that will be used for resolving the parameter.
 * @since 1.0
 */
public interface Parameter {
    Parameter[] ZERO = new Parameter[0];

    /**
     * Retrieve the object from the Parameter that statisfies the expected type.
     * 
     * @param container the container from which dependencies are resolved.
     * @param adapter the {@link org.picocontainer.ComponentAdapter} that is asking for the instance
     * @param expectedType the type that the returned instance needs to match.
     * @param expectedParameterName
     * @return the instance or <code>null</code> if no suitable instance can be found.
     * @throws PicoInitializationException if a referenced addComponent could not be instantiated.
     * @since 1.1
     */
    Object resolveInstance(PicoContainer container, ComponentAdapter adapter, Class expectedType, ParameterName expectedParameterName);

    /**
     * Check if the Parameter can statisfy the expected type using the container.
     * 
     * @param container the container from which dependencies are resolved.
     * @param adapter the {@link ComponentAdapter} that is asking for the instance
     * @param expectedType the required type
     * @param expectedParameterName
     * @return <code>true</code> if the addComponent parameter can be resolved.
     * @since 1.1
     */
    boolean isResolvable(PicoContainer container, ComponentAdapter adapter, Class expectedType, ParameterName expectedParameterName);

    /**
     * Verify that the Parameter can statisfied the expected type using the container
     * 
     * @param container the container from which dependencies are resolved.
     * @param adapter the {@link org.picocontainer.ComponentAdapter} that is asking for the verification
     * @param expectedType the required type
     * @param expectedParameterName
     * @throws PicoIntrospectionException if parameter and its dependencies cannot be resolved
     * @since 1.1
     */
    void verify(PicoContainer container, ComponentAdapter adapter, Class expectedType, ParameterName expectedParameterName);

    /**
     * Accepts a visitor for this Parameter. The method is normally called by visiting a {@link ComponentAdapter}, that
     * cascades the {@linkplain PicoVisitor visitor} also down to all its {@linkplain Parameter Parameters}.
     * 
     * @param visitor the visitor.
     * @since 1.1
     */
    void accept(PicoVisitor visitor);
}
