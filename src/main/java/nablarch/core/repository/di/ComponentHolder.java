package nablarch.core.repository.di;

/**
 * DIコンテナ内でコンポーネントとコンポーネントの状態を保持するクラス。<br/>
 * コンポーネントを生成後はこのクラスがコンポーネントを保持する。
 * 
 * @author Koichi Asano 
 *
 */
public class ComponentHolder {
    /**
     * コンストラクタ。
     * @param definition コンポーネントの定義
     */
    public ComponentHolder(ComponentDefinition definition) {
        this.definition = definition;
    }
    
    /**
     * コンポーネントの定義。
     */
    private ComponentDefinition definition;
    
    /**
     * 生成されたコンポーネント。
     */
    private Object component;

    /**
     * 初期化済みコンポーネント。
     */
    private Object initializedComponent;
    /**
     * コンポーネントの状態。
     */
    private ComponentState state = ComponentState.NOT_INSTANTIATE;

    /**
     * 生成されたコンポーネントを取得する。
     * @return 生成されたコンポーネント
     */
    public Object getComponent() {
        return component;
    }
    /**
     * 生成されたコンポーネントをセットする。
     * @param component 生成されたコンポーネント。
     */
    public void setComponent(Object component) {
        this.component = component;
    }
    /**
     * コンポーネントの状態を取得する。
     * @return コンポーネントの状態
     */
    public ComponentState getState() {
        return state;
    }
    /**
     * コンポーネントの状態をセットする。
     * @param state コンポーネントの状態
     */
    public void setState(ComponentState state) {
        this.state = state;
    }
    /**
     * コンポーネントの定義を取得する。
     * @return コンポーネントの定義
     */
    public ComponentDefinition getDefinition() {
        return definition;
    }
    /**
     * 初期化済みコンポーネントを取得する。
     * @return 初期化済みコンポーネント
     */
    public Object getInitializedComponent() {
        return initializedComponent;
    }
    /**
     * 初期化済みコンポーネントをセットする。
     * @param initializedComponent 初期化済みコンポーネント
     */
    public void setInitializedComponent(Object initializedComponent) {
        this.initializedComponent = initializedComponent;
    }
}
