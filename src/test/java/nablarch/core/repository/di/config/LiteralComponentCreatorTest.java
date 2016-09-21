package nablarch.core.repository.di.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * {@link LiteralComponentCreator} のテスト。
 * @author Koichi Asano 
 *
 */
public class LiteralComponentCreatorTest {

    @Test
    public void testToString() {
        
        LiteralComponentCreator creator = new LiteralComponentCreator(String.class, "test");
        
        assertEquals("literal object = [type=java.lang.String,value=test]", creator.toString());
    }
}
