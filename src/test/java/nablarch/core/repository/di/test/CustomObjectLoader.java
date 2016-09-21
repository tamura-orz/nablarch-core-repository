package nablarch.core.repository.di.test;

import java.util.Map;

import nablarch.core.repository.ObjectLoader;


public class CustomObjectLoader implements ObjectLoader {

    private Map<String, Object> values;
    
    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> load() {
        
        return (Map<String, Object>) values;
    }

}
