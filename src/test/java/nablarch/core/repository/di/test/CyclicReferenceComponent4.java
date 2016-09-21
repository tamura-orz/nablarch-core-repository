package nablarch.core.repository.di.test;

/**
 * @author T.Kawasaki
 */
public class CyclicReferenceComponent4 {

    private Interface1 interface1;
    
    public void setInterface1(Interface1 i) {
        interface1 = i;
    }
    
    public static class Implementer1 implements Interface1 {
        public String test() {
            return null;
        }

        public void setComponent(CyclicReferenceComponent4 component) {
        }
    }

}
