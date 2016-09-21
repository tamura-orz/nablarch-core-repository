package nablarch.core.repository.di.test;

import nablarch.core.repository.di.ComponentFactory;

public class Component2ComponentFactory implements ComponentFactory<Component2> {

    private String factoryProperty;
    
    public void setFactoryProperty(String factoryProperty) {
        this.factoryProperty = factoryProperty;
    }
    public Component2 createObject() {
        Component2 comp2 = new Component2();
        comp2.setProp1(factoryProperty);
        return comp2;
    }

}
