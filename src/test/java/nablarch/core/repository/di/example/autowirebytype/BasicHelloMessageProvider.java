package nablarch.core.repository.di.example.autowirebytype;

public class BasicHelloMessageProvider implements HelloMessageProvider {

    public String getHelloMessage() {
        return "Hello autowire!!";
    }
}
