package nablarch.core.repository.di;

 import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.config.DuplicateDefinitionPolicy;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.test.Component1;
import nablarch.core.repository.di.test.Component2;
import nablarch.core.repository.test.OnMemoryLogWriter;
import nablarch.core.repository.test.RepositoryInitializer;
import nablarch.core.repository.test.SystemPropertyResource;
import nablarch.test.support.tool.Hereis;

/**
 * 設定ファイルに上書き設定があった場合のテスト。
 *
 * @author Koichi Asano 
 */
public class ConfigurationOverrideTest {

    @Rule
    public final SystemPropertyResource systemPropertyResource = new SystemPropertyResource();
  
    /**
     * システムプロパティによる上書きのテスト。
     * 
     * @throws Throwable
     */
    @Test
    public void testLoadSystemProperty() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        File propFile = File.createTempFile("test", ".properties");
        String propFilePath =  propFile.toURI().toString();
        file.deleteOnExit();
        propFile.deleteOnExit();

        List<String> log = OnMemoryLogWriter.getMessages("writer.monitorLog");
        
        Hereis.file(propFile.getAbsolutePath()); /*
        any.key01=value 01!
        any.key02=value 02!
        any.key03=3
        */
        Hereis.file(file.getAbsolutePath(), propFilePath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file file="${propFilePath}" />
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key01}" />
                <property name="intProp" value="${any.key03}" />
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key02}" />
            </component>
        </component-configuration>
        */
        System.setProperty("any.key01", "overrided value 01!");
        System.setProperty("any.key03", "1");
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");

        log = OnMemoryLogWriter.getMessages("writer.appLog");
        
        boolean found = false;
        
        for (String line : log) {
            if (line.contains("value was overridden by system property. "
                    + " key = any.key01"
                    + ", previous value = [value 01!]"
                    + ", new value = [overrided value 01!]"
                    )) {
                found = true;
                break;
            }
        }

        file.delete();
        propFile.delete();
        
        assertTrue("上書きログが出力されているはず", found);
        assertEquals("overrided value 01!", comp1.getProp1());
        assertEquals(1, comp1.getIntProp());
        assertEquals("value 02!", comp2.getProp1());
        
        
    }


    /**
     * コンポーネント設定の上書き(既に設定のあるプロパティの場合)テスト。
     * 
     * @throws Throwable
     */
    @Test
    public void testLoadDuplicateDefinitionPolicyOverride() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="string value1" />
            </component>
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="string value2" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString(), DuplicateDefinitionPolicy.OVERRIDE);
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        assertEquals("string value2", comp1.getProp1());
    }


    /**
     * コンポーネント設定の上書き(元のコンポーネントに設定していないプロパティの場合)テスト。
     * 
     * @throws Throwable
     */
    @Test
    public void testLoadDuplicateDefinitionPolicyOverride2() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 上書きされない設定 -->
                <property name="prop1" value="base value" />
                <!-- 上書きされる設定 -->
                <property name="boolProp" value="false" />
            </component>
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 上書きされる設定 -->
                <property name="boolProp" value="true" />
                <!-- 追加の設定 -->
                <property name="intProp" value="100" />
            </component>
            <component name="comp6" class="nablarch.core.repository.di.test.Component6">
            </component>
            <component name="comp6" class="nablarch.core.repository.di.test.Component6">
            </component>
            <component name="comp5" class="nablarch.core.repository.di.test.Component5">
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString(), DuplicateDefinitionPolicy.OVERRIDE);
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        assertEquals("base value", comp1.getProp1());
        assertEquals(true, comp1.isBoolProp());
        assertEquals(100, comp1.getIntProp());
    }



    /**
     * 同名で別クラスのコンポーネントが登録されていた場合のテスト。
     * 
     * @throws Throwable
     */
    @Test
    public void testLoadOverrideConflictConfiguration() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
            <!-- 同名で別クラス名のコンポーネント -->
            <component name="comp1" class="nablarch.core.repository.di.test.Component2">
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString(), DuplicateDefinitionPolicy.OVERRIDE);
        DiContainer container = new DiContainer(loader);
        
        Object comp1 = container.getComponentByName("comp1");
        
        assertTrue(comp1 instanceof Component2);

    }


    /**
     * システムプロパティでコンポーネントの上書きを使用とした場合、例外が発生するテスト。
     * 
     */
    @Test
    public void testLoadSystemPropertyFail() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="test" />
            </component>
        </component-configuration>
        */
        System.setProperty("comp1", "overrided value 01!");
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(),
                    JUnitMatchers.containsString("key = [comp1]"));
            assertThat(e.getMessage(),
                    JUnitMatchers.containsString("previous class = [nablarch.core.repository.di.test.Component1]"));
            // OK
        }


        file.delete();
        
    }


    @Test
    public void testOverride() {
        SystemRepository.clear();
        ComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/config_override/override.xml");
        DiContainer diContainer = new DiContainer(loader);
        SystemRepository.load(diContainer);

        RepositoryInitializer.recreateRepository("nablarch/core/repository/di/config_override/override.xml");
//        RepositoryInitializer.recreateRepository("nablarch/core/repository/di/config_override/override.xml");
        assertThat(SystemRepository.getString("key1"), is("overridden"));
        assertThat(SystemRepository.getString("key2"), is("original"));
    }


}

