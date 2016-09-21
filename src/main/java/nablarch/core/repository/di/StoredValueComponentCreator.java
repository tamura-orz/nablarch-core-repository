package nablarch.core.repository.di;


/**
 * 作成済みのオブジェクトをコンポーネントとして生成するクラス。<br/>
 * 
 * @author Koichi Asano 
 *
 */
public class StoredValueComponentCreator implements ComponentCreator {

    /**
     * コンストラクタ。
     * @param obj 作成済みオブジェクト
     */
    public StoredValueComponentCreator(Object obj) {
        super();
        this.obj = obj;
    }

    /**
     * 作成済みオブジェクト。
     */
    private Object obj;

    /**
     * コンポーネントを生成する。
     * @param container コンテナ
     * @param def 生成するコンポーネントの定義
     * @return 生成したコンポーネント
     */
    public Object createComponent(DiContainer container, ComponentDefinition def) {
        return obj;
    }

    @Override
    public String toString() {
        return "stored value object = " + obj.toString();
    }
}
