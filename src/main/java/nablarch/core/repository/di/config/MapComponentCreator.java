package nablarch.core.repository.di.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nablarch.core.repository.di.ComponentCreator;
import nablarch.core.repository.di.ComponentDefinition;
import nablarch.core.repository.di.ComponentInjector;
import nablarch.core.repository.di.ContainerProcessException;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.util.StringUtil;


/**
 * Mapの定義からコンポーネントを生成するクラス。
 * 
 * Mapは初期化時にならないと参照するオブジェクトを発見できないため、
 * ComponentCreatorとComponentInjector2つのインタフェースを実装する。
 * 
 * @author Koichi Asano 
 *
 */
public class MapComponentCreator implements ComponentCreator, ComponentInjector {

    /**
     * Map定義のリスト。
     */
    private List<MapEntryDefinition> entries;

    
    /**
     * コンストラクタ。
     * @param entries Map定義のリスト
     */
    public MapComponentCreator(List<MapEntryDefinition> entries) {
        super();
        this.entries = entries;
    }

    /**
     * Mapを生成する。
     * 
     * @param container コンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     * 
     * @see nablarch.core.repository.di.ComponentCreator#createComponent(DiContainer, ComponentDefinition)
     */
    public Object createComponent(DiContainer container, ComponentDefinition def) {
        
        return new HashMap<Object, Object>();
    }

    /**
     * Mapの内容を定義に従い初期化する。
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
        Map<Object, Object> map = (Map<Object, Object>) component;
        
        for (MapEntryDefinition entry : entries) {
            Object key;
            Object value;
            
            switch(entry.getKeyType()) {
            case COMPONENT:
                key = container.getComponentById(entry.getKeyId());
                break;
            case REF:
                key = container.getComponentByName(entry.getKeyRef());
                if (key == null) {
                    throw new ContainerProcessException("map entry key component name was not found."
                            + " name = " + entry.getKeyRef());
                }
                break;
            case STRING:
                key = LiteralExpressionUtil.convertLiteralExpressionToObject(container, entry.getKey(), String.class);
                break;
            default:
                // 設定の読み込み時に値が設定されていない場合にエラーとしているため、ここには到達しません。
                throw new IllegalStateException(
                        "map entry has no key definition.");
            }

            switch(entry.getValueType()) {
            case COMPONENT:
                value = container.getComponentById(entry.getValueId());
                break;
            case REF:
                value = container.getComponentByName(entry.getValueRef());
                if (value == null) {
                    throw new ContainerProcessException("map entry value component name was not found."
                            + " name = " + entry.getValueRef());
                }
                break;
            case STRING:
                value = LiteralExpressionUtil.convertLiteralExpressionToObject(container, entry.getValue(), String.class);
                break;
            default:
                // 設定の読み込み時に値が設定されていない場合にエラーとしているため、ここには到達しません。
                throw new IllegalStateException(
                        "map entry has no value definition.");
            }

            map.put(key, value);
        }
    }

    @Override
    public String toString() {
        List<String> values = new ArrayList<String>();

        for (MapEntryDefinition entry : entries) {
            String key;
            String value;
            
            switch(entry.getKeyType()) {
            case COMPONENT:
                key = "key-id:" + entry.getKeyId();
                break;
            case REF:
                key = "key-name:" + entry.getKeyRef();
                break;
            case STRING:
                key = "key:" + entry.getKey();
                break;
            default:
                // 設定の読み込み時に値が設定されていない場合にエラーとしているため、ここには到達しません。
                throw new IllegalStateException(
                        "map entry has no key definition.");
            }

            switch(entry.getValueType()) {
            case COMPONENT:
                value = "value-id:" + entry.getValueId();
                break;
            case REF:
                value = "value-name:" + entry.getValueRef();
                break;
            case STRING:
                value = "value:" + entry.getValue();
                break;
            default:
                // 設定の読み込み時に値が設定されていない場合にエラーとしているため、ここには到達しません。
                throw new IllegalStateException(
                        "map entry has no value definition.");
            }

            values.add("[" + key + "," + value + "]");
        }
        
        return "map entries = [" + StringUtil.join(",", values) + "]";
    }
}
