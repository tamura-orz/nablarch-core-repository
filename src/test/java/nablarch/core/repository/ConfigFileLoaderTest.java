package nablarch.core.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import org.junit.Test;

import nablarch.test.support.tool.Hereis;

public class ConfigFileLoaderTest {

    @Test
    public void testConfigFileLoaderString() throws Throwable {

        File f = File.createTempFile("ConfigFileLoaderTest", ".properties");
        f.deleteOnExit();
        Hereis.file(f.getAbsolutePath());/*
        #コメント
		key1\\=value1\\
		key2=value2
		*/
        ConfigFileLoader loader = new ConfigFileLoader(f.toURI().toString());
        Map<String, Object> valueMap = loader.load();

        f.delete();
        assertEquals("value1\\", valueMap.get("key1\\"));
        assertEquals("value2", valueMap.get("key2"));
    }

    @Test
    public void testConfigFileLoaderStringString() throws Throwable {
        File sjisFile = File.createTempFile("ConfigFileLoaderTest", ".properties");
        sjisFile.deleteOnExit();
        File utf8File = File.createTempFile("ConfigFileLoaderTest", ".properties");
        utf8File.deleteOnExit();

        FileOutputStream sjisOut = null;
        try {
            sjisOut = new FileOutputStream(sjisFile);
            sjisOut.write("key1=値1\nkey2=値2\n".getBytes("MS932"));
        } finally {
            sjisOut.close();
        }

        FileOutputStream utf8Out = null;
        try {
            utf8Out = new FileOutputStream(utf8File);
            utf8Out.write("key1=値1\nkey2=値2\n".getBytes("UTF-8"));
        } finally {
            utf8Out.close();
        }

        ConfigFileLoader sjisLoader = new ConfigFileLoader(sjisFile.toURI().toString(), "MS932");
        Map<String, Object> sjisValues = sjisLoader.load();
        ConfigFileLoader utf8Loader = new ConfigFileLoader(utf8File.toURI().toString(), "UTF-8");
        Map<String, Object> utf8Values = utf8Loader.load();


        sjisFile.delete();
        utf8File.delete();

        assertEquals("値1", sjisValues.get("key1"));
        assertEquals("値2", sjisValues.get("key2"));
        assertEquals("値1", utf8Values.get("key1"));
        assertEquals("値2", utf8Values.get("key2"));
    }


    @Test
    public void testConfigFileLoaderStream() throws Throwable {

        File f = File.createTempFile("ConfigFileLoaderTest", ".properties");
        f.deleteOnExit();
        Hereis.file(f.getAbsolutePath());/*
        key1=value1
        key2=value2
        */
        FileInputStream fos = null;
        ConfigFileLoader loader = null;
        Map<String, Object> valueMap = null;
        try {
            fos = new FileInputStream(f);
            loader = new ConfigFileLoader(fos);
            valueMap = loader.load();
        } finally {
            fos.close();
        }

        f.delete();
        assertEquals("value1", valueMap.get("key1"));
        assertEquals("value2", valueMap.get("key2"));
    }

    @Test
    public void testConfigFileLoaderStreamString() throws Throwable {
        File sjisFile = File.createTempFile("ConfigFileLoaderTest", ".properties");
        sjisFile.deleteOnExit();
        File utf8File = File.createTempFile("ConfigFileLoaderTest", ".properties");
        utf8File.deleteOnExit();

        FileOutputStream sjisOut = null;
        try {
            sjisOut = new FileOutputStream(sjisFile);
            sjisOut.write("key1=値1\nkey2=値2\n".getBytes("MS932"));
        } finally {
            sjisOut.close();
        }

        FileOutputStream utf8Out = null;
        try {
            utf8Out = new FileOutputStream(utf8File);
            utf8Out.write("key1=値1\nkey2=値2\n".getBytes("UTF-8"));
        } finally {
            utf8Out.close();
        }


        FileInputStream sjisIn = null;
        ConfigFileLoader sjisLoader = null;
        Map<String, Object> sjisValues = null;
        try {
            sjisIn = new FileInputStream(sjisFile);
            sjisLoader = new ConfigFileLoader(sjisIn, "MS932");
            sjisValues = sjisLoader.load();
        } finally {
            sjisIn.close();
        }


        FileInputStream utf8In = null;
        ConfigFileLoader utf8Loader = null;
        Map<String, Object> utf8Values = null;
        try {
            utf8In = new FileInputStream(utf8File);
            utf8Loader = new ConfigFileLoader(utf8In, "UTF-8");
            utf8Values = utf8Loader.load();
        } finally {
            utf8In.close();
        }


        sjisFile.delete();
        utf8File.delete();

        assertEquals("値1", sjisValues.get("key1"));
        assertEquals("値2", sjisValues.get("key2"));
        assertEquals("値1", utf8Values.get("key1"));
        assertEquals("値2", utf8Values.get("key2"));
    }

    @Test
    public void testConfigFileLoaderFileGrammar() throws Throwable {

        File f = File.createTempFile("ConfigFileLoaderTest", ".properties");
        f.deleteOnExit();
        Hereis.file(f.getAbsolutePath());/*
        #コメント
        key1=value1 \
        test
        key2=value2-1
        #重複キーは後勝ち
        key2=value2-2
        #\\は\に解釈される
        key3=value3\\test
        # = を含む設定があった場合
        key4=value4=test
        key5=value5\
        
        =nokey
        novalue=
        
        # コメント行に = が入っている場合。
        # comment_key=comment_value
        
        # 行末コメントが解釈されないことのテスト
        key6=test test#ここは解釈されない
        
        # #をValueに書けることの確認
        key7=test \#test
        */
        ConfigFileLoader loader = new ConfigFileLoader(f.toURI().toString());
        Map<String, Object> valueMap = loader.load();

        f.delete();
        assertEquals("value1 test", valueMap.get("key1"));
        assertEquals("value2-2", valueMap.get("key2"));
        assertEquals("value3\\test", valueMap.get("key3"));
        assertEquals("value4=test", valueMap.get("key4"));
        assertEquals("value5", valueMap.get("key5"));
        assertEquals("test test", valueMap.get("key6"));
        assertEquals("test #test", valueMap.get("key7"));
        assertFalse(valueMap.containsKey("novalue"));

        for (String key : valueMap.keySet()) {
            if (key.contains("comment_key")) {
                fail("コメント行の中身が解釈されている");
            }
        }
    }

    @Test
    public void testConfigFileLoaderFail() throws Throwable {

        File f = File.createTempFile("ConfigFileLoaderTest", ".properties");
        f.deleteOnExit();
        Hereis.file(f.getAbsolutePath());/*
        #コメント
        key1=value1 \
        test
        key2=value2-1
        #重複キーは後勝ち
        key2=value2-2
        #\\は\に解釈される
        key3=value3\\test
        # = を含む設定があった場合
        key4=value4=test
        */
        ConfigFileLoader loader = new ConfigFileLoader(f.toURI().toString(), "unkown encoding");
        try {
            loader.load();
            fail("例外が発生するはず");
        } catch (RuntimeException re) {
            // OK
        }
    }

    /**
     * 存在しないファイルパスを指定。
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNotExistsConfigFile() {
        File file = new File("notFound.config");
        ConfigFileLoader loader = new ConfigFileLoader(file.toURI().toString(),
                "utf-8");
        loader.load();
    }
    
    /**
     * encodingがnullの場合（設定されていない場合）に、UTF-8が固定で使用されることのテスト。
     */
    @Test
    public void testDefaultEncodingRead() throws Exception {

        File f = File.createTempFile("ConfigFileLoaderTest", ".properties");
        f.deleteOnExit();
        
        Hereis.fileWithEncoding(f.getAbsolutePath(), "UTF-8");
        /*
        key=あいうえお
         */
        
        // encodingがnull
        ConfigFileLoader loader = new ConfigFileLoader(f.toURI().toString(), null);
        Map<String, Object> valueMap = loader.load();
    
        assertEquals("あいうえお", valueMap.get("key"));
    }
    
    
    
}
