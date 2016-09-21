package nablarch.core.repository.di;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

/**
 * {@link StoredValueComponentCreator}のテスト。
 * @author Koichi Asano 
 *
 */
public class StoredValueComponentCreatorTest {

    @Test
    public void testToString() {
        
        StoredValueComponentCreator creator = new StoredValueComponentCreator(BigDecimal.valueOf(10l));
        
        assertEquals("stored value object = 10", creator.toString());
    }
}
