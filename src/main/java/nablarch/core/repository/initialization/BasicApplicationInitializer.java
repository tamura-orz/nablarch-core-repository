package nablarch.core.repository.initialization;

import java.util.List;

/**
 * {@link Initializable}を実装したコンポーネントを指定した順序で初期化するクラス。<br>
 *
 * @author Hisaaki Sioiri
 */
public class BasicApplicationInitializer implements ApplicationInitializer {

    /** 初期化対象オブジェクトリスト */
    private List<Object> initializeList;

    /**
     * 初期化処理を行う。<br>
     * <br>
     * 初期化対象一覧内のオブジェクトを順次初期化する。<br>
     * 初期化処理で例外が発生した場合には、以降の処理は行わず呼び出し元に例外を送出する。<br>
     * 初期化対象オブジェクトと設定されているクラスが、Initializableインタフェースを実装していない場合には、
     * 例外を送出し以降の処理は行わない。<br>
     * <b>本メソッドは、同期化を行わない。</b>
     */
    public void initialize() {
        if (initializeList == null) {
            return;
        }
        for (Object initializeObject : initializeList) {
            if (!(initializeObject instanceof Initializable)) {
                throw new RuntimeException(
                        "not initializable class." 
                        + " class name = " + initializeObject.getClass().getName());
            }
            // 初期化処理を実行
            ((Initializable) initializeObject).initialize();
        }
    }

    /**
     * 初期化対象オブジェクトリストを設定する。
     *
     * @param initializeList 初期化対象のオブジェクトが設定されたList
     */
    public void setInitializeList(List<Object> initializeList) {
        this.initializeList = initializeList;
    }
}
