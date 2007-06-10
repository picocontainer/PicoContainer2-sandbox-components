package org.picocontainer.doc.advanced;

import junit.framework.TestCase;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.parameters.CollectionComponentParameter;
import org.picocontainer.parameters.ComponentParameter;
import org.picocontainer.DefaultPicoContainer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * @author J&ouml;rg Schaible
 * @version $Revision$
 */
public class CollectionsTestCase
        extends TestCase
        implements CollectionDemoClasses {
    private MutablePicoContainer pico;

    protected void setUp() throws Exception {
        pico = new DefaultPicoContainer();
    }

    // START SNIPPET: bowl

    public static class Bowl {
        private final LinkedList fishes;
        private final Collection cods;

        public Bowl(LinkedList fishes, Collection cods) {
            this.fishes = fishes;
            this.cods = cods;
        }

        public Collection getFishes() {
            return fishes;
        }

        public Collection getCods() {
            return cods;
        }
    }

    // END SNIPPET: bowl

    public void testShouldCreateBowlWithFishCollection() {

        //      START SNIPPET: usage

        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        pico.addComponent(Bowl.class, Bowl.class,
                          new ComponentParameter(Fish.class, false), new ComponentParameter(Cod.class, false));
        //      END SNIPPET: usage

        Shark shark = pico.getComponent(Shark.class);
        Cod cod = pico.getComponent(Cod.class);
        Bowl bowl = pico.getComponent(Bowl.class);

        Collection fishes = bowl.getFishes();
        assertEquals(2, fishes.size());
        assertTrue(fishes.contains(shark));
        assertTrue(fishes.contains(cod));

        Collection cods = bowl.getCods();
        assertEquals(1, cods.size());
        assertTrue(cods.contains(cod));
    }

    public void testShouldCreateBowlWithFishesOnly() {

        //      START SNIPPET: directUsage

        final Set set = new HashSet();
        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        pico.addComponent(Bowl.class, Bowl.class,
                          new ComponentParameter(Fish.class, false), new ComponentParameter(Cod.class, false));
        pico.addComponent(set);

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: directUsage

        Shark shark = pico.getComponent(Shark.class);
        Cod cod = pico.getComponent(Cod.class);

        //      START SNIPPET: directDemo

        Collection cods = bowl.getCods();
        assertEquals(0, cods.size());
        assertSame(set, cods);

        Collection fishes = bowl.getFishes();
        assertEquals(2, fishes.size());
        //      END SNIPPET: directDemo

        assertTrue(fishes.contains(cod));
        assertTrue(fishes.contains(shark));
    }

    public void testShouldCreateBowlWithFishCollectionAnyway() {

        //      START SNIPPET: ensureCollection

        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        pico.addComponent(Bowl.class, Bowl.class,
                          new CollectionComponentParameter(Fish.class, false), new CollectionComponentParameter(Cod.class, false));
        // This addComponent will match both arguments of Bowl's constructor
        pico.addComponent(new LinkedList());

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: ensureCollection

        Shark shark = pico.getComponent(Shark.class);
        Cod cod = pico.getComponent(Cod.class);

        Collection fishes = bowl.getFishes();
        assertEquals(2, fishes.size());
        Collection cods = bowl.getCods();
        assertEquals(1, cods.size());

        assertTrue(fishes.contains(shark));
        assertTrue(fishes.contains(cod));
        assertTrue(cods.contains(cod));
    }

    public void testShouldCreateBowlWithNoFishAtAll() {

        //      START SNIPPET: emptyCollection

        pico.addComponent(Bowl.class, Bowl.class,
                          new ComponentParameter(Fish.class, true), new ComponentParameter(Cod.class, true));

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: emptyCollection

        Collection fishes = bowl.getFishes();
        assertEquals(0, fishes.size());
        Collection cods = bowl.getCods();
        assertEquals(0, cods.size());
    }
}