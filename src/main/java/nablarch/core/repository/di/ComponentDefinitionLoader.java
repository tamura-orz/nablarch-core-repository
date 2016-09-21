package nablarch.core.repository.di;

import java.util.List;

/**
 * コンポーネントの定義を読み込むインタフェース。
 * 
 * @author Koichi Asano 
 *
 */
public interface ComponentDefinitionLoader {

    /**
     * コンポーネント定義をロードする。
     * @param container ロードするコンテナ
     * @return コンポーネント定義のリスト
     */
    List<ComponentDefinition> load(DiContainer container);
}
