package nablarch.core.repository.di.test;

import nablarch.core.repository.di.ComponentFactory;

// 循環参照するコンポーネントファクトリ
public class RecursiveRefComponentFactory implements ComponentFactory<Component2> {

    private RecursiveRefComponentFactory ref;
    
    public void setRef(RecursiveRefComponentFactory ref) {
        this.ref = ref;
    }
    public Component2 createObject() {
        Component2 comp2 = new Component2();
        return comp2;
    }
}
