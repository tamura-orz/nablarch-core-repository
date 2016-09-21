package nablarch.core.repository.di.test;

public class Component6 {

    private Interface1 interface1;
    
    public void setInterface1(Interface1 interface1) {
        this.interface1 = interface1;
    }
    
    public String callTest() {
        return interface1.test();
    }
}
