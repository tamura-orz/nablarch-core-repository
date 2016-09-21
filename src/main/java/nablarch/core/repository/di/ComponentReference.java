package nablarch.core.repository.di;

/**
 * コンポーネント定義の参照に関する情報を保持するクラス。
 * 
 * @author Koichi Asano 
 *
 */
public class ComponentReference {

    
    /**
     * コンストラクタ。
     * @param propertyName プロパティ名
     * @param componentName 要求するコンポーネント名
     * @param requiredType 要求するコンポーネントの型
     * @param injectionType インジェクションのタイプ
     * @param targetId インジェクションするオブジェクトのID
     */
    public ComponentReference(String propertyName, String componentName,
            Class<?> requiredType, InjectionType injectionType, int targetId) {
        super();
        this.propertyName = propertyName;
        this.componentName = componentName;
        this.requiredType = requiredType;
        this.injectionType = injectionType;
        this.targetId = targetId;
    }
    /**
     * プロパティ名。
     */
    private String propertyName;
    /**
     * 要求するコンポーネント名。
     */
    private String componentName;
    /**
     * 要求するコンポーネントの型。
     */
    private Class<?> requiredType;
    /**
     * インジェクションのタイプ。
     */
    private InjectionType injectionType;
    /**
     * インジェクションするオブジェクトのID。
     */
    private int targetId;

    /**
     * プロパティ名を取得する。
     * @return プロパティ名。
     */
    public String getPropertyName() {
        return propertyName;
    }
    /**
     * コンポーネント名を取得する。
     * @return コンポーネント名。
     */
    public String getReferenceName() {
        return componentName;
    }
    /**
     * 要求するコンポーネントの型を取得する。
     * @return 要求するコンポーネントの型
     */
    public Class<?> getRequiredType() {
        return requiredType;
    }
    /**
     * 自動インジェクションのタイプを取得する。
     * @return 自動インジェクションのタイプ。
     */
    public InjectionType getInjectionType() {
        return injectionType;
    }
    /**
     * インジェクションするオブジェクトのIDを取得する。
     * @return インジェクションするオブジェクトのID
     */
    public int getTargetId() {
        return targetId;
    }

}
