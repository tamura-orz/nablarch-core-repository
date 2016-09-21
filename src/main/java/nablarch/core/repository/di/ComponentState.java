package nablarch.core.repository.di;

/**
 * コンポーネントの状態を表す列挙型。
 * 
 * @author Koichi Asano 
 *
 */
public enum ComponentState {

    /**
     * インスタンス生成前。
     */
    NOT_INSTANTIATE,
    
    /**
     * インスタンス化中。
     */
    INSTANTIATING,
    /**
     * インスタンス化完了。
     */
    INSTANTIATED,

    /**
     * インジェクション実施中。
     */
    INJECTING,
    
    /**
     * インジェクション完了。
     */
    INJECTED,
    
    /**
     * インジェクション失敗。
     */
    INJECTION_FAILED
}
