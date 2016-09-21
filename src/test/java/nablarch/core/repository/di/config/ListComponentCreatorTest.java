package nablarch.core.repository.di.config;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * {@link ListComponentCreator}のテスト。
 * 
 * @author Koichi Asano 
 *
 */
public class ListComponentCreatorTest {

    @Test
    public void testToString() {
        List<ListElementDefinition> elementDefs = new ArrayList<ListElementDefinition>();
        
        elementDefs.add(new ListElementDefinition(null, "value"));
        elementDefs.add(new ListElementDefinition(10, null));
        elementDefs.add(new ListElementDefinition(11, null));
        
        ListComponentCreator creator = new ListComponentCreator(elementDefs);
        
        assertEquals("list objects = [name:value,id:10,id:11]", creator.toString());
    }
}
