package nablarch.core.repository.di.example.imp;

import java.io.IOException;
import java.net.URL;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.util.FileUtil;

public class ImportDirMain {

    public static void main(String[] args) throws IOException {
        URL url = FileUtil.getClasspathResourceURL("nablarch/core/repository/di/example/imp/importDirParent.xml");
        
        
        // 最もrootに近い設定ファイルを読み込み
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(url.toExternalForm());
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        // imported1.xml に記述したクラスの読み込み
        HelloImport helloImport1 = (HelloImport) SystemRepository.getObject("comp1");
        // "imported1" が取得できる。
        System.out.println(helloImport1.getValue());

        // imported2.xml に記述したクラスの読み込み
        HelloImport helloImport2 = (HelloImport) SystemRepository.getObject("comp2");
        // "imported2" が取得できる。
        System.out.println(helloImport2.getValue());
    }
    
}
