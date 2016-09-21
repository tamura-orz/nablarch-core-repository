package nablarch.core.repository.di.example.configfile;

import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.example.hello.HelloComponent;

public class ConfigFileLoadExampleMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader("nablarch/core/repository/di/example/configfile/configfile.xml");
        DiContainer container = new DiContainer(loader);
        
        HelloComponent helloComponent = (HelloComponent) container.getComponentByName("helloComponent");
        // XMLファイルに書いた"${prop1.value}"ではなく、プロパティファイルに設定した"This is Hello StringResource!!"が出力される。
        helloComponent.printHello();
    }
}
