/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */
package xyz.tacchang;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 標準処理時間記録
 * シングルトンである。
 */
public class MeasurementResult implements MeasurementResultCollectable {

    /**
     * 記録簿
     */
    protected final Map<LocalDateTime, MeasurementReport> records = new HashMap<>();
    
    /**
     * シングルトンインスタンス
     * Bill Push型シングルトンである。
     */
    protected static class SingletonHolder {
        private static final MeasurementResult INSTANCE = new MeasurementResult();  
    }
    
    protected MeasurementResult() {}
    
    /**
     * 標準処理時間記録オブジェクトを取得する。
     * 
     * @return 標準処理時間記録オブジェクト
     */
    public static MeasurementResult getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void append(final MeasurementReport report) {
        records.put(
                LocalDateTime.now(),
                report
        );
    }
    
    /**
     * 記録簿を取得する。
     * 記録簿は読み取り専用である。
     * 
     * @return 記録簿 
     */
    public Map<LocalDateTime, MeasurementReport> getImmutableRecords() {
        return Collections.unmodifiableMap(this.records);
    }
}
