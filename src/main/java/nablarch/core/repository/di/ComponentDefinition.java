package nablarch.core.repository.di;

import java.util.ArrayList;
import java.util.List;

/**
 * DIコンテナがコンポーネントの生成に使用する定義を保持するクラス。
 * 
 * @author Koichi Asano 
 *
 */
public class ComponentDefinition {

    /**
     * コンストラクタ
     * @param id コンポーネントID
     * @param name コンポーネント名
     * @param creator コンポーネントのファクトリ
     * @param type コンポーネントの型
     */
    public ComponentDefinition(int id, String name, ComponentCreator creator,
            Class<?> type) {
        this.id = id;
        this.name = name;
        this.factory = creator;
        this.type = type;
        references = new ArrayList<ComponentReference>();
    }
    /**
     * コンポーネントのID。
     */
    private int id;
    /**
     * コンポーネントの名称(名称なしの場合null)。
     */
    private String name;
    /**
     * コンポーネントを生成するファクトリ
     */
    private ComponentCreator factory;
    /**
     * コンポーネントの参照。
     */
    private List<ComponentReference> references;
    /**
     * コンポーネントのクラス。
     */
    private Class<?> type;
    
    /**
     * IDのみ参照を許すか否か。
     */
    private boolean useIdOnly = false;

    /**
     * コンポーネントで使用するComponentInjector。<br/>
     * 遅延初期化が必要な場合に使用する。
     */
    private ComponentInjector injector;
    
    /**
     * コンポーネントのIDを取得する。
     * @return コンポーネントのID
     */
    public int getId() {
        return id;
    }

    /**
     * コンポーネントの名称を取得する。
     * @return コンポーネントの名称
     */
    public String getName() {
        return name;
    }
    /**
     * コンポーネントのファクトリを取得する。
     * @return コンポーネントのファクトリ
     */
    public ComponentCreator getCreator() {
        return factory;
    }
    /**
     * コンポーネントが要求する参照のリストを取得する。
     * @return コンポーネントが要求する参照のリスト
     */
    public List<ComponentReference> getReferences() {
        return references;
    }
    /**
     * コンポーネントの型を取得する。
     * @return コンポーネントの型
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * コンポーネントの参照を追加する。
     * 
     * @param reference 追加するコンポーネントの参照
     */
    public void addReference(ComponentReference reference) {
        references.add(reference);
    }

    /**
     * コンポーネントで使用するComponentInjectorを取得する。
     * @return コンポーネントで使用するComponentInjector
     */
    public ComponentInjector getInjector() {
        return injector;
    }

    /**
     * コンポーネントで使用するComponentInjectorをセットする。
     * @param injector コンポーネントで使用するComponentInjector
     */
    public void setInjector(ComponentInjector injector) {
        this.injector = injector;
    }

    /**
     * コンポーネントの参照を更新する。
     * 
     * @param references 更新後のコンポーネント参照のリスト
     */
    public void updateReferences(List<ComponentReference> references) {
        this.references.clear();
        this.references.addAll(references);
    }

    /**
     * IDのみ参照を許すか否かを取得する。
     * 
     * @return IDのみ参照を許す場合 true
     */
    public boolean isUseIdOnly() {
        return useIdOnly;
    }

    /**
     * IDのみ参照を許すか否かを設定する。
     * 
     * @param useIdOnly IDのみ参照を許す場合 true
     */
    public void setUseIdOnly(boolean useIdOnly) {
        this.useIdOnly = useIdOnly;
    }

    
}
