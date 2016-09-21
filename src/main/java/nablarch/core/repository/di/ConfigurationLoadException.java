package nablarch.core.repository.di;

import nablarch.core.util.annotation.Published;

/**
 * 設定のロードに失敗した際に発生する例外。
 * 
 * @author Koichi Asano 
 *
 */
@Published(tag = "architect")
public class ConfigurationLoadException extends ContainerProcessException {

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param cause 原因例外
     */
    public ConfigurationLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public ConfigurationLoadException(String message) {
        super(message);
    }

}
