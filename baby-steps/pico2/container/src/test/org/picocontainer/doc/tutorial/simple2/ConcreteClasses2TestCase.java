package org.picocontainer.doc.tutorial.simple2;

import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.doc.tutorial.interfaces.Kissable;
import org.picocontainer.doc.tutorial.simple.Girl;
import org.picocontainer.defaults.DefaultPicoContainer;

public class ConcreteClasses2TestCase extends TestCase {

    public void testAssembleComponentsAndInstantiateAndUseThem() {
        // START SNIPPET: assemble
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.component(Kissable.class, Boy.class);
        pico.component(Girl.class);
        // END SNIPPET: assemble

    }


}
