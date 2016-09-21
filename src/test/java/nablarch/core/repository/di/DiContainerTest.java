package nablarch.core.repository.di;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import nablarch.core.repository.ObjectLoader;
import nablarch.core.repository.di.config.DuplicateDefinitionPolicy;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.di.test.Component1;
import nablarch.core.repository.di.test.Component2;
import nablarch.core.repository.di.test.Component3;
import nablarch.core.repository.di.test.Component4;
import nablarch.core.repository.di.test.Component6;
import nablarch.core.repository.di.test.Component8;
import nablarch.core.repository.di.test.NestedComponent;
import nablarch.core.repository.test.SystemPropertyResource;
import nablarch.test.support.tool.Hereis;


/**
 * DiContainerクラスのテスト。
 * @author asano
 *
 */
public class DiContainerTest {

    @Rule
    public final SystemPropertyResource systemPropertyResource = new SystemPropertyResource();

    /**
     * ObjectLoader が、Mapからの取得順にロードされることを確認する。
     */
    @Test
    public void testObjectLoaderSequential() {
        
        final ObjectLoader childLoader0 = createSimpleLoader("key1", "val0");
        final ObjectLoader childLoader1 = createSimpleLoader("key1", "val1");
        final ObjectLoader childLoader2 = createSimpleLoader("key1", "val2");
        final ObjectLoader childLoader3 = createSimpleLoader("key1", "val3");
        final ObjectLoader childLoader4 = createSimpleLoader("key1", "val4");
        final ObjectLoader childLoader5 = createSimpleLoader("key1", "val5");
        final ObjectLoader childLoader6 = createSimpleLoader("key1", "val6");
        final ObjectLoader childLoader7 = createSimpleLoader("key1", "val7");
        final ObjectLoader childLoader8 = createSimpleLoader("key1", "val8");
        final ObjectLoader childLoader9 = createSimpleLoader("key1", "val9");
        
        ComponentDefinitionLoader cdl = new ComponentDefinitionLoader() {
            
            public List<ComponentDefinition> load(DiContainer container) {
                List<ComponentDefinition> defs = new ArrayList<ComponentDefinition>();
                defs.add(new ComponentDefinition(0, "child0", new StoredValueComponentCreator(childLoader0), childLoader0.getClass()));
                defs.add(new ComponentDefinition(1, "child1", new StoredValueComponentCreator(childLoader1), childLoader1.getClass()));
                defs.add(new ComponentDefinition(2, "child2", new StoredValueComponentCreator(childLoader2), childLoader2.getClass()));
                defs.add(new ComponentDefinition(3, "child3", new StoredValueComponentCreator(childLoader3), childLoader3.getClass()));
                defs.add(new ComponentDefinition(4, "child4", new StoredValueComponentCreator(childLoader4), childLoader4.getClass()));
                defs.add(new ComponentDefinition(5, "child5", new StoredValueComponentCreator(childLoader5), childLoader5.getClass()));
                defs.add(new ComponentDefinition(6, "child6", new StoredValueComponentCreator(childLoader6), childLoader6.getClass()));
                defs.add(new ComponentDefinition(7, "child7", new StoredValueComponentCreator(childLoader7), childLoader7.getClass()));
                defs.add(new ComponentDefinition(8, "child8", new StoredValueComponentCreator(childLoader8), childLoader8.getClass()));
                defs.add(new ComponentDefinition(9, "child9", new StoredValueComponentCreator(childLoader9), childLoader9.getClass()));
                return defs;
            }
        };

        DiContainer container = new DiContainer(cdl);
        
        assertEquals("val9", container.getComponentByName("key1"));
    }

    private ObjectLoader createSimpleLoader(final String key, final Object value) {
        return new ObjectLoader() {
            
            public Map<String, Object> load() {
                Map<String, Object> ret = new LinkedHashMap() {
                    {
                        put(key, value);
                    }
                };
                
                return ret;
            }
        };
    }
    
    @Test
    public void testLoad() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath());
        /*
        <component-configuration
            xmlns="http://tis.co.jp/nablarch/component-configuration"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="string value" />
                <property name="component2" ref="comp2"/>
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop2" />
            </component>
            <component name="initializer" class="nablarch.core.repository.initialization.BasicApplicationInitializer">
                <!-- 2 -->
                <property name="initializeList">
                    <list>
                        <component-ref name="comp2"></component-ref>
                        <component-ref name="comp1"/>
                    </list>
                </property>
            </component>
        </component-configuration>*/
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertEquals("prop2", comp1.getComponent2().getProp1());
        // 初期化の確認
        assertEquals("init", comp1.getInitValue());
        Map<String, String> map = comp1.getComponent2().getInitMap();
        assertEquals("10", map.get("1"));
        assertEquals("20", map.get("2"));
        assertEquals("30", map.get("3"));
    }


    @Test
    public void testLoadLiteralValue() throws Throwable {
        File file = File.createTempFile("test", ".xml");
//        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="string value" />
                <property name="boolProp" value="true" />
                <property name="wrapBoolProp" value="true" />
                <property name="intProp" value="2" />
                <property name="wrapIntProp" value="3" />
                <property name="longProp" value="5" />
                <property name="wrapLongProp" value="6" />
                <property name="arrayProp1" value="abc,def,,ghi," />
                <property name="arrayProp2" value="abc,def,ghi" />
                <property name="intArrayProp" value="1,2,3" />
                <property name="integerArrayProp" value="4,5,6" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertEquals(true, comp1.isBoolProp());
        assertEquals(true, comp1.getWrapBoolProp());
        assertEquals(2, comp1.getIntProp());
        assertEquals(Integer.valueOf(3), comp1.getWrapIntProp());
        assertEquals(5l, comp1.getLongProp());
        assertEquals(Long.valueOf(6l), comp1.getWrapLongProp());

        assertEquals(5, comp1.getArrayProp1().length);
        assertEquals("abc", comp1.getArrayProp1()[0]);
        assertEquals("def", comp1.getArrayProp1()[1]);
        assertEquals("", comp1.getArrayProp1()[2]);
        assertEquals("ghi", comp1.getArrayProp1()[3]);
        assertEquals("", comp1.getArrayProp1()[4]);

        assertEquals(3, comp1.getArrayProp2().length);
        assertEquals("abc", comp1.getArrayProp2()[0]);
        assertEquals("def", comp1.getArrayProp2()[1]);
        assertEquals("ghi", comp1.getArrayProp2()[2]);

        assertArrayEquals(new int[] {1,2,3}, comp1.getIntArrayProp());
        assertArrayEquals(new Integer[] {4,5,6}, comp1.getIntegerArrayProp());
    }

    @Test
    public void testLoadAutowireByType() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="string value" />
                <!-- component2 のインジェクション設定なし(オートワイヤ) -->
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop2" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertEquals("prop2", comp1.getComponent2().getProp1());
    }

    @Test
    public void testLoadAutowireByTypeWithInterface() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp6" class="nablarch.core.repository.di.test.Component6">
                <!-- interface1 のインジェクション設定なし -->
                <!-- Component5が設定される -->
            </component>
            <component name="comp5" class="nablarch.core.repository.di.test.Component5">
                <property name="message" value="this is message" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component6 comp6 = (Component6) container.getComponentByName("comp6");
        // "this is messsage"が出力される
        System.out.println(comp6.callTest());

        file.delete();

        assertEquals("this is message", comp6.callTest());
    }

    @Test
    public void testLoadAutowireByTypeWithAncesterInterface() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp6" class="nablarch.core.repository.di.test.Component6">
                <!-- interface1 のインジェクション設定なし -->
                <!-- Component7が設定される -->
            </component>
            <component name="comp7" class="nablarch.core.repository.di.test.Component7">
                <property name="message" value="this is message" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component6 comp6 = (Component6) container.getComponentByName("comp6");
        // "this is messsage"が出力される
        System.out.println(comp6.callTest());

        file.delete();

        assertEquals("this is message", comp6.callTest());
    }

    @Test
    public void testLoadAutowireByName() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1" autowireType="ByName">
                <property name="prop1" value="string value" />
                <!-- component2 のインジェクション設定なし(名前ベースのオートワイヤ) -->
            </component>
            <component name="component2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop2" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertEquals("prop2", comp1.getComponent2().getProp1());
    }


    @Test
    public void testLoadAutowireNone() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1" autowireType="None">
                <property name="prop1" value="string value" />
                <!-- component2 のインジェクション設定なし(オートワイヤされない) -->
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop2" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertNull(comp1.getComponent2());
    }


    @Test
    public void testLoadNestedComponent() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1" autowireType="ByName">
                <property name="prop1" value="string value" />
                <property name="component2">
                    <component class="nablarch.core.repository.di.test.Component2">
                        <property name="prop1" value="prop2" />
                    </component>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        file.delete();

        assertEquals("string value", comp1.getProp1());
        assertEquals("prop2", comp1.getComponent2().getProp1());
    }


    @Test
    public void testLoadMapDefinition() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();
 
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-1" />
            </component>
            <component name="key-component" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-3" />
            </component>
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="mapProp" >
                    <map>
                        <entry key="key1" value="value1"/>
                        <entry key="key2" value-name="component2"/>
                        <entry key="key3" >
                            <value-component class="nablarch.core.repository.di.test.Component2">
                                <property name="prop1" value="prop1-2" />
                            </value-component>
                        </entry>
                        <entry key-name="key-component" value="value2"/>
                        <entry value="value3">
                            <key-component class="nablarch.core.repository.di.test.Component2">
                                <property name="prop1" value="prop1-4" />
                            </key-component>
                        </entry>
                    </map>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component3 comp3 = (Component3) container.getComponentByName("component3");
        Map<Object, Object> mapProp = comp3.getMapProp();
        Component2 comp2_1 = (Component2) mapProp.get("key2");
        Component2 comp2_2 = (Component2) mapProp.get("key3");
        Object val2 = null;
        Object val3 = null;
        for (Map.Entry<Object, Object> entry : mapProp.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            
            if (key instanceof Component2) {
                Component2 comp2 = (Component2) key;
                if ("prop1-3".equals(comp2.getProp1())) {
                    val2 = value;
                } else if ("prop1-4".equals(comp2.getProp1())) {
                    val3 = value;
                }
            }
        }

        file.delete();

        assertEquals(5, mapProp.size());
        assertEquals("value1", mapProp.get("key1"));
        assertEquals("prop1-1", comp2_1.getProp1());
        assertEquals("prop1-2", comp2_2.getProp1());
        assertEquals("value2", val2);
        assertEquals("value3", val3);
    }

    @Test
    public void testLoadMapWithNameDefinition() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-1" />
            </component>
            <component name="key-component" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-3" />
            </component>
            <map name="testMap">
                <entry key="key1" value="value1"/>
                <entry key="key2" value-name="component2"/>
                <entry key="key3" >
                    <value-component class="nablarch.core.repository.di.test.Component2">
                        <property name="prop1" value="prop1-2" />
                    </value-component>
                </entry>
                <entry key-name="key-component" value="value2"/>
                <entry value="value3">
                    <key-component class="nablarch.core.repository.di.test.Component2">
                        <property name="prop1" value="prop1-4" />
                    </key-component>
                </entry>
            </map>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Map<Object, Object> mapProp = (Map<Object, Object>) container.getComponentByName("testMap");
        Object val2 = null;
        Object val3 = null;
        for (Map.Entry<Object, Object> entry : mapProp.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            
            if (key instanceof Component2) {
                Component2 comp2 = (Component2) key;
                if ("prop1-3".equals(comp2.getProp1())) {
                    val2 = value;
                } else if ("prop1-4".equals(comp2.getProp1())) {
                    val3 = value;
                }
            }
        }

        file.delete();

        assertEquals(5, mapProp.size());
        assertEquals("value1", mapProp.get("key1"));
        assertEquals("value2", val2);
        assertEquals("value3", val3);
    }

    
    @Test
    public void testLoadList() throws Throwable {

        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-1" />
            </component>
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="listProp" >
                    <list>
                        <component class="nablarch.core.repository.di.test.Component2">
                            <property name="prop1" value="prop1-1" />
                        </component>
                        <value>strval1</value>
                        <component class="nablarch.core.repository.di.test.Component2">
                            <property name="prop1" value="prop1-2" />
                        </component>
                        <value>strval2</value>
                    </list>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component3 comp3 = (Component3) container.getComponentByName("component3");
        List<Object> listProp = comp3.getListProp();
        Component2 comp2_1 = (Component2) listProp.get(0);
        String str1 = (String) listProp.get(1);
        Component2 comp2_2 = (Component2) listProp.get(2);
        String str2 = (String) listProp.get(3);

        file.delete();

        assertEquals("prop1-1", comp2_1.getProp1());
        assertEquals("strval1", str1);
        assertEquals("prop1-2", comp2_2.getProp1());
        assertEquals("strval2", str2);
    }


    @Test
    public void testLoadNamedList() throws Throwable {

        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <list name="testList">
                <component class="nablarch.core.repository.di.test.Component2">
                    <property name="prop1" value="prop1-1" />
                </component>
                <value>strval1</value>
                <component class="nablarch.core.repository.di.test.Component2">
                    <property name="prop1" value="prop1-2" />
                </component>
                <component-ref name="outerComponent"/>
                <value>strval2</value>
            </list>
            
            <component name="outerComponent" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="prop1-3" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        List<Object> listProp = (List<Object>) container.getComponentByName("testList");
        Component2 comp2_1 = (Component2) listProp.get(0);
        String str1 = (String) listProp.get(1);
        Component2 comp2_2 = (Component2) listProp.get(2);
        Component2 comp2_3 = (Component2) listProp.get(3);
        String str2 = (String) listProp.get(4);

        file.delete();

        assertEquals("prop1-1", comp2_1.getProp1());
        assertEquals("strval1", str1);
        assertEquals("prop1-2", comp2_2.getProp1());
        assertEquals("prop1-3", comp2_3.getProp1());
        assertEquals("strval2", str2);
    }


    @Test
    public void testLoadAutowireIgnoreString() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1" autowireType="ByName">
                <property name="prop1" value="string value" />
            </component>
            <!-- comp2のプロパティprop1には自動インジェクションされないはず -->
            <component name="comp2" class="nablarch.core.repository.di.test.Component1" autowireType="ByName">
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");

        file.delete();
        assertEquals("string value", comp1.getProp1());
        assertTrue(comp2.getProp1() == null);
    }

    @SuppressWarnings("unchecked")
	@Test
    public void testLoadPropertyFile() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        File propFile = File.createTempFile("test", ".properties");
        String propFilePath =  propFile.toURI().toString();
        file.deleteOnExit();
        propFile.deleteOnExit();

        Hereis.file(propFile.getAbsolutePath());
        /*
        any.key01=value 01!
        any.key02=value 02!
        any.key03=value 03!
        any.key04=value 04!
        any.key05=value 05!
        */
        
        Hereis.file(file.getAbsolutePath(), propFilePath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file file="${propFilePath}" > </config-file>
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key01}" />
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key01}-${any.key02}" />
            </component>
            <map name="testMap">
                <entry key="[${any.key03}]" value="[${any.key04}]"/>
            </map>
            <list name="testList">
                <value>[${any.key05}]</value>
            </list>
        </component-configuration>
        */
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");
        Map<String,String> map = (Map<String,String>)container.getComponentByName("testMap");
        List<String> list = (List<String>)container.getComponentByName("testList");

        file.delete();
        propFile.delete();
        assertEquals("value 01!", comp1.getProp1());
        assertEquals("value 01!-value 02!", comp2.getProp1());
        assertEquals("[value 04!]", map.get("[value 03!]"));
        assertEquals("[value 05!]", list.get(0));
    }

    @Test
    public void testLoadPropertyFileFromClasspath() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath());
        /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file file="nablarch/core/repository/di/classpath-test.properties" />
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key01}" />
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${any.key01}-${any.key02}" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");

        file.delete();
        assertEquals("value 01!", comp1.getProp1());
        assertEquals("value 01!-value 02!", comp2.getProp1());
    }

    
    /**
     * ディレクトリに格納された環境設定ファイルをワイルドカード指定で読み込めることの確認テスト。
     * <p/>
     * 必要なファイルだけ読み込まれ、不要なファイルやディレクトリは読み込まれないことを確認。
     */
    @Test
    public void testLoadPropertyFileWildcard() throws Throwable {
        File file = File.createTempFile("test", ".xml");

        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + "DiContainerTest");
        testDir.mkdir();
        // 前回テストしたファイルを削除
        for (File f : testDir.listFiles()) {
            f.delete();
        }
        
        File nestedFile01 = new File(testDir, "test01.properties");
        File nestedFile02 = new File(testDir, "test02.properties");
        File dummyFile = new File(testDir, "test03.xml");
        File dummyDir = new File(testDir, "test04.properties");

        String nestedFile01Path =  nestedFile01.getAbsolutePath();
        String nestedFile02Path =  nestedFile02.getAbsolutePath();
        String dummyFilePath =  dummyFile.getAbsolutePath();

        file.deleteOnExit();
        nestedFile01.deleteOnExit();
        nestedFile02.deleteOnExit();

        Hereis.file(nestedFile01Path); /*
        key.file1=value01
        */

        Hereis.file(nestedFile02Path); /*
        key.file2=value02
        */

        Hereis.file(dummyFilePath); /*
        key.file3=value03
        */
        
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file dir="./DiContainerTest" file="*.properties" />
        </component-configuration>
        */
        dummyDir.mkdirs();

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        String value1 = (String) container.getComponentByName("key.file1");
        String value2 = (String) container.getComponentByName("key.file2");
        String value3 = (String) container.getComponentByName("key.file3");

        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
        
        assertEquals("value01", value1);
        assertEquals("value02", value2);
        assertTrue(value3 == null);
    }

    /**
     * 存在しないディレクトリおよび、条件に合致するファイルが見つからない場合でも、例外が発生せず、正常に処理が行われることのテスト。
     */
    @Test
    public void testLoadPropertyFileFromDirNotFoundDirOrFile() throws Throwable {
        
        File file = File.createTempFile("test", ".xml");

        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + "DiContainerTest");
        testDir.mkdir();
        // 前回テストしたファイルを削除
        for (File f : testDir.listFiles()) {
            f.delete();
        }
        
        File nestedFile01 = new File(testDir, "test01.properties");
        File nestedFile02 = new File(testDir, "test02.properties");
        File dummyFile = new File(testDir, "test03.xml");
        File dummyDir = new File(testDir, "test04.properties");
        dummyDir.mkdirs();

        String nestedFile01Path =  nestedFile01.getAbsolutePath();
        String nestedFile02Path =  nestedFile02.getAbsolutePath();
        String dummyFilePath =  dummyFile.getAbsolutePath();

        file.deleteOnExit();
        nestedFile01.deleteOnExit();
        nestedFile02.deleteOnExit();

        Hereis.file(nestedFile01Path); /*
        key.file1=value01
        */

        Hereis.file(nestedFile02Path); /*
        key.file2=value02
        */

        Hereis.file(dummyFilePath); /*
        key.file3=value03
        */
        
        /*
         * 存在しないディレクトリを指定
         */
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file dir="./NonExists" file="*.properties" />
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail();
        } catch(IllegalStateException e) {
            assertTrue(e.getMessage().contains("directory not found."));
        }
        
        
        /*
         * 条件に合致するファイルが見つからない場合
         */
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file dir="./DiContainerTest" file="*.nonExist" />
        </component-configuration>
        */
        dummyDir.mkdirs();

        loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        assertNull((String) container.getComponentByName("key.file1"));
        assertNull((String) container.getComponentByName("key.file2"));
        assertNull((String) container.getComponentByName("key.file3"));

        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
    }


    /**
     * config-file要素のdir属性のテスト。
     * <p/>
     * コンポーネント設定ファイルからの相対パスで環境設定ファイルが取得できることを確認する。
     * <p/>
     * 以下の動作が正常に行われることを確認する。
     * <ul>
     * <li>dir属性に指定したコンポーネント設定ファイルの２階層下のディレクトリから環境設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの１階層下のディレクトリから環境設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの１階層上のディレクトリから環境設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの同階層のディレクトリから環境設定ファイルを読み込む</li>
     * </ul>
     */
    @Test
    public void testLoadPropertyFileFromDir() throws Throwable {
        // xmlファイルは、tmpディレクトリ/ConfigTestに作成する。
        File tmpDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "ConfigTest");
        tmpDir.mkdir();
        File file = File.createTempFile("test", ".xml", tmpDir);

        /*
         * ConfigTestディレクトリの２階層下に存在するSubDir2ディレクトリからconfigを読み込む
         */
        loadPropertyFileFromDir(file, "SubDir1/SubDir2");

        /*
         * ConfigTestディレクトリ１階層下に存在するSubDir1ディレクトリからconfigを読み込む
         */
        loadPropertyFileFromDir(file, "SubDir1");

        /*
         * ConfigTestディレクトリ１階層上に存在するParent1ディレクトリからconfigを読み込む
         */
        loadPropertyFileFromDir(file, "../Parent1");

        /*
         * ConfigTestディレクトリからconfigを読み込む
         */
        loadPropertyFileFromDir(file, "");
    }
    
    
    private void loadPropertyFileFromDir(File file, String configDirPath) {
        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + configDirPath);
        testDir.mkdir();
        
        File nestedFile01 = new File(testDir, "test01.properties");
        File nestedFile02 = new File(testDir, "test02.properties");

        Hereis.file(nestedFile01.getAbsolutePath()); /*
        key.file1=value01
        */

        Hereis.file(nestedFile02.getAbsolutePath()); /*
        key.file2=value02
        */

        /*
         * ワイルドカードで複数ファイルを指定するパターン。
         */
        Hereis.file(file.getAbsolutePath(), configDirPath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file dir="${configDirPath}" file="*.properties" />
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        String value1 = (String) container.getComponentByName("key.file1");
        String value2 = (String) container.getComponentByName("key.file2");
        String value3 = (String) container.getComponentByName("key.file3");

        
        assertEquals("value01", value1); // test01.propertiesの内容が読み込まれる
        assertEquals("value02", value2); // test02.propertiesの内容が読み込まれる
        assertTrue(value3 == null); // test03.xmlの内容は読み込まれない
        
        
        /*
         * ファイル（test01.properties）名を直接指定するパターン。
         */
        Hereis.file(file.getAbsolutePath(), configDirPath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <config-file dir="${configDirPath}" file="test01.properties" />
        </component-configuration>
        */

        loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        container = new DiContainer(loader);

        value1 = (String) container.getComponentByName("key.file1");
        value2 = (String) container.getComponentByName("key.file2");
        value3 = (String) container.getComponentByName("key.file3");

        assertEquals("value01", value1); // test01.propertiesの内容が読み込まれる
        assertTrue(value2 == null); // test02.propertiesの内容が読み込まれる
        assertTrue(value3 == null); // test03.xmlの内容は読み込まれない
        
        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
        testDir.delete();
        
        
    }

    /**
     * config-file要素のdir属性のテスト。
     * <p/>
     * コンポーネント設定ファイルがクラスパスに存在する場合（URLがclasspath:）でも正常にパスが解釈され、
     * １階層下のディレクトリ（パッケージ）に存在する環境設定ファイルが取得できることを確認。
     */
    @Test
    public void testLoadPropertyFileFromDirInClassPath() throws Throwable {
        
        // クラスパスに存在するコンポーネント設定ファイル
        URI componentFileUri = new URI("classpath:nablarch/core/repository/di/config/configfile_test_context.xml");
        
        // 上記設定ファイルには以下の定義が行われている
        // <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        //     xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
        //     <config-file dir="./configs" file="*.properties" />
        // </component-configuration>
        
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(componentFileUri.toString());
        DiContainer container = new DiContainer(loader);

        String value1 = (String) container.getComponentByName("key.file1");
        String value2 = (String) container.getComponentByName("key.file2");

        assertEquals("value01", value1); // test01.propertiesの内容が読み込まれる
        assertEquals("value02", value2); // test02.propertiesの内容が読み込まれる
        
    }

    @Test
    public void testLoadNestedFile() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        File nestedFile = File.createTempFile("test", ".xml");
        String nestedFilePath =  nestedFile.toURI().toString();
        file.deleteOnExit();
        nestedFile.deleteOnExit();

        Hereis.file(nestedFile.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1"/>
        </component-configuration>
        */
        Hereis.file(file.getAbsolutePath(), nestedFilePath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import file="${nestedFilePath}" > </import>
            <component name="comp2" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="str01" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component2 comp2 = comp1.getComponent2();

        file.delete();
        nestedFile.delete();
        assertEquals("str01", comp2.getProp1());
    }


    /**
     * ディレクトリに格納されたコンポーネント設定ファイルをワイルドカード指定で読み込めることの確認テスト。
     * <p/>
     * 必要なファイルだけ読み込まれ、不要なファイルやディレクトリは読み込まれないことを確認。
     */
    @Test
    public void testLoadNestedFileWildcard() throws Throwable {
        File file = File.createTempFile("test", ".xml");

        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + "DiContainerTest");
        testDir.mkdir();
        // 前回テストしたファイルを削除
        for (File f : testDir.listFiles()) {
            f.delete();
        }
        
        File nestedFile01 = new File(testDir, "test01.xml");
        File nestedFile02 = new File(testDir, "test02.xml");
        File dummyFile = new File(testDir, "test03.properties");
        File dummyDir = new File(testDir, "test04.xml");

        String nestedFile01Path =  nestedFile01.getAbsolutePath();
        String nestedFile02Path =  nestedFile02.getAbsolutePath();
        String dummyFilePath =  dummyFile.getAbsolutePath();

        file.deleteOnExit();
        nestedFile01.deleteOnExit();
        nestedFile02.deleteOnExit();

        Hereis.file(nestedFile01Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str01" />
            </component>
        </component-configuration>
        */

        Hereis.file(nestedFile02Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str02" />
            </component>
        </component-configuration>
        */

        Hereis.file(dummyFilePath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp3" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str03" />
            </component>
        </component-configuration>
        */
        
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import dir="./DiContainerTest" file="*.xml" />
        </component-configuration>
        */
        
        dummyDir.mkdirs();

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");

        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
        
        assertEquals("str01", comp1.getProp1());
        assertEquals("str02", comp2.getProp1());
        assertTrue(container.getComponentByName("comp3") == null);
    }


    /**
     * 存在しないディレクトリおよび、条件に合致するファイルが見つからない場合でも、例外が発生せず、正常に処理が行われることのテスト。
     */
    @Test
    public void testLoadNestedFileFromDirNotFoundDirOrFile() throws Throwable {
        File file = File.createTempFile("test", ".xml");

        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + "DiContainerTest");
        testDir.mkdir();
        // 前回テストしたファイルを削除
        for (File f : testDir.listFiles()) {
            f.delete();
        }
        
        File nestedFile01 = new File(testDir, "test01.xml");
        File nestedFile02 = new File(testDir, "test02.xml");
        File dummyFile = new File(testDir, "test03.properties");
        File dummyDir = new File(testDir, "test04.xml");
        dummyDir.mkdirs();

        String nestedFile01Path =  nestedFile01.getAbsolutePath();
        String nestedFile02Path =  nestedFile02.getAbsolutePath();
        String dummyFilePath =  dummyFile.getAbsolutePath();

        file.deleteOnExit();
        nestedFile01.deleteOnExit();
        nestedFile02.deleteOnExit();

        Hereis.file(nestedFile01Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str01" />
            </component>
        </component-configuration>
        */

        Hereis.file(nestedFile02Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str02" />
            </component>
        </component-configuration>
        */

        Hereis.file(dummyFilePath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp3" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str03" />
            </component>
        </component-configuration>
        */
        
        
        /*
         * 存在しないディレクトリを指定するパターン。
         */
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import dir="./nonExists" file="*.xml" />
        </component-configuration>
        */
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail();
        } catch(IllegalStateException e) {
            assertTrue(e.getMessage().contains("directory not found."));
        }
        
        /*
         * 条件に合致するファイルが見つからない場合
         */
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import dir="./DiContainerTest" file="*.nonExist" />
        </component-configuration>
        */
        
        loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);


        assertNull((String) container.getComponentByName("comp1"));
        assertNull((String) container.getComponentByName("comp2"));
        

        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
    }
    
    
    /**
     * import要素のdir属性のテスト。
     * <p/>
     * コンポーネント設定ファイルからの相対パスで他のコンポーネント設定ファイルが取得できることを確認する。
     * <p/>
     * 以下の動作が正常に行われることを確認する。
     * <ul>
     * <li>dir属性に指定したコンポーネント設定ファイルの２階層下のディレクトリからコンポーネント設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの１階層下のディレクトリからコンポーネント設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの１階層上のディレクトリからコンポーネント設定ファイルを読み込む</li>
     * <li>dir属性に指定したコンポーネント設定ファイルの同階層のディレクトリからコンポーネント設定ファイルを読み込む</li>
     * </ul>
     */
    @Test
    public void testLoadNestedFileFromDir() throws Throwable {
        // xmlファイルは、tmpディレクトリ/ConfigTestに作成する。
        File tmpDir = new File(System.getProperty("java.io.tmpdir") + File.separator + "ImportTest");
        tmpDir.mkdir();
        File file = File.createTempFile("test", ".xml", tmpDir);

        /*
         * ImportTestディレクトリの２階層下に存在するSubDir2ディレクトリからconfigを読み込む
         */
        loadNestedFileFromDir(file, "SubDir1/SubDir2");

        /*
         * ImportTestディレクトリの１階層下に存在するSubDir1ディレクトリからconfigを読み込む
         */
        loadNestedFileFromDir(file, "SubDir1");

        /*
         * ImportTestディレクトリの１階層上に存在するParent1ディレクトリからconfigを読み込む
         */
        loadNestedFileFromDir(file, "../Parent1");
    }
    
    
    private void loadNestedFileFromDir(File file, String importDirPath) {
        File testDir = new File(file.getParentFile().getAbsoluteFile() + File.separator + importDirPath);
        testDir.mkdir();
        
        File testNestedDir = new File(testDir, File.separator + "nested");
        testNestedDir.mkdir();
        
        File nestedFile01 = new File(testDir, "test01.xml");
        File nestedFile02 = new File(testDir, "test02.xml");
        File nestedFile05 = new File(testNestedDir, "test05.xml"); // test02.xmlのdir属性（相対パス）よりインポートされる
        File nestedFile06 = new File(testNestedDir, "test06.xml"); // test02.xmlからdir属性（相対パス）よりインポートされる

        String nestedFile01Path =  nestedFile01.getAbsolutePath();
        String nestedFile02Path =  nestedFile02.getAbsolutePath();
        String nestedFile05Path =  nestedFile05.getAbsolutePath();
        String nestedFile06Path =  nestedFile06.getAbsolutePath();
        
        Hereis.file(nestedFile01Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str01" />
            </component>
        </component-configuration>
        */

        Hereis.file(nestedFile02Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import dir="./nested" file="*.xml" />
            <component name="comp2" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str02" />
            </component>
        </component-configuration>
        */

        Hereis.file(file.getAbsolutePath(), importDirPath); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import dir="${importDirPath}" file="*.xml" />
        </component-configuration>
        */

        Hereis.file(nestedFile05Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp5" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str05" />
            </component>
        </component-configuration>
        */
        
        Hereis.file(nestedFile06Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp6" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="str06" />
            </component>
        </component-configuration>
        */
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component1 comp2 = (Component1) container.getComponentByName("comp2");
        Component1 comp5 = (Component1) container.getComponentByName("comp5");
        Component1 comp6 = (Component1) container.getComponentByName("comp6");

        
        file.delete();
        nestedFile01.delete();
        nestedFile02.delete();
        testDir.delete();
        nestedFile05.delete();
        nestedFile06.delete();
        
        assertEquals("str01", comp1.getProp1());
        assertEquals("str02", comp2.getProp1());
        assertTrue(container.getComponentByName("comp3") == null);
        assertEquals("str05", comp5.getProp1());
        assertEquals("str06", comp6.getProp1());
        
    }

    /**
     * import要素のdir属性のテスト。
     * <p/>
     * コンポーネント設定ファイルがクラスパスに存在する場合（URLがclasspath:）でも正常にパスが解釈され、
     * １階層下のディレクトリ（パッケージ）に存在するコンポーネント設定ファイルが取得できることを確認。
     */
    @Test
    public void testLoadNestedFileFromDirInClasspath() throws Throwable {

        // クラスパスに存在するコンポーネント設定ファイル
        URI componentFileUri = new URI("classpath:nablarch/core/repository/di/config/importfile_test_context.xml");
        
        // 上記設定ファイルには以下の定義が行われている
        // <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        //     xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
        //     <config-file dir="./imports" file="*.xml" />
        // </component-configuration>
        
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(componentFileUri.toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component2 comp2 = comp1.getComponent2();

        assertEquals("str02", comp2.getProp1());
    }

    
    @Test
    public void testLoadExtendTypeInjection() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component4">
                <property name="prop1" value="str01" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component2 comp2 = comp1.getComponent2();

        file.delete();
        assertEquals("str01", comp2.getProp1());
        assertTrue(comp2 instanceof Component4);
    }


    @Test
    public void testFactoryInjection() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
            <component name="factory" class="nablarch.core.repository.di.test.Component2ComponentFactory">
                <property name="factoryProperty" value="str01" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");
        Component2 comp2 = comp1.getComponent2();

        file.delete();
        assertEquals("str01", comp2.getProp1());
    }


    @Test
    public void testObjectLoad() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component class="nablarch.core.repository.di.test.CustomObjectLoader">
                <property name="values">
                    <map>
                        <entry key="key1" value="value1"/>
                        <entry key="key2" value="value2"/>
                        <entry key="key3" value="value3"/>
                    </map>
                </property>
            </component>
            <component name="comp1" class="nablarch.core.repository.di.test.Component2">
                <property name="prop1" value="${key1}"/>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        Component2 comp2 = (Component2) container.getComponentByName("comp1");

        file.delete();
        assertEquals("value1", comp2.getProp1());
    }

    @Test
    public void testLoadFromClasspath() throws Throwable {

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader("nablarch/core/repository/di/classpath-context.xml");
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        assertEquals("test", comp1.getProp1());
    }


    @Test
    public void testLoadNestedComponentName() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="nest1" class="nablarch.core.repository.di.test.NestedComponent">
                <property name="stringProp" value="nest1"/>
                <property name="child">
                    <component name="child" class="nablarch.core.repository.di.test.NestedComponent">
                        <property name="stringProp" value="nest2"/>
                        <property name="child">
                            <component name="child" class="nablarch.core.repository.di.test.NestedComponent">
                                <property name="stringProp" value="nest3"/>
                            </component>
                        </property>
                    </component>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        NestedComponent nest1 = (NestedComponent) container.getComponentByName("nest1");
        NestedComponent nest2 = (NestedComponent) container.getComponentByName("nest1.child");
        NestedComponent nest3 = (NestedComponent) container.getComponentByName("nest1.child.child");

        file.delete();

        assertEquals("nest1", nest1.getStringProp());
        assertEquals("nest2", nest2.getStringProp());
        assertEquals("nest3", nest3.getStringProp());
    }


    @Test
    public void testLoadRecursive() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.NestedComponent">
                <property name="child" ref="comp2"/>
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.NestedComponent">
                <property name="child" ref="comp1"/>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            DiContainer container = new DiContainer(loader);
            fail("例外が発生するはず。");
        } catch (ContainerProcessException e) {
            // OK
        }

        file.delete();
    }

    @Test
    public void testLoadRecursiveAutowire() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.CyclicReferenceComponent1">
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.CyclicReferenceComponent2">
            </component>
            <component name="comp3" class="nablarch.core.repository.di.test.CyclicReferenceComponent3">
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず。");
        } catch (ContainerProcessException e) {
            // OK
        }

        file.delete();
    }

    @Test
    public void testLoadRecursiveAutowire2() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.CyclicReferenceComponent1">
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.CyclicReferenceComponent2">
            </component>
            <component name="comp3" class="nablarch.core.repository.di.test.CyclicReferenceComponent3">
            	<property name="component" ref="comp1"/>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            DiContainer diContainer = new DiContainer(loader);
            diContainer.getComponentByName("comp1");
            fail("例外が発生するはず。");
        } catch (ContainerProcessException e) {
            // OK
        }

        file.delete();
    }

    @Test
    public void testLoadInject() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="parent" class="nablarch.core.repository.di.test.NestedComponent">
                <property name="stringProp" value="parent"/>
                <property name="child" ref="child"/>
            </component>
            <component name="child" class="nablarch.core.repository.di.test.NestedComponent">
                <property name="stringProp" value="child"/>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);

        NestedComponent parent = (NestedComponent) container.getComponentByName("parent");
        NestedComponent child = (NestedComponent) container.getComponentByName("child");

        file.delete();

        assertEquals("parent", parent.getStringProp());
        assertEquals("child", child.getStringProp());
    }

    @Test
    public void testLoadDuplicateDefinitionPolicyDeny() throws Throwable {
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
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString(), DuplicateDefinitionPolicy.DENY);
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (ConfigurationLoadException e) {
            
        }

    }

    @Test
    public void testLoadFromClasspathUrl() throws Throwable {

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader("classpath:nablarch/core/repository/di/classpath-context.xml");
        DiContainer container = new DiContainer(loader);

        Component1 comp1 = (Component1) container.getComponentByName("comp1");

        assertEquals("test", comp1.getProp1());
    }

    @Test
    public void testLoadInvalidProperty() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp2" class="nablarch.core.repository.di.test.Component4">
                <property name="prop3" value="str01" />
            </component>
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }

    @Test
    public void testLoadInvalidXml() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            <!-- 閉じタグなし -->
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }

    @Test
    public void testLoadFactory() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="factory" class="nablarch.core.repository.di.test.Component2ComponentFactory">
                <property name="factoryProperty" value="str01" />
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);
        Map<String, Object> loadedObjs = container.load();

        // ファクトリから取ったオブジェクトは、インスタンス化されたオブジェクトのはず
        Component2 createdObj = (Component2) loadedObjs.get("factory");

        file.delete();
        assertEquals("str01", createdObj.getProp1());

        // 2回ロードしても、正しく動くことを確認。
        Map<String, Object> loadedObjs2 = container.load();
        assertEquals(loadedObjs, loadedObjs2);
    }

    
    /**
     * コンポーネント名、クラス名がまったく一致する定義があり、かつコンポーネントを直接参照している場合に、
     * エラーにならないことを確認するテスト。
     * (#5146 不具合の再現)
     * 
     */
    @Test
    public void testLoadDuplicateDefinedValue() throws Throwable {
        File file1 = File.createTempFile("test", ".xml");
        file1.deleteOnExit();

        Hereis.file(file1.getAbsolutePath()); /*
                <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                    <component name="comp6" class="nablarch.core.repository.di.test.Component6">
                        <property name="interface1" >
                            <component name="comp5" class="nablarch.core.repository.di.test.Component5">
                                <property name="message" value="message1" />
                            </component>
                        </property>
                    </component>
                    <component name="comp6" class="nablarch.core.repository.di.test.Component6">
                        <property name="interface1" >
                            <component name="comp5" class="nablarch.core.repository.di.test.Component5">
                                <property name="message" value="message2" />
                            </component>
                        </property>
                    </component>
                </component-configuration>
            */
        
        
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file1.toURI().toString());
        DiContainer container = new DiContainer(loader);
        Map<String, Object> loadedObjs = container.load();

        // ファクトリから取ったオブジェクトは、インスタンス化されたオブジェクトのはず
        Component6 createdObj = (Component6) loadedObjs.get("comp6");

        file1.delete();
        assertEquals("message2", createdObj.callTest());
    }

    @Test
    public void testClassNotFound() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <!-- 存在しないクラス -->
            <component name="comp1" class="anyClass"/>
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }

    @Test
    public void testNoPropertyValue() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- プロパティに値を設定しない -->
                <property name="prop1"/>
            </component>
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }


    @Test
    public void testMapEntryHasNoValue() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="mapProp" >
                    <map>
                        <entry key="key1"/>
                    </map>
                </property>
            </component>
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }

    @Test
    public void testMapEntryHasNoKey() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="mapProp" >
                    <map>
                        <entry value="value1"/>
                    </map>
                </property>
            </component>
        </component-configuration>
        */
        try {
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch(ConfigurationLoadException e) {
            // OK
        }
    }

    @Test
    public void testPropertyNotFound() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <property name="prop1" value="${test.prop}" />
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        DiContainer container = new DiContainer(loader);
        Component1 comp1 = (Component1) container.getComponentByName("comp1");


        file.delete();
        
        assertEquals("${test.prop}", comp1.getProp1());
    }

    @Test
    public void testPropertyIsNotString() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                <!-- コンポーネントの名前を${}で指定 -->
                <property name="prop1" value="${comp2}" />
            </component>
            <component name="comp2" class="nablarch.core.repository.di.test.Component2">
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        DiContainer container = new DiContainer(loader);
        Component1 comp1 = (Component1) container.getComponentByName("comp1");


        file.delete();
        
        assertEquals("${comp2}", comp1.getProp1());
    }


    @Test
    public void testLoadRecursiveRef() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <!-- 循環参照するコンポーネントファクトリ -->
            <component name="ref1" class="nablarch.core.repository.di.test.RecursiveRefComponentFactory">
                <property name="ref" ref="ref2" />
            </component>
            <component name="ref2" class="nablarch.core.repository.di.test.RecursiveRefComponentFactory">
                <property name="ref" ref="ref1" />
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        try { 
            DiContainer container = new DiContainer(loader);
            fail("コンポーネントファクトリ間で循環参照がある場合、例外が発生するはず");
        } catch (ContainerProcessException e) {
            // OK
        }


        file.delete();
        
    }

    @Test
    public void testLoadUnregisterdName() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        DiContainer container = new DiContainer(loader);
        // 存在しない名前を指定
        Object obj = container.getComponentByName("xxx");


        file.delete();
        
        assertTrue(obj == null);
        
    }

    @Test
    public void testLoadUnregisterdId() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        try { 
            DiContainer container = new DiContainer(loader);
            // 存在しないIDを指定
            container.getComponentById(-1);
            fail("例外が発生するはず");
        } catch (ContainerProcessException e) {
            // OK
        }


        file.delete();
        
    }


    @Test
    public void testLoadUnregisterdType() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="comp1" class="nablarch.core.repository.di.test.Component1">
            </component>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());

        DiContainer container = new DiContainer(loader);
        // 存在しない型を指定
        Object obj = container.getComponentByType(DiContainer.class);


        file.delete();
        
        assertTrue(obj == null);
    }

    @Test
    public void testLoadUnsupportedLiteralValue() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        {
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                    <property name="component2" value="test" />
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        }

        {
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                    <property name="intArrayProp" value="1,a,3" />
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        }


        {
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                    <property name="integerArrayProp" value="1,a,3" />
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        }


        {
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="comp1" class="nablarch.core.repository.di.test.Component1">
                    <property name="longArrayProp" value="1,2,3" />
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ConfigurationLoadException e) {
                // OK
            }
        }
    }


    @Test
    public void testLoadListUnknownComponentName() throws Throwable {

        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <list name="testList">
                <!-- 存在しないコンポーネント名 -->
                <component-ref name="outComponent"/>
            </list>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (ContainerProcessException e) {
            
        }

    }

    @Test
    public void testLoadMapKeyUnknownComponentName() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="mapProp" >
                    <map>
                        <entry key-name="key-component" value="value2"/>
                    </map>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (ContainerProcessException e) {
            
        }

    }

    @Test
    public void testLoadMapValueUnknownComponentName() throws Throwable {
        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <component name="component3" class="nablarch.core.repository.di.test.Component3">
                <property name="mapProp" >
                    <map>
                        <entry key="key2" value-name="component2"/>
                    </map>
                </property>
            </component>
        </component-configuration>
        */
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (ContainerProcessException e) {
            
        }

    }

    @Test
    public void testLoadInvalidSetterComponent() throws Throwable {

        File file = File.createTempFile("test", ".xml");
        file.deleteOnExit();

        // nablarch.core.repository.di.test.Component8 には正しくない setter が含まれている。
        Hereis.file(file.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="component8" class="nablarch.core.repository.di.test.Component8">
                </component>
        </component-configuration>
        */
        

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
        DiContainer container = new DiContainer(loader);
        
        Object component8 = container.getComponentByName("component8");
        assertTrue(component8 instanceof Component8);
    }

    @Test
    public void testLoadComponentCreationFail() throws Throwable {
        {
            File file = File.createTempFile("test", ".xml");
            file.deleteOnExit();
    
            // private コンストラクタの場合
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="component3" class="nablarch.core.util.FileUtil">
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ContainerProcessException e) {
                
            }
        }


        {
            File file = File.createTempFile("test", ".xml");
            file.deleteOnExit();
    
            // デフォルトコンストラクタがない場合
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                <component name="component3" class="nablarch.core.repository.di.test.DefaultConstructorLessObject">
                </component>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ContainerProcessException e) {
                
            }
        }
    }


    @Test
    public void testLoadMapCreationFail() throws Throwable {
        {

            File file = File.createTempFile("test", ".xml");
            file.deleteOnExit();
    
            // mapのキーがない場合
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                    <map>
                        <entry key="key1"/>
                    </map>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ContainerProcessException e) {
                
            }
        }
        {

            File file = File.createTempFile("test", ".xml");
            file.deleteOnExit();
    
            // mapの値がない場合
            Hereis.file(file.getAbsolutePath()); /*
            <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
                    <map>
                        <entry value="value1"/>
                    </map>
            </component-configuration>
            */
            XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file.toURI().toString());
            try {
                new DiContainer(loader);
                fail("例外が発生するはず");
            } catch (ContainerProcessException e) {
                
            }
        }
    }

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    /**
     * コンポーネント定義ファイル内でのインポートで循環が発生した場合、
     * 例外が発生すること。
     * @throws IOException 予期しない入出力例外
     */
    @Test
    public void testImportCircularFail() throws IOException {

        File file1 = temp.newFile("test1.xml");
        String file1Path =  file1.toURI().toString();

        File file2 = temp.newFile("test2.xml");
        String file2Path =  file2.toURI().toString();

        //
        Hereis.file(file1.getAbsolutePath(), file2Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import file="${file2Path}"/>
        </component-configuration>
        */
        //
        Hereis.file(file2.getAbsolutePath(), file1Path); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import file="${file1Path}"/>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(file1.toURI().toString());
        try {
            new DiContainer(loader);
            fail("例外が発生するはず");
        } catch (ConfigurationLoadException e) {
            assertThat(e.getCause().getMessage(), containsString(file1.getName()));
            assertThat(e.getCause().getMessage(), containsString(file2.getName()));
        }

    }


    /**
     * インポート対象のファイルが存在しない場合、例外が発生し、
     * インポートに失敗したリソース名が例外メッセージに含まれること。
     * @throws IOException 予期しない入出力例外
     */
    @Test
    public void testImportFileNotFound() throws IOException {
        File parent = temp.newFile("parent.xml");
        Hereis.file(parent.getAbsolutePath()); /*
        <component-configuration xmlns="http://tis.co.jp/nablarch/component-configuration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://tis.co.jp/nablarch/component-configuration /home/ssd/workspace/Nablarch/resources/component-configuration.xsd">
            <import file="fileNotFound.xml"/>
        </component-configuration>
        */

        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(parent.toURI().toString());
        try {
            new DiContainer(loader);
            fail();
        } catch (ConfigurationLoadException e) {
            assertThat(e.getCause().getMessage(),
                       is("file to import not found. path=[classpath:fileNotFound.xml]"));
        }
    }
    
    private static final class MockObjectLoader implements ObjectLoader {
        private Map<String, Object> values;
        public MockObjectLoader(Map<String, Object> values) {
            this.values = values;
        }
        public Map<String, Object> load() {
            
            return values;
        }
    }

    
}
