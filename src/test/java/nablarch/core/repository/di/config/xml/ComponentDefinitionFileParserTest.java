package nablarch.core.repository.di.config.xml;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

import nablarch.core.repository.di.ConfigurationLoadException;
import nablarch.core.util.FileUtil;
import nablarch.test.support.tool.Hereis;


public class ComponentDefinitionFileParserTest {

    @Test
    public void testLoadFailIllegalTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 存在しないタグ -->
            <componen name="comp1" class="nablarch.core.repository.di.test.Component1">
            </componen>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }

    @Test
    public void testLoadFailNestComponentConfiguration() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 正しくない位置にcomponent-configurationタグ -->
            <component-configuration name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component-configuration>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }


    @Test
    public void testLoadFailIllegalComponentTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                
            <!-- 正しくない位置にcomponentタグ -->
                <component>
                </component>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    

    @Test
    public void testLoadFailIllegalKeyComponentTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                
            <!-- 正しくない位置にkey-componentタグ -->
                <key-component>
                </key-component>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    
    @Test
    public void testLoadFailIllegalValueComponentTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                
            <!-- 正しくない位置にvalue-componentタグ -->
                <value-component>
                </value-component>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }


    @Test
    public void testLoadFailIllegalPropertyTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 正しくない位置にpropertyタグ -->
            <property>
            </property>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }

    @Test
    public void testLoadFailIllegalComponentRefTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 正しくない位置にcomponent-refタグ -->
            <component-ref>
            </component-ref>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }

    @Test
    public void testLoadFailIllegalConfigFileTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

      
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 正しくない位置にconfig-fileタグ -->
                <config-file>
                </config-file>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }

    @Test
    public void testLoadFailIllegalImportTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

      
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 正しくない位置にimportタグ -->
                <import>
                </import>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }

    @Test
    public void testLoadFailIllegalListTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

      
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 正しくない位置にlistタグ -->
                <list>
                </list>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    

    @Test
    public void testLoadFailIllegalMapTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

      
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- 正しくない位置にmapタグ -->
                <map>
                </map>
            </component>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    

    @Test
    public void testLoadFailIllegalEntryTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 正しくない位置にentryタグ -->
            <entry>
            </entry>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    

    @Test
    public void testLoadFailIllegalValueTag() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">

            <!-- 正しくない位置にvalueタグ -->
            <value>
            </value>
        </component-configuration>
        */
        ComponentDefinitionFileParser parser = new ComponentDefinitionFileParser();
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            try {
                parser.parse(in);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        } finally {
            FileUtil.closeQuietly(in);
        }
    }
    
    
    
}
