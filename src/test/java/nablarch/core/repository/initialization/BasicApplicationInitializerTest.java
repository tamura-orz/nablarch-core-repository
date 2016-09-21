package nablarch.core.repository.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


/**
 * Javadoc
 *
 * @author Hisaaki Sioiri
 */
public class BasicApplicationInitializerTest {

    /**
     * {@link BasicApplicationInitializer#initialize()} のテスト。
     * テストケース
     * <ul>
     * <li>初期化対象クラスを設定されない場合、正常に処理が終了する。</li>
     * <li>初期化対象クラスに空のListを設定した場合、正常に処理が終了する。</li>
     * <li>初期化対象クラスを1クラス設定した場合、正常に処理が終了する。</li>
     * <li>初期化対象クラスを複数設定した場合、設定した順に初期化処理が行われる。</li>
     * <li>初期化対象クラスがInitializableインタフェースを実装していない場合、異常終了する。</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testInitialize() throws Exception {

        //----------------------------------------------------------------------
        // 初期化対象クラスを設定しない場合
        //----------------------------------------------------------------------
        BasicApplicationInitializer initializer = new BasicApplicationInitializer();
        initializer.initialize();

        //----------------------------------------------------------------------
        // 初期化対象クラスに空のListを設定した場合
        //----------------------------------------------------------------------
        initializer = new BasicApplicationInitializer();
        initializer.setInitializeList(new ArrayList<Object>());
        initializer.initialize();

        //----------------------------------------------------------------------
        // 初期化対象クラスが1クラスの場合
        //----------------------------------------------------------------------
        initializer = new BasicApplicationInitializer();

        // 初期化対象クラスを設定
        List<Object> initList1 = new ArrayList<Object>();

        InitializeClass1 ic1 = new InitializeClass1();
        initList1.add(ic1);
        initializer.setInitializeList(initList1);

        assertTrue("初期化処理実行前は、dataは空", ic1.data.isEmpty());

        // 初期化処理を実行
        initializer.initialize();


        // 初期化対象が初期化されていること。
        assertEquals("1", ic1.data.get("1"));

        //----------------------------------------------------------------------
        // 初期化対象クラスが複数クラスの場合
        //----------------------------------------------------------------------
        BasicApplicationInitializer initializer2 = new BasicApplicationInitializer();

        // 初期化対象クラスを設定
        List<Object> initList2 = new ArrayList<Object>();
        ic1 = new InitializeClass1();
        InitializeClass2 ic2 = new InitializeClass2();
        ic2.setIc1(ic1);

        initList2.add(ic1);
        initList2.add(ic2);

        assertTrue("初期化処理実行前は、dataは空", ic1.data.isEmpty());
        assertNull("初期化処理実行前は、dataはnull", ic2.data);
        assertNull("初期化処理実行前は、mapはnull", ic2.map);

        initializer2.setInitializeList(initList2);

        initializer2.initialize();


        // 初期化対象が初期化されていること。
        assertEquals("1", ic1.data.get("1"));
        assertEquals(2, ic2.data.size());
        assertEquals("1", ic2.data.get(0));
        assertEquals("2", ic2.data.get(1));
        assertEquals("ic1で初期化された値を参照できているので、Listの順で初期化されていることが確認できる。", "1", ic2.map.get("1"));

        //----------------------------------------------------------------------
        // Initializableを実装していないクラスの場合
        //----------------------------------------------------------------------
        initializer = new BasicApplicationInitializer();

        List<Object> initList3 = new ArrayList<Object>();
        initList3.add(new NotInitializeClass());

        initializer.setInitializeList(initList3);

        try {
            initializer.initialize();
            fail();
        } catch (RuntimeException e) {
            assertEquals("not initializable class. class name = " + NotInitializeClass.class.getName(), e.getMessage());
        }
    }

    //------------------------------------------------------------------
    // 以下テスト用の初期化対象クラス
    //------------------------------------------------------------------

    private class InitializeClass1 implements Initializable {

        private Map<String, String> data = new HashMap<String, String>();

        /** 初期化処理を行う。 */
        public void initialize() {
            data.put("1", "1");
        }
    }

    private class InitializeClass2 implements Initializable {

        private List<String> data;
        private Map<String, String> map;
        private InitializeClass1 ic1;

        /** 初期化処理を行う。 */
        public void initialize() {
            data = new ArrayList<String>();
            data.add("1");
            data.add("2");
            map = ic1.data;

        }

        public void setIc1(InitializeClass1 ic1) {
            this.ic1 = ic1;
        }
    }

    private class NotInitializeClass {
        public void initialize() {
        }
    }
}
