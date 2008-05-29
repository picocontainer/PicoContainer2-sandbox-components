package org.picocontainer.script;

import org.picocontainer.BehaviorFactory;
import org.picocontainer.ComponentFactory;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoBuilder;
import org.picocontainer.PicoContainer;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.PicoClassNotFoundException;
import org.picocontainer.InjectionFactory;
import org.picocontainer.containers.TransientPicoContainer;


public final class ScriptedBuilder {

    private Class<? extends ScriptedPicoContainer> scriptClass = DefaultScriptedPicoContainer.class;
    private final PicoBuilder picoBuilder;
    private ClassLoader classLoader = DefaultScriptedPicoContainer.class.getClassLoader();
    private boolean cfs;

    public ScriptedBuilder(PicoContainer parentcontainer, InjectionFactory injectionType) {
        picoBuilder = new PicoBuilder(parentcontainer, injectionType);
    }

    public ScriptedBuilder(PicoContainer parentcontainer) {
        picoBuilder = new PicoBuilder(parentcontainer);
    }

    public ScriptedBuilder(InjectionFactory injectionType) {
        picoBuilder = new PicoBuilder(injectionType);
    }

    public ScriptedBuilder() {
        picoBuilder = new PicoBuilder();
    }

    public ScriptedPicoContainer build() {
        DefaultPicoContainer temp = new TransientPicoContainer();
        temp.addComponent(ClassLoader.class, classLoader);
        temp.addComponent("sc", scriptClass);
        temp.addComponent(MutablePicoContainer.class, buildPico());
        ScriptedPicoContainer nc = (ScriptedPicoContainer)temp.getComponent("sc");
        return nc;
    }

    public MutablePicoContainer buildPico() {
        return picoBuilder.build();
    }

    public ScriptedBuilder withConsoleMonitor() {
        picoBuilder.withConsoleMonitor();
        return this;
    }

    public ScriptedBuilder withLifecycle() {
        picoBuilder.withLifecycle();
        return this;
    }

    public ScriptedBuilder withReflectionLifecycle() {
        picoBuilder.withReflectionLifecycle();
        return this;
    }

    public ScriptedBuilder withMonitor(Class<? extends ComponentMonitor> clazz) {
        picoBuilder.withMonitor(clazz);
        return this;
    }

    public ScriptedBuilder withHiddenImplementations() {
        picoBuilder.withHiddenImplementations();
        return this;
    }

    public ScriptedBuilder withComponentFactory(ComponentFactory componentFactory) {
        cfs = true;
        picoBuilder.withComponentFactory(componentFactory);
        return this;
    }

    public ScriptedBuilder withComponentAdapterFactories(BehaviorFactory... factories) {
        cfs = true;
        picoBuilder.withBehaviors(factories);
        return this;
    }

    public ScriptedBuilder withSetterInjection() {
        picoBuilder.withSetterInjection();
        return this;
    }

    public ScriptedBuilder withAnnotatedMethodInjection() {
        picoBuilder.withAnnotatedMethodInjection();
        return this;
    }

    public ScriptedBuilder withConstructorInjection() {
        picoBuilder.withConstructorInjection();
        return this;
    }

    public ScriptedBuilder withCaching() {
        picoBuilder.withCaching();
        return this;
    }

    public ScriptedBuilder withThreadSafety() {
        picoBuilder.withSynchronizing();
        return this;
    }

    public ScriptedBuilder implementedBy(Class<? extends ScriptedPicoContainer> scriptedContainerClass) {
        scriptClass = scriptedContainerClass;
        return this;
    }

    public ScriptedBuilder picoImplementedBy(Class<? extends MutablePicoContainer> picoContainerClass) {
        picoBuilder.implementedBy(picoContainerClass);
        return this;
    }

    public ScriptedBuilder withClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public ScriptedBuilder withComponentFactory(String componentFactoryName) {
        if (componentFactoryName != null && !componentFactoryName.equals("")) {
            picoBuilder.withComponentFactory(loadClass(componentFactoryName, ComponentFactory.class));
        }
        return this;
    }

    private <T> Class<? extends T> loadClass(String className, Class<T> asClass) {
        try {
            return classLoader.loadClass(className).asSubclass(asClass);
        } catch (ClassNotFoundException e) {
            throw new PicoClassNotFoundException(className, e);
        }
    }

    public ScriptedBuilder withMonitor(String monitorName) {
        if (monitorName != null && !monitorName.equals("")) {
            picoBuilder.withMonitor(loadClass(monitorName, ComponentMonitor.class));
        }
        return this;
    }
}