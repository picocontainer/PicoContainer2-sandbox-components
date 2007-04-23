package org.picocontainer.doc.introduction;

import junit.framework.TestCase;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.UnsatisfiableDependenciesException;

/**
 * @author Aslak Helles&oslash;y
 * @version $Revision$
 */
public class HierarchyTestCase extends TestCase {
    public void testHierarchy() {
        try {
            // START SNIPPET: wontwork
            // Create x hierarchy of containers
            MutablePicoContainer x = new DefaultPicoContainer();
            MutablePicoContainer y = new DefaultPicoContainer( x );
            MutablePicoContainer z = new DefaultPicoContainer( x );

            // Assemble components
            x.registerComponent(Apple.class);
            y.registerComponent(Juicer.class);
            z.registerComponent(Peeler.class);

            // Instantiate components
            Peeler peeler = (Peeler) z.getComponent(Peeler.class);
            // WON'T WORK! peeler will be null
            peeler = (Peeler) x.getComponent(Peeler.class);
            // WON'T WORK! This will throw an exception
            Juicer juicer = (Juicer) y.getComponent(Juicer.class);
            // END SNIPPET: wontwork
        } catch (UnsatisfiableDependenciesException e) {
            // expected
        }
    }

}