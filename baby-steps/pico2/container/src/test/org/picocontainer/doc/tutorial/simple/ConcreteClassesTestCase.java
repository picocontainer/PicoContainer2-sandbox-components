package org.picocontainer.doc.tutorial.simple;

import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class ConcreteClassesTestCase extends TestCase {

    public void testAssembleComponentsAndInstantiateAndUseThem() {
        // START SNIPPET: assemble
        MutablePicoContainer pico = new DefaultPicoContainer();
        pico.component(Boy.class);
        pico.component(Girl.class);
        // END SNIPPET: assemble

        // START SNIPPET: instantiate-and-use
        Girl girl = (Girl) pico.getComponent(Girl.class);
        girl.kissSomeone();
        // END SNIPPET: instantiate-and-use
    }


}
