/*****************************************************************************
 * Copyright (C) PicoContainer Organization. All rights reserved.            *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by                                                          *
 *****************************************************************************/
package org.picocontainer.doc.advanced;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.behaviors.Caching;
import org.picocontainer.injectors.ConstructorInjection;
import org.picocontainer.parameters.Array;
import org.picocontainer.parameters.ByKeyClass;


/**
 * @author Aslak Helles&oslash;y
 * @author J&ouml;rg Schaible
 */
public class ArraysTestCase
        extends TestCase
        implements CollectionDemoClasses {
    private MutablePicoContainer pico;

    protected void setUp() throws Exception {
        pico = new DefaultPicoContainer(new Caching().wrap(new ConstructorInjection()));
    }

    private void explanation() {
        // START SNIPPET: explanation

        Shark shark = new Shark();
        Cod cod = new Cod();

        Fish[] fishes = new Fish[]{shark, cod};
        Cod[] cods = new Cod[]{cod};

        Bowl bowl = new Bowl(fishes, cods);
        // END SNIPPET: explanation
    }

    // START SNIPPET: bowl

    public static class Bowl {
        private final Fish[] fishes;
        private final Cod[] cods;

        public Bowl(Fish[] fishes, Cod[] cods) {
            this.fishes = fishes;
            this.cods = cods;
        }

        public Fish[] getFishes() {
            return fishes;
        }

        public Cod[] getCods() {
            return cods;
        }
    }

    // END SNIPPET: bowl

    public void testShouldCreateBowlWithFishCollection() {

        //      START SNIPPET: usage

        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        pico.addComponent(Bowl.class);

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: usage

        Shark shark = pico.getComponent(Shark.class);
        Cod cod = pico.getComponent(Cod.class);

        List fishes = Arrays.asList(bowl.getFishes());
        assertEquals(2, fishes.size());
        assertTrue(fishes.contains(shark));
        assertTrue(fishes.contains(cod));

        List cods = Arrays.asList(bowl.getCods());
        assertEquals(1, cods.size());
        assertTrue(cods.contains(cod));
    }

    public void testShouldCreateBowlWithCodsOnly() {

        //      START SNIPPET: directUsage

        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        pico.addComponent(Bowl.class);
        pico.addComponent(new Fish[]{});

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: directUsage

        Cod cod = pico.getComponent(Cod.class);

        //      START SNIPPET: directDemo

        List cods = Arrays.asList(bowl.getCods());
        assertEquals(1, cods.size());

        List fishes = Arrays.asList(bowl.getFishes());
        assertEquals(0, fishes.size());
        //      END SNIPPET: directDemo

        assertTrue(cods.contains(cod));
    }

    public void testShouldCreateBowlWithFishCollectionAnyway() {

        //      START SNIPPET: ensureArray

        pico.addComponent(Shark.class);
        pico.addComponent(Cod.class);
        
        pico.addComponent(Bowl.class, Bowl.class, new Array(Fish.class), new Array(Cod.class));
        pico.addComponent(new Fish[]{});
        pico.addComponent(new Cod[]{});

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: ensureArray

        Shark shark = pico.getComponent(Shark.class);
        Cod cod = pico.getComponent(Cod.class);

        //      START SNIPPET: ensureDemo

        List fishes = Arrays.asList(bowl.getFishes());
        assertEquals(2, fishes.size());

        List cods = Arrays.asList(bowl.getCods());
        assertEquals(1, cods.size());
        //      END SNIPPET: ensureDemo

        assertTrue(fishes.contains(shark));
        assertTrue(fishes.contains(cod));
        assertTrue(cods.contains(cod));
    }

    public void testShouldCreateBowlWithNoFishAtAll() {

        //      START SNIPPET: emptyArray

        pico.addComponent(Bowl.class, Bowl.class, new Array(Fish.class,true), new Array(Cod.class,true));

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: emptyArray

        List fishes = Arrays.asList(bowl.getFishes());
        assertEquals(0, fishes.size());
        List cods = Arrays.asList(bowl.getCods());
        assertEquals(0, cods.size());
    }

    public void testShouldCreateBowlWithNamedFishesOnly() {

        //      START SNIPPET: useKeyType

        pico.addComponent(Shark.class);
        pico.addComponent("Nemo", Cod.class);
        pico.addComponent(Bowl.class, Bowl.class,
                          new Array(new ByKeyClass(String.class), Fish.class), new Array(Cod.class));

        Bowl bowl = pico.getComponent(Bowl.class);
        //      END SNIPPET: useKeyType

        //      START SNIPPET: ensureKeyType

        List fishes = Arrays.asList(bowl.getFishes());
        List cods = Arrays.asList(bowl.getCods());
        assertEquals(1, fishes.size());
        assertEquals(1, cods.size());
        assertEquals(fishes, cods);
        //      END SNIPPET: ensureKeyType
    }
}