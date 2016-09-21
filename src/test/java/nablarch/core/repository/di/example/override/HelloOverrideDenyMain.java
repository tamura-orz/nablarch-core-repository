package nablarch.core.repository.di.example.override;

import nablarch.core.repository.di.ConfigurationLoadException;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.DuplicateDefinitionPolicy;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class HelloOverrideDenyMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/override/hello-override.xml",
                DuplicateDefinitionPolicy.DENY);
        try {
            DiContainer container = new DiContainer(loader);

        } catch (ConfigurationLoadException e) {
            // 重複した設定を行なった場合、例外が発生する。
            throw new RuntimeException("設定ファイルの読み込みに失敗しました。", e);
        }    
    }

}
