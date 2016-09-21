package nablarch.core.repository.di.test;

import java.util.List;
import java.util.Map;

public class Component3 {

    private Map<Object, Object> mapProp;
    private List<Object> listProp;

    /**
     * listPropを取得する。
     * @return listProp
     */
    public List<Object> getListProp() {
        return listProp;
    }

    /**
     * listPropをセットする。
     * @param listProp セットするlistProp。
     */
    public void setListProp(List<Object> listProp) {
        this.listProp = listProp;
    }

    /**
     * mapPropを取得する。
     * @return mapProp
     */
    public Map<Object, Object> getMapProp() {
        return mapProp;
    }

    /**
     * mapPropをセットする。
     * @param mapProp セットするmapProp。
     */
    public void setMapProp(Map<Object, Object> mapProp) {
        this.mapProp = mapProp;
    }
    
}
