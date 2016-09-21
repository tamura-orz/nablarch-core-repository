package nablarch.core.repository.di.config;


import nablarch.core.repository.di.ComponentCreator;
import nablarch.core.repository.di.ComponentDefinition;
import nablarch.core.repository.di.DiContainer;



/**
 * リテラル表現からオブジェクトを作成するクラス。
 * 
 * @author Koichi Asano 
 *
 */
public class LiteralComponentCreator implements ComponentCreator {

    /**
     * オブジェクトの型。
     */
    private Class<?> type;
    /**
     * 文字列オブジェクトの文字列表現。
     */
    private String literal;
    /**
     * コンストラクタ
     * 
     * @param type クラスの型
     * @param literal 値の文字列表現。
     */
    public LiteralComponentCreator(Class<?> type, String literal) {
        super();
        this.type = type;
        this.literal = literal;
    }

    /**
     * リテラルを解決する。
     * 
     * @param container コンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     * 
     * @see nablarch.core.repository.di.ComponentCreator#createComponent(DiContainer, ComponentDefinition)
     */
    public Object createComponent(DiContainer container, ComponentDefinition def) {
        Object converted = LiteralExpressionUtil.convertLiteralExpressionToObject(container, literal, type);
        return converted;
    }

    @Override
    public String toString() {
        return "literal object = [type=" + type.getName() + ",value=" + literal + "]";
    }
}
