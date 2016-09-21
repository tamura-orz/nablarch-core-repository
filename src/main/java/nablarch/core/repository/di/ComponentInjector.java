package nablarch.core.repository.di;

/**
 * インジェクションに相当する特殊な初期化処理を行うインタフェース。<br/>
 * このインタフェースは、ListやMapといったプロパティを持たないクラス
 * でもインジェクションと同様に他のコンポーネントの参照を解決できる
 * ように初期化する目的で使用する。
 * 
 * @author Koichi Asano 
 *
 */
public interface ComponentInjector {

    /**
     * コンポーネントのインジェクション処理を行う。
     * @param container コンテナ
     * @param def インジェクションするコンポーネントの定義
     * @param component インジェクションするコンポーネント
     */
    void completeInject(DiContainer container, ComponentDefinition def, Object component);
}
