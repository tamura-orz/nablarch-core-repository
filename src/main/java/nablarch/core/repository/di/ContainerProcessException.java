package nablarch.core.repository.di;

import nablarch.core.util.annotation.Published;

/**
 * DIコンテナ内の処理に失敗した際に発生する例外。
 * 
 * @author Koichi Asano 
 *
 */
@Published(tag = "architect")
public class ContainerProcessException extends RuntimeException {

    /**
     * コンストラクタ。
     * @param message メッセージ
     * @param cause 原因例外
     */
    public ContainerProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * コンストラクタ。
     * @param message メッセージ
     */
    public ContainerProcessException(String message) {
        super(message);
    }
}
