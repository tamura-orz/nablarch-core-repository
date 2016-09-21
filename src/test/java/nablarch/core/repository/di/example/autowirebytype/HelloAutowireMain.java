package nablarch.core.repository.di.example.autowirebytype;

import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class HelloAutowireMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader 
                = new XmlComponentDefinitionLoader("nablarch/core/repository/di/example/autowire/hello-autowire.xml");
        DiContainer container = new DiContainer(loader);

        // DIコンテナで"helloComponent"と名付けたコンポーネントを取得
        // HelloComponentにはHelloMessageProviderインタフェースのセッタが存在する
        HelloComponent helloComponent = (HelloComponent) container.getComponentByName("helloComponent");
        // 自動インジェクションされたBasicHelloMessageProviderが返す "Hello autowire!!"が表示される
        helloComponent.printHello();
    }
    
}
