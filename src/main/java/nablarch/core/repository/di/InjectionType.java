package nablarch.core.repository.di;

/**
 * インジェクションの種類を表す列挙型。
 * 
 * @author Koichi Asano 
 *
 */
public enum InjectionType {

    /**
     * 型ベースの自動インジェクション。
     */
    BY_TYPE,

    /**
     * 名前ベースの自動インジェクション。
     */
    BY_NAME,

    /**
     * コンポーネント名指定のインジェクション。
     */
    REF,

    /**
     * IDベースのインジェクション
     */
    ID
}
