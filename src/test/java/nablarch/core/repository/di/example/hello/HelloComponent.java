package nablarch.core.repository.di.example.hello;

public class HelloComponent {

    private HelloMessageProvider helloMessageProvider;

    public void setHelloMessageProvider(HelloMessageProvider helloProvider) {
        this.helloMessageProvider = helloProvider;
    }

    public void printHello() {
        System.out.println(helloMessageProvider.getHelloMessage());
    }
}
