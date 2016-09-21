package nablarch.core.repository.di.test;

public class Component5 implements Interface1 {

    private String message;
    
    public void setMessage(String message) {
        this.message = message;
    }

    public String test() {
        return message;
    }
}
