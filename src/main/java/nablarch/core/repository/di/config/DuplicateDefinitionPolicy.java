package nablarch.core.repository.di.config;

/**
 * 設定値に重複する設定が存在した場合の動作ポリシーを表す列挙型。
 * 
 * @author Koichi Asano 
 *
 */
public enum DuplicateDefinitionPolicy {

    /**
     * より後に記述設定値で既存の設定値を上書きする。
     */
    OVERRIDE,
    
    /**
     * 重複した設定値が検出された際に、設定を削除する。
     */
    DENY,
}
