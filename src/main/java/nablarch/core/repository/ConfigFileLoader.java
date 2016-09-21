package nablarch.core.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.core.util.FileUtil;
import nablarch.core.util.annotation.Published;

/**
 * 設定ファイルから文字列の設定値を読み込むクラス。
 * 
 * このクラスで使用する特殊文字は '=' '#' '\' の3文字（下記参照）。
 * <dl>
 *         <dt>デリミタ文字（'='）
 *             <dd>デリミタ文字は'='のみで、空白（タブを含む）や":"も文字列の一部とみなす。
 *                 (いわゆるpropertiesファイルとは異なる。）
 *                 但し、キー及び値はそれぞれ前後の空白（タブを含む）をトリミングする。
 *                 (" A B "(スペースAスペースBスペース)という文字列は
 *                 "A B"(AスペースB)となる。キーの'A'と'a'は区別される。)
 *                 デリミタ文字'='で区切られた３つめ以降のトークンは無視する。
 *                 <br>'='をキーまたは値に含めたい場合は前に'\'を付加する。
 *         <dt>コメント文字（'#'）
 *             <dd>コメント文字'#'を使用するとその行の以降の文字列はコメントとみなす。
 *                 '#'によるコメントを除去する処理は行連結の前に行われるので、
 *                 継続行中でも使用可能（下記「使用例」参照）。
 *                 <br>'#'をキーまたは値に含めたい場合は前に'\'を付加する。
 *         <dt>改行文字（'\'）
 *             <dd>キーと値のセットは行末に'\'を指定することによって行をまたがることが可能。
 *                 その場合'\'を除いた文字列と次の行の先頭の空白（タブを含む）を除いた
 *                 文字列を連結する。（'\'を除いた文字列の後方の空白は維持する。）
 *                 <br>キーまたは値の行末に'\'を含めたい場合は前に'\'を付加する。
 *         <dt>エスケープ文字（'\'）
 *             <dd>'\'を記述すると次の１文字を特殊文字ではなく一般文字として扱う。
 *                 <br>'\'をキーまたは値に含めたい場合は前に'\'を付加する。
 * </dl>
 * 読み込むファイルの記述例：<br><pre>
 *  # キー＝"key"、値＝"value"の場合
 *  key = value # comment
 *  key = value = comment
 *
 *  # キー＝"key"、値＝"value1 = value2"の場合
 *  key = value1 \= value2  #comment
 *  key = \
 *      value1 \= value2
 *
 *  # キー＝"key"、値＝"value1,value2,value3"の場合
 *  key =   value1,value2,value3    # comment
 *  key =   value1,\
 *          value2,\
 *          value3 # comment
 *  key =   value1,\    # comment
 *          value2,\    # comment
 *          value3      # comment
 * 
 *  # 下記はNG。
 *  key =   value1,     # comment \
 *          value2,     # comment \
 *          value3      # comment
 * </PRE>
 * <p/>
 * なお、本クラスはデフォルトでは設定ファイルをUTF-8エンコーディングで読み込む。
 * エンコーディングを変更する場合は、ConfigFileクラスのencodingプロパティにエンコーディングを設定してから load() メソッドを呼び出すこと。
 * 
 * @author Koichi Asano 
 * @see nablarch.core.repository.di.config.xml.schema.ConfigFile
 */
@Published(tag = "architect")
public class ConfigFileLoader implements ObjectLoader {

    /**
     * ロガー。
     */
    private static final Logger LOGGER = LoggerManager.get(ConfigFileLoader.class);

    /**
     * エスケープ文字＆行連結文字。
     */
    private static final char ESC_CHAR = '\\';

    /**
     * デリミタ文字。
     */
    private static final char DELIMITER_CHAR = '=';

    /**
     * コメント文字。
     */
    private static final char COMMENT_CHAR = '#'; // コメント文字

    /** 
     * 設定ファイルのデフォルトエンコーディング。
     */
    private static final String DEFAULT_CONFIG_FILE_ENCODING = "UTF-8";

    /**
     * 入力ファイル。
     */
    private String url;
    /**
     * 入力ストリーム。
     */
    private InputStream inStream;
    
    /**
     * 入力ストリームのエンコーディング。
     */
    private String encoding;

    /**
     * コンストラクタ。
     * 
     * @param url ロードするファイル。
     */
    public ConfigFileLoader(String url) {
        this(url, null);
    }

    /**
     * コンストラクタ。
     * @param url ロードするファイルを表すURL表現。
     * @param encoding ファイルのエンコーディング。
     */
    public ConfigFileLoader(String url, String encoding) {
        this.url = url;
        this.encoding = encoding;
    }

    /**
     * コンストラクタ。
     * 
     * @param stream ロードするファイルのストリーム。
     */
    public ConfigFileLoader(InputStream stream) {
        this.inStream = stream;
    }

    /**
     * コンストラクタ。
     * 
     * @param stream ロードするファイルのストリーム。
     * @param encoding ファイルのエンコーディング。
     */
    public ConfigFileLoader(InputStream stream, String encoding) {
        this.inStream = stream;
        this.encoding = encoding;
    }

    /**
     * {@inheritDoc} <br/>
     * 
     * ConfigFileLoaderでは、プロパティファイルに書かれたキーと値の組合せを
     * そのままMapを返す。
     * このため、値は常に文字列となる。
     */
    public Map<String, Object> load() {
        Map<String, Object> values;
        BufferedReader reader = null;
        LOGGER.logInfo("load environment config file." 
                + " file = " + url);
        try {
            if (url != null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.logTrace(" config file opened. "
                            + " url = " + url + "");
                }
            }

            if (inStream == null) {
                inStream = FileUtil.getResource(url);
            }
            
            String configFileEncoding;
            if (this.encoding != null) {
                configFileEncoding = this.encoding;
            } else {
                configFileEncoding = DEFAULT_CONFIG_FILE_ENCODING;
            }

            reader = new BufferedReader(new InputStreamReader(inStream,
                    configFileEncoding));

            values = readFile(reader);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    "config file read failed.", e);
        } catch (IOException e) {
            // readFile のエラーなので、到達不能です。
            throw new RuntimeException(
                    "config file read failed.", e);
        } finally {
            FileUtil.closeQuietly(reader);
            if (url != null) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.logTrace(" config file closed. "
                            + " url = " + url + "");
                }
            }
        }

        return values;
    }

    /**
     * ファイルの読み込み処理を行う。
     * 
     * @param reader ファイルのリーダ
     * @return 読み込み結果
     * @throws IOException readLineに失敗した場合
     */
    private Map<String, Object> readFile(BufferedReader reader)
            throws IOException {

        Map<String, Object> values = new HashMap<String, Object>();
        String sentence = null;

        // 一文(行末にESC_CHARがある場合に複数行を連結したもの)を読み込み
        while (null != (sentence = readSentence(reader))) {

            StringBuilder key = new StringBuilder(); // Key
            StringBuilder value = new StringBuilder(); // Value

            // キー取得
            int i = 0;
            boolean isKeyOk = false;
            for (; i < sentence.length(); i++) {
                if (sentence.charAt(i) == ESC_CHAR) {
                    // 行末にESC_CHARがないことは保証されている。
                    key.append(sentence.charAt(++i)); 
                } else if (sentence.charAt(i) == DELIMITER_CHAR) {
                    if (key.toString().trim().length() != 0) {
                        isKeyOk = true;
                    }
                    break;
                } else {
                    key.append(sentence.charAt(i));
                }
            }

            if (!isKeyOk) {
                // 構文NG -> 次の文へ
                continue;
            }

            // 値取得
            StringBuilder sb = new StringBuilder();
            for (i++; i < sentence.length(); i++) { // １文字取得Loop(Value)
                sb.append(sentence.charAt(i));
                if (sentence.charAt(i) == ESC_CHAR) {
                    value.append(sentence.charAt(++i)); // ++i : 判定済みのため安全
                } else {
                    value.append(sentence.charAt(i));
                }
            } // １文字取得Loop(Value)

            String trimmedKey = key.toString().trim();
            String trimmedValue = value.toString().trim();

            // キーと値を格納
            if (0 != trimmedValue.length()) {
                if (values.containsKey(trimmedKey)) {
                    logWarn("duplicate key ["
                            + trimmedKey + "]. change [" + values.get(trimmedKey)
                            + "] to [" + trimmedValue + "]");
                }

                values.put(trimmedKey, trimmedValue);
            }

        } // １行読込みLoop
        return values;
    }

    /**
     * ワーニングログを出力する。
     * @param warnMessage ワーニングログメッセージ
     */
    private void logWarn(String warnMessage) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.logWarn(warnMessage);
        }
    }
    
    /**
     * 論理的な一行(エスケープ文字直後の改行ではない一行)を取得する。
     * 
     * @param reader 読み込みを行うReader
     * @return 論理的な一行
     * @throws IOException readLineに失敗した場合
     */
    private String readSentence(BufferedReader reader) throws IOException {
        String line = null;
        StringBuilder sentence = new StringBuilder();

        // １行読込みLoop
        while (null != (line = reader.readLine())) {
            String trimmedLine = line.trim();
            
            // コメント削除（エスケープされていないCOMMENT_CHARを判定）
            trimmedLine = removeComment(trimmedLine);
            
            // 行連結（エスケープされていないESC_CHARを判定）
            int escCnt = 0; // 文後方からの連続したESC_CHARの数
            for (int i = trimmedLine.length() - 1; i >= 0; i--, escCnt++) {
                if (trimmedLine.charAt(i) != ESC_CHAR) {
                    break;
                }
            }
            // 行末のESC_CHARが奇数(改行の連結を意味する)の場合
            if (escCnt % 2 != 0) {
                // 行末のESC_CHARを削って、次の行を連結
                sentence.append(trimmedLine
                        .substring(0, trimmedLine.length() - 1));
                continue;
            }
            sentence.append(trimmedLine);
            break;
        }
        if (line == null) {
            return null;
        } else {
            return sentence.toString();
        }
    }

    /**
     * 1行の文字列からコメントを削除する。
     * 
     * @param line 1行の文字列
     * @return トリム済みの行
     */
    private String removeComment(String line) {
        if (line.indexOf(COMMENT_CHAR) < 0) {
            return line;
        }
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ESC_CHAR) {
                // エスケープは無視
                i++;
            } else {
                if (line.charAt(i) == COMMENT_CHAR) {
                    return line.substring(0, i);
                }
            }
        }
        return line;
    }

}
