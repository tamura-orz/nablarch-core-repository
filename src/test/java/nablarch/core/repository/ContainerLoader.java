package nablarch.core.repository;

import nablarch.core.repository.di.ComponentDefinitionLoader;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

import static nablarch.core.util.Builder.concat;

/**
 * {@link DiContainer}をロードするためのクラス。
 *
 * @author T.Kawasaki
 */
public class ContainerLoader {

    /**
     * テストクラスと同じパッケージにあるコンポーネント定義ファイルをロードする。
     * aaa.bbb.SomeTestクラス内で以下のコードを実行したとする。
     * <pre>
     * loader.createContainer(this, "component.xml")
     * </pre>
     * その場合、
     * classpath:aaa/bbb/component.xmlがロードされる。
     *
     * @param test             テストクラスのインスタンス
     * @param componentDefFile コンポーネント定義ファイル名
     * @return コンテナ
     */
    public DiContainer createContainer(Object test, String componentDefFile) {
        return createContainer(test.getClass(), componentDefFile);
    }


    /**
     * テストクラスと同じパッケージにあるコンポーネント定義ファイルをロードする。
     * 以下のコードを実行したとする。
     * <pre>
     * loader.load(aaa.bbb.SomeTest.class, "component.xml")
     * </pre>
     * その場合、
     * classpath:aaa/bbb/component.xmlがロードされる。
     *
     * @param testClass        テストクラス
     * @param componentDefFile コンポーネント定義ファイル名
     * @return コンテナ
     */
    public DiContainer createContainer(Class<?> testClass, String componentDefFile) {
        String url = buildUrl(testClass, componentDefFile);
        return createContainer(url);
    }

    /**
     * コンポーネント定義ファイルから{@link DiContainer}インスタンスを生成する。
     *
     * @param url コンポーネント定義ファイルのURL
     */
    public DiContainer createContainer(String url) {
        ComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(url);
        return new DiContainer(loader);
    }

    /**
     * URLを組み立てる。
     *
     * @param testClass テストクラス
     * @param fileName  ファイル名
     * @return URL
     */
    protected String buildUrl(Class<?> testClass, String fileName) {
        String pkg = testClass.getPackage().getName().replace('.', '/');
        return concat("classpath:", pkg, "/", fileName);
    }

    /** システムリポジトリをクリアする。 */
    protected void clearRepository() {
        SystemRepository.clear();
    }
}
