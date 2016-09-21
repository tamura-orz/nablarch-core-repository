package nablarch.core.repository.di.config;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * MapComponentCreator のテスト。
 *
 * @author Koichi Asano 
 *
 */
public class MapComponentCreatorTest {


    @Test
    public void testToString() {
        
        
        List<MapEntryDefinition> elementDefs = new ArrayList<MapEntryDefinition>();
        
        elementDefs.add(new MapEntryDefinition(){{setKey("key"); setValue("value"); setKeyType(DataType.STRING); setValueType(DataType.STRING);}});
        elementDefs.add(new MapEntryDefinition(){{setKeyId(10); setValueId(12); setKeyType(DataType.COMPONENT); setValueType(DataType.COMPONENT);}});
        elementDefs.add(new MapEntryDefinition(){{setKeyRef("keyName"); setValueRef("valueName"); setKeyType(DataType.REF); setValueType(DataType.REF);}});
        
        MapComponentCreator creator = new MapComponentCreator(elementDefs);
        
        assertEquals("map entries = [[key:key,value:value],[key-id:10,value-id:12],[key-name:keyName,value-name:valueName]]", creator.toString());
    }
}
