package nablarch.core.repository.di.config;

/**
 * Mapが保持するエントリーの定義を保持するクラス。
 * 
 * @author Koichi Asano 
 *
 */
public class MapEntryDefinition {

    /**
     * Entryのkeyおよびvalueのデータ定義を表す列挙型。
     *
     */
    public enum DataType {
        /**
         * 文字列データ。
         */
        STRING,
        /**
         * 参照名による定義。
         */
        REF,
        /**
         * 直接的コンポーネントの定義。
         */
        COMPONENT,
    }

    /**
     * キーの種類。
     */
    private DataType keyType;
    /**
     * キーの文字列表現。
     */
    private String key;
    /**
     * キーの参照名。
     */
    private String keyRef;
    /**
     * キーのコンポーネントID。
     */
    private int keyId;
    /**
     * 値の種類。
     */
    private DataType valueType;
    /**
     * 値の文字列表現。
     */
    private String value;
    /**
     * 値の参照名。
     */
    private String valueRef;
    /**
     * 値のコンポーネントID。
     */
    private int valueId;
    /**
     * キーの種類を取得する。
     * @return キーの種類
     */
    public DataType getKeyType() {
        return keyType;
    }
    /**
     * キーの種類をセットする。
     * @param keyType キーの種類
     */
    public void setKeyType(DataType keyType) {
        this.keyType = keyType;
    }
    /**
     * キーの文字列表現を取得する。
     * @return キーの文字列表現
     */
    public String getKey() {
        return key;
    }
    /**
     * キーの文字列表現をセットする。
     * @param key キーの文字列表現
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * キーの参照名を取得する。
     * @return キーの参照名
     */
    public String getKeyRef() {
        return keyRef;
    }
    /**
     * キーの参照名をセットする。
     * @param keyRef キーの参照名
     */
    public void setKeyRef(String keyRef) {
        this.keyRef = keyRef;
    }
    /**
     * キーのコンポーネントIDを取得する。
     * @return キーのコンポーネントID
     */
    public int getKeyId() {
        return keyId;
    }
    /**
     * キーのコンポーネントIDをセットする。
     * @param keyId キーのコンポーネントID
     */
    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }
    /**
     * 値の種類を取得する。
     * @return 値の種類
     */
    public DataType getValueType() {
        return valueType;
    }
    /**
     * 値の種類をセットする。
     * @param valueType 値の種類
     */
    public void setValueType(DataType valueType) {
        this.valueType = valueType;
    }
    /**
     * 値の文字列表現を取得する。
     * @return 値の文字列表現
     */
    public String getValue() {
        return value;
    }
    /**
     * 値の文字列表現をセットする。
     * @param value 値の文字列表現
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * 値の参照名を取得する。
     * @return 値の参照名
     */
    public String getValueRef() {
        return valueRef;
    }
    /**
     * 値の参照名をセットする。
     * @param valueRef 値の参照名
     */
    public void setValueRef(String valueRef) {
        this.valueRef = valueRef;
    }
    /**
     * 値のコンポーネントIDを取得する。
     * @return 値のコンポーネントID
     */
    public int getValueId() {
        return valueId;
    }
    /**
     * 値のコンポーネントIDをセットする。
     * @param valueId 値のコンポーネントID
     */
    public void setValueId(int valueId) {
        this.valueId = valueId;
    }
}
