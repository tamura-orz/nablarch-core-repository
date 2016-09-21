package nablarch.core.repository;

import nablarch.core.repository.di.DiContainer;
import org.junit.rules.ExternalResource;

/**
 * NAFのテストで使用するシステムリポジトリ操作クラス。
 * 以下のようにしてテストクラスのpublicフィールドとして宣言する。
 * <pre>
 * {@literal @Rule public RepositoryLoader repo = new RepositoryLoader();}
 * {@literal @ClassRule public RepositoryLoader repo = new RepositoryLoader();}
 * </pre>
 * <p/>
 * テスト終了後に自動的にシステムリポジトリが初期化されるため、
 * 明示的にリポジトリの初期化処理を記述する必要はない。
 * これは、テストクラス内でのリポジトリ変更が、
 * 他のテストクラスへ影響を与えないことを意図している。
 *
 * @author T.Kawasaki
 */
public class RepositoryLoader extends ExternalResource {

    private final ContainerLoader loader = new ContainerLoader();

    public RepositoryLoader() {
        // @Testメソッド実行前に使用される場合もあるためbeforeメソッドでなく、
        // インスタンス生成時にリポジトリクリアを実行。
        loader.clearRepository();
    }

    /**
     * テストクラスと同じパッケージにあるコンポーネント定義ファイルを
     * システムリポジトリにロードする。
     * aaa.bbb.SomeTestクラス内で以下のコードを実行したとする。
     * <pre>
     * repo.load(this, "component.xml")
     * </pre>
     * その場合、
     * classpath:aaa/bbb/component.xmlがロードされる。
     * コンポーネント定義ファイルを複数指定した場合、指定した順番にロードされる。
     *
     * @param test              テストクラス（パッケージをパスとして使用する）のインスタンス
     * @param componentDefFiles コンポーネント定義ファイル名（複数指定可）
     */
    public void load(Object test, String... componentDefFiles) {
        load(test.getClass(), componentDefFiles);
    }

    /**
     * テストクラスと同じパッケージにあるコンポーネント定義ファイルを
     * システムリポジトリにロードする。
     * 以下のコードを実行したとする。
     * <pre>
     * repo.load(aaa.bbb.SomeTest.class, "component.xml")
     * </pre>
     * その場合、
     * classpath:aaa/bbb/component.xmlがロードされる。
     * コンポーネント定義ファイルを複数指定した場合、指定した順番にロードされる。
     *
     * @param testClass         テストクラス（パッケージをパスとして使用する）のClass
     * @param componentDefFiles コンポーネント定義ファイル名（複数指定可）
     */
    public void load(Class<?> testClass, String... componentDefFiles) {
        for (String e : componentDefFiles) {
            String url = loader.buildUrl(testClass, e);
            load(url);
        }
    }


    /**
     * コンポーネント定義ファイルをシステムリポジトリにロードする。
     *
     * @param url コンポーネント定義ファイルのURL
     */
    public void load(String url) {
        DiContainer container = loader.createContainer(url);
        SystemRepository.load(container);
    }

    /** {@inheritDoc} */
    @Override
    protected void after() {
        loader.clearRepository();
    }
}
