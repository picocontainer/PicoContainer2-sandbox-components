package org.picocontainer.gems.adapters;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.ComponentFactory;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.injectors.AnyInjectionFactory;
import org.picocontainer.injectors.ConstructorInjector;
import org.picocontainer.injectors.ConstructorInjectionFactory;
import org.picocontainer.tck.AbstractComponentAdapterFactoryTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public final class HotSwappingBehaviorFactoryTestCase extends AbstractComponentAdapterFactoryTestCase {
    private final ComponentFactory implementationHidingComponentAdapterFactory = new HotSwappingBehaviorFactory().forThis(new AnyInjectionFactory());

    // START SNIPPET: man
    public static interface Man {
        Woman getWoman();

        void kiss();

        boolean wasKissed();
    }

    // END SNIPPET: man

    // START SNIPPET: woman
    public static interface Woman {
        Man getMan();
    }

    // END SNIPPET: woman

    public static class Wife implements Woman {
        public final Man partner;

        public Wife(Man partner) {
            this.partner = partner;
        }

        public Man getMan() {
            return partner;
        }
    }


    public void testHotSwappingNaturaelyCaches() {
        DefaultPicoContainer pico = new DefaultPicoContainer(new HotSwappingBehaviorFactory().forThis(new ConstructorInjectionFactory()));
        pico.addComponent(Map.class, HashMap.class);
        Map firstMap = pico.getComponent(Map.class);
        Map secondMap = pico.getComponent(Map.class);
        assertSame(firstMap, secondMap);

    }


    public void testSwappingViaSwappableInterface() {
        MutablePicoContainer pico = new DefaultPicoContainer();
        HotSwappingBehavior hsca = (HotSwappingBehavior) pico.addAdapter(new HotSwappingBehavior(new ConstructorInjector("l", ArrayList.class))).lastCA();
        List l = (List)pico.getComponent("l");
        l.add("Hello");
        final ArrayList newList = new ArrayList();

        ArrayList oldSubject = (ArrayList) hsca.swapRealInstance(newList);
        assertEquals("Hello", oldSubject.get(0));
        assertTrue(l.isEmpty());
        l.add("World");
        assertEquals("World", l.get(0));
    }


    protected ComponentFactory createComponentFactory() {
        return implementationHidingComponentAdapterFactory;
    }

}