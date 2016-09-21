package nablarch.core.repository.di.test;

import java.util.HashMap;
import java.util.Map;

import nablarch.core.repository.ObjectLoader;


public class CustomObjectLoader2 implements ObjectLoader {

    private Object obj;

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Map<String, Object> load() {
        
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("obj", obj);
        return values;
    }

    public Object getObj() {
        return obj;
    }
}
