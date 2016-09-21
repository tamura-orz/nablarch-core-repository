package nablarch.core.repository.di.example.factory;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class FactoryMain {

    public static void main(String[] args) {

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/factory/factory-example.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // "sampleComponent"を指定した場合、SampleComponentFactoryではなく、
        // SampleComponentが取得できる。
        SampleComponent comp = (SampleComponent) SystemRepository
                .getObject("sampleComponent");
        
        comp.hoge();
    }

}
