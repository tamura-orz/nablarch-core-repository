package nablarch.core.repository.di.config.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import nablarch.core.repository.di.ConfigurationLoadException;
import nablarch.core.repository.di.config.xml.schema.AutowireType;
import nablarch.core.repository.di.config.xml.schema.Component;
import nablarch.core.repository.di.config.xml.schema.ComponentConfiguration;
import nablarch.core.repository.di.config.xml.schema.ComponentRef;
import nablarch.core.repository.di.config.xml.schema.ConfigFile;
import nablarch.core.repository.di.config.xml.schema.Entry;
import nablarch.core.repository.di.config.xml.schema.Import;
import nablarch.core.repository.di.config.xml.schema.List;
import nablarch.core.repository.di.config.xml.schema.Property;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * コンポーネント設定ファイルをパースするクラス。
 * 
 * @author Koichi Asano 
 */
public class ComponentDefinitionFileParser {

    
    /**
     * コンポーネント設定ファイルをパースする。
     * 
     * @param in コンポーネント設定ファイルのストリーム
     * @return パース結果をマッピングした ComponentConfiguration
     * @throws ParserConfigurationException Saxパーサの作成に失敗した場合
     * @throws SAXException ファイルのパースに失敗した場合
     * @throws IOException ファイルの読み込みに失敗した場合
     */
    public ComponentConfiguration parse(InputStream in) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    
        SAXParser parser = parserFactory.newSAXParser();
        ComponentConfigurationHandler handler = new ComponentConfigurationHandler();
        parser.parse(in, handler);
        return handler.getResult();
    }

    /**
     * 要素を処理するクラスのリスト。
     */
    private static final ElementProcessor<?>[] ELEMENT_PROCESSORS = new ElementProcessor<?>[] {
        new ComponentConfigurationProcessor(),
        new ComponentProcessor(),
        new PropertyProcessor(),
        new ComponentRefProcessor(),
        new ConfigFileProcessor(),
        new ImportProcessor(),
        new ListProcessor(),
        new MapProcessor(),
        new EntryProcessor(),
        new KeyComponentProcessor(),
        new ValueComponentProcessor(),
        new ValueProcessor()
    };

    /**
     * 要素を処理するクラスのマップ。
     */
    private static final Map<String, ElementProcessor<?>> ELEMENT_PROCESSOR_MAP;
    
    static {
        Map<String, ElementProcessor<?>> tmpMap = new HashMap<String, ElementProcessor<?>>();
        
        for (ElementProcessor<?> processor : ELEMENT_PROCESSORS) {
            tmpMap.put(processor.getElementName(), processor);
        }
        
        ELEMENT_PROCESSOR_MAP = Collections.unmodifiableMap(tmpMap);
    }

    /**
     * コンポーネント設定ファイルを処理するハンドラ。
     * 
     */
    private static final class ComponentConfigurationHandler extends DefaultHandler {
        /**
         * ルート要素をマップする ComponentConfiguration 。
         */
        private ComponentConfiguration rootElement;

        /**
         * 処理中の要素をマップしたオブジェクトのスタック。
         */
        private Stack<Object> targetStack;
        
        /**
         * 処理中の要素をマップしたオブジェクト。
         */
        private Object currentElement;

        /**
         * 処理中の ElementProcessor のスタック。
         */
        private Stack<ElementProcessor<?>> processorStack;
        
        /**
         * 処理中の ElementProcessor
         */
        private ElementProcessor<?> currentProcessor;
        /**
         * コンストラクタ。
         */
        private ComponentConfigurationHandler() {
            targetStack = new Stack<Object>();
            processorStack = new Stack<ElementProcessor<?>>();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) throws SAXException {
            currentProcessor = ELEMENT_PROCESSOR_MAP.get(qName);
            processorStack.push(currentProcessor);
            if (currentProcessor == null) {
                throw new ConfigurationLoadException("processor was not found." 
                        + " element name = " + qName);
            }
            
            currentElement = currentProcessor.process(uri, localName, qName, attributes, currentElement);
            targetStack.push(currentElement);
            if (rootElement == null) {
                rootElement = (ComponentConfiguration) currentElement;
            }
            innerText = new StringBuilder();
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            innerText.append(new String(ch, start, length));
            
        }

        /**
         * 内部文字列の一時領域。
         */
        private StringBuilder innerText = null;

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            currentProcessor.processCharacters(innerText.toString(), targetStack);
            
            targetStack.pop();
            if (targetStack.isEmpty()) {
                currentElement = null;
            } else {
                currentElement = targetStack.peek();
            }

            processorStack.pop();
            if (processorStack.isEmpty()) {
                currentProcessor = null;
            } else {
                currentProcessor = processorStack.peek();
            }
        }

        /**
         * ファイルをパースした結果の ComponentConfiguration を取得する。
         * 
         * @return ファイルをパースした結果の ComponentConfiguration
         */
        public ComponentConfiguration getResult() {
            return rootElement;
        }
    }

    /**
     * 1要素を処理するインタフェース。
     * 
     */
    private static interface ElementProcessor<T> {
        /**
         * 処理対象の要素名を取得する。
         * @return 処理対象の要素名
         */
        String getElementName();

        /**
         * 1要素の処理を行う。
         * 
         * @param uri 要素のURI
         * @param localName 要素のローカル名
         * @param qName 要素のQNAME
         * @param attributes 要素の属性
         * @param parent 親要素のオブジェクト
         * @return 要素を処理した結果のオブジェクト
         */
        T process(String uri, String localName, String qName,
                Attributes attributes, Object parent);

        /**
         * 要素の内部を処理する。
         * 
         * @param innerText 要素の内部の文字列
         * @param targetStack 処理対象プロセッサのスタック
         * 
         * @see DefaultHandler#characters(char[], int, int)
         */
        void processCharacters(String innerText, Stack<?> targetStack);
    }

    /**
     * component-configuration 要素を処理するクラス。
     * 
     */
    private static class ComponentConfigurationProcessor implements ElementProcessor<ComponentConfiguration> {
        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "component-configuration";
        }

        /**
         * {@inheritDoc}
         */
        public ComponentConfiguration process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            ComponentConfiguration ret = new ComponentConfiguration();
            if (parent != null) {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            return ret;
        }
        
        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * Component型の要素を処理するクラスのベースクラス。
     * 
     */
    private abstract static class ComponentTypeProcessor {
        /**
         * Component クラスのインスタンスを作成する。
         * @param uri 要素のURI
         * @param localName 要素のローカル名
         * @param qName 要素のQNAME
         * @param attributes 要素の属性
         * @param parent 親要素のオブジェクト
         * @return Component クラスのインスタンス
         * 
         * @see ElementProcessor#process(String, String, String, Attributes, Object)
         */
        protected Component createComponent(String uri, String localName, String qName,
                Attributes attributes, Object parent) {

            Component ret = new Component();
            
            String autowireType = attributes.getValue("autowireType");
            if (autowireType != null) {
                ret.setAutowireType(AutowireType.fromValue(autowireType));
            }
            ret.setClazz(attributes.getValue("class"));
            ret.setName(attributes.getValue("name"));
            return ret;
        }
    }

    /**
     * component要素を処理するクラス。
     * 
     */
    private static class ComponentProcessor extends ComponentTypeProcessor implements ElementProcessor<Component> {
        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "component";
        }

        /**
         * {@inheritDoc}
         */
        public Component process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {

            Component ret = createComponent(uri, localName, qName, attributes, parent);
            
            if (parent instanceof ComponentConfiguration) {
                ((ComponentConfiguration) parent).getImportOrConfigFileOrComponent().add(ret);
            } else if (parent instanceof List) {
                ((List) parent).getComponentOrValueOrComponentRef().add(ret);
            } else if (parent instanceof Property) {
                ((Property) parent).setComponent(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * key-component要素を処理するクラス。
     * 
     */
    private static class KeyComponentProcessor extends ComponentTypeProcessor implements ElementProcessor<Component> {
        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "key-component";
        }
        /**
         * {@inheritDoc}
         */
        public Component process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            Component ret = createComponent(uri, localName, qName, attributes, parent);
            if (parent instanceof Entry) {
                ((Entry) parent).setKeyComponent(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * value-component要素を処理するクラス。
     * 
     */
    private static class ValueComponentProcessor extends ComponentTypeProcessor implements ElementProcessor<Component> {
        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "value-component";
        }
        /**
         * {@inheritDoc}
         */
        public Component process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            Component ret = createComponent(uri, localName, qName, attributes, parent);
            if (parent instanceof Entry) {
                ((Entry) parent).setValueComponent(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * property要素を処理するクラス。
     * 
     */
    private static class PropertyProcessor implements ElementProcessor<Property> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "property";
        }

        /**
         * {@inheritDoc}
         */
        public Property process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            Property ret = new Property();

            if (!(parent instanceof Component)) {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            Component parentComponent = (Component) parent;
            parentComponent.getProperty().add(ret);

            ret.setName(attributes.getValue("name"));
            ret.setValue(attributes.getValue("value"));
            ret.setRef(attributes.getValue("ref"));
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * component-ref要素を処理するクラス。
     * 
     */
    private static class ComponentRefProcessor implements ElementProcessor<ComponentRef> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "component-ref";
        }

        /**
         * {@inheritDoc}
         */
        public ComponentRef process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            ComponentRef ret = new ComponentRef();
            ret.setName(attributes.getValue("name"));
            if (!(parent instanceof List)) {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            List parentList = (List) parent;
            parentList.getComponentOrValueOrComponentRef().add(ret);
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * config-file要素を処理するクラス。
     * 
     */
    private static class ConfigFileProcessor implements ElementProcessor<ConfigFile> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "config-file";
        }

        /**
         * {@inheritDoc}
         */
        public ConfigFile process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            ConfigFile ret = new ConfigFile();
            if (!(parent instanceof ComponentConfiguration)) {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            
            ret.setFile(attributes.getValue("file"));
            ret.setEncoding(attributes.getValue("encoding"));
            ret.setDir(attributes.getValue("dir"));
            ComponentConfiguration parentConfiguration = (ComponentConfiguration) parent;
            parentConfiguration.getImportOrConfigFileOrComponent().add(ret);
            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * import要素を処理するクラス。
     * 
     */
    private static class ImportProcessor implements ElementProcessor<Import> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "import";
        }

        /**
         * {@inheritDoc}
         */
        public Import process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {

            Import ret = new Import();
            if (!(parent instanceof ComponentConfiguration)) {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            
            ret.setFile(attributes.getValue("file"));
            ret.setDir(attributes.getValue("dir"));
            ComponentConfiguration parentConfiguration = (ComponentConfiguration) parent;
            parentConfiguration.getImportOrConfigFileOrComponent().add(ret);
            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * list要素を処理するクラス。
     * 
     */
    private static class ListProcessor implements ElementProcessor<List> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "list";
        }

        /**
         * {@inheritDoc}
         */
        public List process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            List ret = new List();

            ret.setName(attributes.getValue("name"));
            if (parent instanceof ComponentConfiguration) {
                ((ComponentConfiguration) parent).getImportOrConfigFileOrComponent().add(ret);
            } else if (parent instanceof Property) {
                ((Property) parent).setList(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }

            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * map要素を処理するクラス。
     * 
     */
    private static class MapProcessor implements ElementProcessor<nablarch.core.repository.di.config.xml.schema.Map> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "map";
        }

        /**
         * {@inheritDoc}
         */
        public nablarch.core.repository.di.config.xml.schema.Map process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            nablarch.core.repository.di.config.xml.schema.Map ret = new nablarch.core.repository.di.config.xml.schema.Map();

            ret.setName(attributes.getValue("name"));
            if (parent instanceof ComponentConfiguration) {
                ((ComponentConfiguration) parent).getImportOrConfigFileOrComponent().add(ret);
            } else if (parent instanceof Property) {
                ((Property) parent).setMap(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }

            
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }

    /**
     * entry要素を処理するクラス。
     * 
     */
    private static class EntryProcessor implements ElementProcessor<Entry> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "entry";
        }

        /**
         * {@inheritDoc}
         */
        public Entry process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {
            Entry ret = new Entry();

            if (parent instanceof nablarch.core.repository.di.config.xml.schema.Map) {
                ((nablarch.core.repository.di.config.xml.schema.Map) parent).getEntry().add(ret);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
            ret.setKey(attributes.getValue("key"));
            ret.setKeyName(attributes.getValue("key-name"));
            ret.setValue(attributes.getValue("value"));
            ret.setValueName(attributes.getValue("value-name"));
            return ret;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
        }
    }
    
    /**
     * value要素を処理するクラス。
     * 
     */
    private static class ValueProcessor implements ElementProcessor<String> {

        /**
         * {@inheritDoc}
         */
        public String getElementName() {
            return "value";
        }

        /**
         * {@inheritDoc}
         */
        public String process(String uri, String localName, String qName,
                Attributes attributes, Object parent) {

            return null;
        }

        /**
         * {@inheritDoc}
         */
        public void processCharacters(String innerText, Stack<?> targetStack) {
            Object parent = targetStack.get(targetStack.size() - 2);
            if (parent instanceof List) {
                ((List) parent).getComponentOrValueOrComponentRef().add(innerText);
            } else {
                throw new ConfigurationLoadException("xml format was not valid.");
            }
        }
    }
}
