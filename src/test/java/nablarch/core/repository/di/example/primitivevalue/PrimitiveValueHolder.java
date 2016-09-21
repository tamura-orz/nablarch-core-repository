package nablarch.core.repository.di.example.primitivevalue;

public class PrimitiveValueHolder {
    private String stringValue;
    private boolean boolValue;
    private Boolean boolWrapperValue;
    private int intValue;
    private Integer intWrapperValue;
    private long longValue;
    private Long longWrapperValue;
    private String[] stringArrayValue;
    private int[] intArrayValue;
    private Integer[] integerArrayValue;

    /**
     * boolWrapperValueを取得する。
     * @return boolWrapperValue
     */
    public Boolean getBoolWrapperValue() {
        return boolWrapperValue;
    }
    /**
     * boolWrapperValueをセットする。
     * @param boolWrapperValue セットするboolWrapperValue。
     */
    public void setBoolWrapperValue(Boolean boolWrapperValue) {
        this.boolWrapperValue = boolWrapperValue;
    }
    /**
     * intWrapperValueを取得する。
     * @return intWrapperValue
     */
    public Integer getIntWrapperValue() {
        return intWrapperValue;
    }
    /**
     * intWrapperValueをセットする。
     * @param intWrapperValue セットするintWrapperValue。
     */
    public void setIntWrapperValue(Integer intWrapperValue) {
        this.intWrapperValue = intWrapperValue;
    }
    /**
     * longWrapperValueを取得する。
     * @return longWrapperValue
     */
    public Long getLongWrapperValue() {
        return longWrapperValue;
    }
    /**
     * longWrapperValueをセットする。
     * @param longWrapperValue セットするlongWrapperValue。
     */
    public void setLongWrapperValue(Long longWrapperValue) {
        this.longWrapperValue = longWrapperValue;
    }
    
    // setter、getter省略

    /**
     * stringValueを取得する。
     * @return stringValue
     */
    public String getStringValue() {
        return stringValue;
    }
    /**
     * stringValueをセットする。
     * @param stringValue セットするstringValue。
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
    /**
     * boolValueを取得する。
     * @return boolValue
     */
    public boolean isBoolValue() {
        return boolValue;
    }
    /**
     * boolValueをセットする。
     * @param boolValue セットするboolValue。
     */
    public void setBoolValue(boolean boolValue) {
        this.boolValue = boolValue;
    }
    /**
     * intValueを取得する。
     * @return intValue
     */
    public int getIntValue() {
        return intValue;
    }
    /**
     * intValueをセットする。
     * @param intValue セットするintValue。
     */
    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }
    /**
     * longValueを取得する。
     * @return longValue
     */
    public long getLongValue() {
        return longValue;
    }
    /**
     * longValueをセットする。
     * @param longValue セットするlongValue。
     */
    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
    /**
     * stringArrayValueを取得する。
     * @return stringArrayValue
     */
    public String[] getStringArrayValue() {
        return stringArrayValue;
    }
    /**
     * stringArrayValueをセットする。
     * @param stringArrayValue セットするstringArrayValue。
     */
    public void setStringArrayValue(String[] stringArrayValue) {
        this.stringArrayValue = stringArrayValue;
    }
    /**
     * intArrayValueを取得する。
     * 
     * @return intArrayValue
     */
    public int[] getIntArrayValue() {
        return intArrayValue;
    }
    /**
     * intArrayValueを設定する。
     *
     * @param intArrayValue intArrayValue 
     */
    public void setIntArrayValue(int[] intArrayValue) {
        this.intArrayValue = intArrayValue;
    }
    /**
     * integerArrayValueを取得する。
     * 
     * @return integerArrayValue
     */
    public Integer[] getIntegerArrayValue() {
        return integerArrayValue;
    }
    /**
     * integerArrayValueを設定する。
     *
     * @param integerArrayValue integerArrayValue 
     */
    public void setIntegerArrayValue(Integer[] integerArrayValue) {
        this.integerArrayValue = integerArrayValue;
    }
}
