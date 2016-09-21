package nablarch.core.repository.di.example.nested;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.example.autowirebytype.HelloComponent;
import nablarch.core.repository.di.example.autowirebytype.HelloMessageProvider;

public class HelloNestedComponentMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/nested/hello-nested.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // DIコンテナで"helloComponent"と名付けたコンポーネントを取得
        HelloComponent helloComponent = (HelloComponent) SystemRepository
                .getObject("helloComponent");
        // HelloMessageProviderに設定した"hello"がコンソールに表示される
        helloComponent.printHello();

        // helloComponent.helloMessageProvider というコンポーネント名でhelloComponentに
        // ネストしたhelloMessageProviderコンポーネントが取得できる。
        HelloMessageProvider helloMessageProvider = (HelloMessageProvider) SystemRepository
                .getObject("helloComponent.helloMessageProvider");

        // 直接 HelloMessageProvider.getHelloMessageメソッドを呼び出す。
        System.out.println(helloMessageProvider.getHelloMessage());
    }

}
