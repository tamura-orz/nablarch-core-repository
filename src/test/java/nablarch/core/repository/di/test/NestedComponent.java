package nablarch.core.repository.di.test;

public class NestedComponent {

    private NestedComponent child;

    private String stringProp;

    public void setChild(NestedComponent child) {
        this.child = child;
    }
    
    public NestedComponent getChild() {
        return child;
    }

    /**
     * valueを取得する。
     * @return value
     */
    public String getStringProp() {
        return stringProp;
    }

    /**
     * valueをセットする。
     * @param value セットするvalue。
     */
    public void setStringProp(String value) {
        this.stringProp = value;
    }

    
}
