package nablarch.core.repository.di;

/**
 * コンポーネントを生成するインタフェース。
 *
 * @author Koichi Asano 
 */
public interface ComponentCreator {

    /**
     * コンポーネントを生成する。
     * @param container コンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     */
    Object createComponent(DiContainer container, ComponentDefinition def);
}
