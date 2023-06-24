/**
 * @authority 
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
