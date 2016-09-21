package nablarch.core.repository.jndi;

import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nablarch.core.util.Builder;
import nablarch.core.util.annotation.Published;


/**
 * JNDIルックアップを行う際のヘルパークラス。<br/>
 * <p/>
 * 設定例を以下に示す。
 * <pre>
 * {@literal
 * <component class="nablarch.core.repository.jndi.JndiHelper">
 *   <property name="jndiProperties">
 *     <map>
 *        <entry key="java.naming.factory.initial" value="weblogic.jndi.WLInitialContextFactory"/>
 *        <entry key="java.naming.provider.url"    value="t3://weblogic.server:7001"/>
 *     </map>
 *   </property>
 * </component>
 * }
 * </pre>
 *
 * @author T.Kawasaki
 */
@Published(tag = "architect")
public class JndiHelper {

    /** JNDIプロパティ */
    private Properties jndiProperties;

    /** JNDIリソース名 */
    private String jndiResourceName;

    /**
     * JNDIルックアップを行う。<br/>
     * JNDI名は、{@link #setJndiResourceName(String)}で設定されたリソース名が使用される。
     *
     * @param <T> ルックアップするオブジェクトの型
     * @return ルックアップして得られたオブジェクト
     */
    @SuppressWarnings("unchecked")
    public <T> T lookUp() {
        return (T) lookUp(jndiResourceName);
    }

    /**
     * JNDIルックアップを行う。<br/>
     *
     * @param <T>              ルックアップするオブジェクトの型
     * @param jndiResourceName JNDIリソース名
     * @return ルックアップして得られたオブジェクト
     */
    @SuppressWarnings("unchecked")
    public <T> T lookUp(String jndiResourceName) {
        T result = null;
        try {
            InitialContext context = createContext();
            result = (T) context.lookup(jndiResourceName);
        } catch (NamingException e) {
            handleNamingException(jndiResourceName, e);
        }
        return result;
    }

    /**
     * {@link InitialContext}を生成する。<br/>
     * プロパティjndiPropertiesが設定されている場合はその設定で、
     * そうでない場合は、クラスパス上のjndi.propertiesで{@link InitialContext}が生成される。
     *
     * @return {@link InitialContext}のインスタンス
     * @throws NamingException {@link InitialContext}生成時に発生した例外
     * @see InitialContext#InitialContext()
     * @see InitialContext#InitialContext(java.util.Hashtable)
     */
    protected InitialContext createContext() throws NamingException {
        return (jndiProperties == null)
                ? new InitialContext()   // クラスパス上のjndi.propertiesが使用される。
                : new InitialContext(jndiProperties);
    }

    /**
     * ルックアップ時に発生した{@link NamingException}を処理する。<br/>
     * この実装では必要なメッセージを設定した上で例外を送出する。
     *
     * @param resourceName    ルックアップした時のJNDIリソース名
     * @param namingException 発生した例外
     * @throws IllegalStateException 常に送出される
     */
    protected void handleNamingException(
            String resourceName, NamingException namingException)
        throws IllegalStateException {

        String msg = Builder.concat(
                "looking up a resource in JNDI failed. resource name=[", resourceName, "] ",
                "JNDI Properties=", jndiProperties, "]");
        throw new IllegalStateException(msg, namingException);
    }

    /**
     * JNDIプロパティを設定する。
     *
     * @param jndiProperties JNDIプロパティ
     */
    public void setJndiProperties(Map<String, String> jndiProperties) {
        this.jndiProperties = new Properties();
        this.jndiProperties.putAll(jndiProperties);
    }

    /**
     * JNDIリソース名を設定する。
     * {@link #lookUp()}メソッドでは、ここで設定したJNDI名が使用される。
     *
     * @param jndiResourceName JNDIリソース名
     */
    public void setJndiResourceName(String jndiResourceName) {
        this.jndiResourceName = jndiResourceName;
    }
}
