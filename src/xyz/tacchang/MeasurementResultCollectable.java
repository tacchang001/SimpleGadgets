/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */
package xyz.tacchang;

/**
 * 処理時間記録
 */
public interface MeasurementResultCollectable {
    
    /**
     * 処理時間(計測結果)を記録に加える。
     * 
     * @param report 処理時間(計測結果) 
     */
    void append(final MeasurementReport report);
}
