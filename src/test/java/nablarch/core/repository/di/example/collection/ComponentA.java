package nablarch.core.repository.di.example.collection;

import java.util.List;
import java.util.Map;

public class ComponentA {

    private List<?> listProperty;
    
    private Map<?, ?> mapProperty;

    public List<?> getListProperty() {
        return listProperty;
    }

    public void setListProperty(List<?> listProperty) {
        this.listProperty = listProperty;
    }

    public Map<?, ?> getMapProperty() {
        return mapProperty;
    }

    public void setMapProperty(Map<?, ?> mapProperty) {
        this.mapProperty = mapProperty;
    }

    
}
