package nablarch.core.repository.di.example.primitivevalue;

import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;

public class PrimitiveValueExampleMain {

    public static void main(String[] args) {
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "nablarch/core/repository/di/example/primitivevalue/primitivevalue.xml");
        DiContainer container = new DiContainer(loader);
        SystemRepository.load(container);
        
        // DIコンテナで"primitiveValueHolder"と名付けたコンポーネントを取得
        PrimitiveValueHolder primitiveValueHolder = (PrimitiveValueHolder) SystemRepository
                .getObject("primitiveValueHolder");

        // 設定した"string value"が取得できる
        System.out.println(primitiveValueHolder.getStringValue());

        // 設定したtrueが取得できる
        System.out.println(primitiveValueHolder.isBoolValue());

        // 設定したfalseが取得できる
        System.out.println(primitiveValueHolder.getBoolWrapperValue());

        // 設定した2が取得できる
        System.out.println(primitiveValueHolder.getIntValue());

        // 設定した3が取得できる
        System.out.println(primitiveValueHolder.getIntWrapperValue());

        // 設定した5が取得できる
        System.out.println(primitiveValueHolder.getLongValue());

        // 設定した6が取得できる
        System.out.println(primitiveValueHolder.getLongWrapperValue());

        // 設定した["abc","def","ghi"]が取得できる
        for (String val : primitiveValueHolder.getStringArrayValue()) {
            System.out.println(val);
        }

        // 設定した[1, 2, 3]が取得できる
        for (int val : primitiveValueHolder.getIntArrayValue()) {
            System.out.println(val);
        }

        // 設定した[4, 5, 6]が取得できる
        for (Integer val : primitiveValueHolder.getIntegerArrayValue()) {
            System.out.println(val);
        }

    }
}
