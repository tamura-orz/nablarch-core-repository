package nablarch.core.repository.di.example.hello;

public class HelloMessageProvider {
    private String helloMessage;

    public void setHelloMessage(String hello) {
        this.helloMessage = hello;
    }

    public String getHelloMessage() {
        return helloMessage;
    }
}
