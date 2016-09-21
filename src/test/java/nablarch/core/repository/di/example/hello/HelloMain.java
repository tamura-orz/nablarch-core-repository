package nablarch.core.repository.di.example.hello;

import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class HelloMain {

    public static void main(String[] args) {

        XmlComponentDefinitionLoader loader
            = new XmlComponentDefinitionLoader("nablarch/core/repository/di/example/hello/hello.xml");
        DiContainer container = new DiContainer(loader);

        // DIコンテナで"helloComponent"と名付けたコンポーネントを取得
        HelloComponent helloComponent = (HelloComponent) container.getComponentByName("helloComponent");

        // HelloMessageProviderに設定した"hello"がコンソールに表示される
        helloComponent.printHello();
    }
    
}
