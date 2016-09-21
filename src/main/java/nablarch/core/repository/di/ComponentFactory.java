package nablarch.core.repository.di;

import nablarch.core.util.annotation.Published;

/**
 * コンポーネントのインスタンスを生成するインタフェース。
 * 
 * このインタフェースを登録したクラスをDIコンテナにコンポーネントとして登録した場合、
 * このオブジェクトそのものではなくメソッドcreateComponentで返されるオブジェクトが
 * コンポーネントとして使用される。
 * 
 * @param <T> ファクトリが作成するオブジェクトの型。
 * 
 * @author Koichi Asano 
 */
@Published(tag = "architect")
public interface ComponentFactory<T> {

    /**
     * オブジェクトを作成する。
     * @return 作成したオブジェクト
     */
    T createObject();

}
