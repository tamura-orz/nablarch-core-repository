package nablarch.core.repository.di.config;

import nablarch.core.repository.di.ComponentCreator;
import nablarch.core.repository.di.ComponentDefinition;
import nablarch.core.repository.di.ContainerProcessException;
import nablarch.core.repository.di.DiContainer;


/**
 * JavaBeans型のコンポーネントを生成するクラス。
 * 
 * @author Koichi Asano 
 *
 */
public class BeanComponentCreator implements ComponentCreator {

    /**
     * デフォルトコンストラクタを使ってコンポーネントを生成する。
     * 
     * @param container コンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     * 
     * @see nablarch.core.repository.di.ComponentCreator#createComponent(DiContainer, ComponentDefinition)
     */
    public Object createComponent(DiContainer container, ComponentDefinition def) {
        try {
            return def.getType().newInstance();
        } catch (InstantiationException e) {
            throw new ContainerProcessException(
                    "component instantiation failed."
                    + " component class name = " + def.getType()
                    , e);
        } catch (IllegalAccessException e) {
            throw new ContainerProcessException(
                    "component instantiation failed."
                    + " component class name = " + def.getType()
                    , e);
        }
    }

}
