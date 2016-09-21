package nablarch.core.repository.di.config;

import java.util.ArrayList;
import java.util.List;

import nablarch.core.repository.di.ComponentCreator;
import nablarch.core.repository.di.ComponentDefinition;
import nablarch.core.repository.di.ComponentInjector;
import nablarch.core.repository.di.ContainerProcessException;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.util.StringUtil;


/**
 * Listの定義からコンポーネントを生成するクラス。<br/>
 * 
 * Listは初期化時にならないと対象のオブジェクトを発見できないため、
 * ComponentCreatorとComponentInjectorの2つのインタフェースを実装する。
 * 
 * @author Koichi Asano 
 *
 */
public class ListComponentCreator implements ComponentCreator, ComponentInjector {

    /**
     * 要素となるコンポーネントのIDリスト。
     */
    private List<ListElementDefinition> elementDefinitions;

    
    /**
     * コンストラクタ。
     * @param elementDefs 要素となるコンポーネントのIDリスト
     */
    public ListComponentCreator(List<ListElementDefinition> elementDefs) {
        super();
        this.elementDefinitions = elementDefs;
    }
    /**
     * Listコンポーネントを生成する。
     * 
     * @param container DIコンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     * 
     * @see nablarch.core.repository.di.ComponentCreator#createComponent(DiContainer, ComponentDefinition)
     * 
     */
    public Object createComponent(DiContainer container, ComponentDefinition def) {
        return new ArrayList<Object>();
    }
    /**
     * Listの内容を初期化する。
     * 
     * @param container コンテナ
     * @param def 初期化するコンポーネントの定義
     * @param component 初期化するコンポーネント
     * 
     * @see nablarch.core.repository.di.ComponentInjector#completeInject(nablarch.core.repository.di.DiContainer, 
     *       nablarch.core.repository.di.ComponentDefinition, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void completeInject(DiContainer container,
            ComponentDefinition def, Object component) {

        List<Object> l = (List<Object>) component;
        for (ListElementDefinition elementDef : elementDefinitions) {
            if (elementDef.getId() != null) {
                l.add(container.getComponentById(elementDef.getId()));
            } else {
                Object refComponent = container.getComponentByName(elementDef.getName());
                if (refComponent == null) {
                    throw new ContainerProcessException("list entry component was not found."
                            + "name = " + elementDef.getName());
                }
                l.add(refComponent);
            }
        }
    }

    @Override
    public String toString() {
        List<String> values = new ArrayList<String>();
        for (ListElementDefinition elementDef : elementDefinitions) {
            if (elementDef.getName() != null) {
                values.add("name:" + elementDef.getName());
            } else {
                values.add("id:" + elementDef.getId().toString());
            }
        }
        return "list objects = [" + StringUtil.join(",", values) + "]";
    }
}
