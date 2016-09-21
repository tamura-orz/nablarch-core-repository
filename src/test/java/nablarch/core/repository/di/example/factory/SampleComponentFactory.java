package nablarch.core.repository.di.example.factory;

import nablarch.core.repository.di.ComponentFactory;

public class SampleComponentFactory implements ComponentFactory<SampleComponent> {
    public SampleComponent createObject() {
        // コンポーネントを生成する。
        // この例では単にクラスをnewして返しているが、フレームワーク外のソフトウェアに
        // 含まれるクラスの場合は、クラスに必要な初期化処理をハードコーディングする。
        return new SampleComponent();
    }
}