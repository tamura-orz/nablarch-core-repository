package nablarch.core.repository.di.config;

/**
 * Listが保持するエントリーの定義を保持するクラス。
 * 
 * @author Koichi Asano
 *
 */
public class ListElementDefinition {

    /**
     * コンポーネントID。
     */
    private Integer id;
    /**
     * コンポーネント名。
     */
    private String name;
    /**
     * コンストラクタ。
     * 
     * @param id コンポーネントID
     * @param name コンポーネント名
     */
    public ListElementDefinition(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * コンポーネントIDを取得する。
     * 
     * @return コンポーネントID
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * コンポーネント名を取得する。
     * 
     * @return コンポーネント名
     */
    public String getName() {
        return name;
    }
}
