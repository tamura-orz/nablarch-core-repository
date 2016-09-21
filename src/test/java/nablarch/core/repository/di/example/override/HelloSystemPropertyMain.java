package nablarch.core.repository.di.example.override;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.example.hello.HelloComponent;

public class HelloSystemPropertyMain {

    public static void main(String[] args) {
        
        // システムプロパティにキー"hello.message"でメッセージを設定
        System.setProperty("hello.message", "This is system property hello message!!");
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/override/hello-system-property.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // DIコンテナで"helloComponent"と名付けたコンポーネントを取得
        HelloComponent helloComponent = (HelloComponent) SystemRepository.getObject("helloComponent");
        // 環境設定ファイルに設定した"This is property file hello message!!"ではなく、
        // システムプロパティに設定を記述した"This is system property hello message!!"がコンソールに表示される
        helloComponent.printHello();
    }

}
