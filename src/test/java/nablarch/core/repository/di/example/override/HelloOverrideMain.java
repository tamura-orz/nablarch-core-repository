package nablarch.core.repository.di.example.override;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class HelloOverrideMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader("nablarch/core/repository/di/example/override/hello-override.xml");
        DiContainer container = new DiContainer(loader);

        SystemRepository.load(container);

        // コンポーネントを取得
        OverrideExample overrideExample = (OverrideExample) SystemRepository.getObject("overrideExample");

        // 上書きする前の "base value1" が出力される
        System.out.println(overrideExample.getStr1());
        // 上書きされた "override value2" が出力される
        System.out.println(overrideExample.getStr2());
        // 上書き設定で設定された "override value3" が出力される
        System.out.println(overrideExample.getStr3());
    }

}
