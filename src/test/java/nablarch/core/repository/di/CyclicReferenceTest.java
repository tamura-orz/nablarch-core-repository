package nablarch.core.repository.di;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.core.repository.test.IgnoringLS;
import nablarch.core.util.Builder;

/**
 * コンポーネント設定ファイルに循環参照がある場合のテスト。
 *
 * @author T.Kawasaki
 */
public class CyclicReferenceTest {

    /**
     * コンポーネントの参照が循環した場合、例外が発生すること。
     * 例外には、参照を解決する際のスタックが含まれること。
     */
    @Test
    public void testReferenceCyclical() {
        try {
            load("referenceCyclic.xml");
            fail();
        } catch (ContainerProcessException e) {
            String expected = Builder.lines(
                    "\tid=[1] name=[component2] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent2] ",
                    "\tid=[2] name=[component3] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent3] ",
                    "\tid=[0] name=[component1] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent1] ");
            assertThat(e.getMessage(), IgnoringLS.contains(expected));
        }
    }

    /**
     * クラスでオートワイアリングを行い循環参照が発生した場合、例外が発生すること。
     * 例外には、参照を解決する際のスタックが含まれること。
     * スタックには、ルックアップするときの型（インタフェースの型）と
     * 実際のコンポーネントの型（クラスの型）と両方の情報が含まれること。
     */
    @Test
    public void testCyclicallyAutoWired() {
        try {
            load("typeCyclic.xml");
            fail();
        } catch (ContainerProcessException e) {
            String expected = Builder.lines(
                    "\tid=[1] name=[component2] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent2] " +
                            "lookup type=[nablarch.core.repository.di.test.CyclicReferenceComponent2]",
                    "\tid=[2] name=[component3] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent3] " +
                            "lookup type=[nablarch.core.repository.di.test.CyclicReferenceComponent3]",
                    "\tid=[0] name=[component1] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent1] " +
                            "lookup type=[nablarch.core.repository.di.test.CyclicReferenceComponent1]");
            assertThat(e.getMessage(), IgnoringLS.contains(expected));
        }

    }

    /**
     * ネストしたコンポーネントで循環参照が発生した場合、例外が発生すること。
     * スタックには、ルックアップするときの型（インタフェースの型）と
     * 実際のコンポーネントの型（クラスの型）と両方の情報が含まれること。
     */
    @Test
    public void testNestedCyclic() {
        try {
            load("nestedCyclic.xml");
            fail();
        } catch (ContainerProcessException e) {
            String expected = Builder.lines(
                    "\tid=[1] name=[component1.component2] " +
                            "component type=[nablarch.core.repository.di.test.CyclicReferenceComponent2] ",
                    "\tid=[2] name=[component1.component2.component3] " +
                            "component type=[nablarch.core.repository.di.test.CyclicReferenceComponent3] ",
                    "\tid=[0] name=[component1] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent1] ");
            assertThat(e.getMessage(), IgnoringLS.contains(expected));
        }
    }

    /**
     * インタフェースでオートワイアリングを行い循環参照が発生した場合、例外が発生すること。
     * 例外には、参照を解決する際のスタックが含まれること。
     * スタックには、ルックアップするときの型（インタフェースの型）と
     * 実際のコンポーネントの型（クラスの型）と両方の情報が含まれること。
     */
    @Test
    public void testAutoWireCyclicUsingInterface() {
        try {
            load("autoWiredByInterface.xml");
            fail();
        } catch (ContainerProcessException e) {
            String expected = Builder.lines(
                    "\tid=[1] name=[] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent4$Implementer1] " +
                            "lookup type=[nablarch.core.repository.di.test.Interface1]",
                    "\tid=[0] name=[component4] component type=[nablarch.core.repository.di.test.CyclicReferenceComponent4] " +
                            "lookup type=[nablarch.core.repository.di.test.CyclicReferenceComponent4]"
            );
            assertThat(e.getMessage(), IgnoringLS.contains(expected));
        }
    }

    private static final String PREFIX = "nablarch/core/repository/di/";

    private DiContainer load(String file) {
        XmlComponentDefinitionLoader loader
                = new XmlComponentDefinitionLoader(PREFIX + file);
        return new DiContainer(loader);
    }

}

