package nablarch.core.repository.di.example.imp;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class ImportMain {

    public static void main(String[] args) {
        // 最もrootに近い設定ファイルを読み込み
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader("nablarch/core/repository/di/example/imp/import.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // imported1.xml に記述したクラスの読み込み
        HelloImport helloImport1 = (HelloImport) SystemRepository.getObject("helloImport1");
        // "imported1" が取得できる。
        System.out.println(helloImport1.getValue());

        // imported2.xml に記述したクラスの読み込み
        HelloImport helloImport2 = (HelloImport) SystemRepository.getObject("helloImport2");
        // "imported2" が取得できる。
        System.out.println(helloImport2.getValue());
    }
    
}
