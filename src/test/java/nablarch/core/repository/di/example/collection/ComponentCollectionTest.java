package nablarch.core.repository.di.example.collection;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class ComponentCollectionTest {

    @Test
    public void testList() {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/collection/collection.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        ComponentA compA = (ComponentA) SystemRepository.getObject("compA");
        List<?> list = compA.getListProperty();
        ComponentB compB_0 = (ComponentB) list.get(0);
        // "compB_0" が取得できる
        System.out.println(compB_0.getName());

        ComponentB compB_1 = (ComponentB) list.get(1);
        // "compB_1" が取得できる
        System.out.println(compB_1.getName());

        String stringValue = (String) list.get(2);
        // 文字列 "String value" が取得できる
        System.out.println(stringValue);

        assertEquals("compB_0", compB_0.getName());
        assertEquals("compB_1", compB_1.getName());
        assertEquals("String value", stringValue);
    }

    @Test
    public void testMap() {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/collection/collection.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);

        ComponentA compA = (ComponentA) SystemRepository.getObject("compA");
        Map<?, ?> map = compA.getMapProperty();

        ComponentB compB_0 = (ComponentB) map.get("compB_0");
        // "compB_0" が取得できる
        System.out.println(compB_0.getName());

        ComponentB compB_2 = (ComponentB) map.get("compB_2");
        // "compB_2" が取得できる
        System.out.println(compB_2.getName());

        String stringValue = (String) map.get("stringKey");
        // "String value" が取得できる
        System.out.println(stringValue);

        KeyComponent key = new KeyComponent();
        key.setId("00001");
        key.setLang("ja");
        ComponentB compB_3 = (ComponentB) map.get(key);
        // "compB_3" が取得できる
        System.out.println(compB_3.getName());
    }
}
