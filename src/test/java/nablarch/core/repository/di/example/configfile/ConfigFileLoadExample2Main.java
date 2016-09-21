package nablarch.core.repository.di.example.configfile;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class ConfigFileLoadExample2Main {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/configfile/configfile2.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // hello.configに設定した "This is Hello Message!!" が取得できる。
        String helloMessage = SystemRepository.getString("hello.message");
        System.out.println(helloMessage);
    }
}
