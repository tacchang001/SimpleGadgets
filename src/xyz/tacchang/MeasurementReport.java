/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */
package xyz.tacchang;

/**
 * 処理時間計測結果
 */
public final class MeasurementReport {
   
    /**
     * 計測箇所を識別するための識別子である。
     * 計測箇所識別子と呼ぶ。
     */
    private String id;
    
    /**
     * 処理時間（ナノ秒）
     */
    private long elapsedEpochNano;
    
    /**
     * コンストラクタ。
     * 
     * @param id 計測箇所識別子
     * @param elapsedEpochNano 処理時間 
     */
    public MeasurementReport(final String id, final long elapsedEpochNano) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Argument 'id' is not allowed to be empty.");
        }
        this.id = id;
        this.elapsedEpochNano = elapsedEpochNano;
    }
    
    /**
     * コピーコンストラクタ。
     * @param rhs 右辺オブジェクト
     */
    public MeasurementReport(final MeasurementReport rhs) {
        this.id = rhs.id;
        this.elapsedEpochNano = rhs.elapsedEpochNano;
    }
    
    /**
     * 計測箇所識別子を取得する。
     * 
     * @return 計測箇所識別子 
     */
    public String getId() {
        return this.id;
    }
    
    /**
     * 処理時間を返す。
     * 
     * @return 処理時間（ナノ秒） 
     */
    public long getElapsedEpochNano() {
        return this.elapsedEpochNano;
    }
}
