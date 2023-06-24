package xyz.tacchang;

/**
 * 処理時間計測結果
 */
public final class MeasurementReport {
   
    /**
     * 処理時間計測結果を識別するための識別子である。
     */
    private String id;
    
    /**
     * 処理時間（ナノ秒）
     */
    private long elapsedEpochNano;
    
    /**
     * コンストラクタ。
     * 
     * @param id 処理時間識別子
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
     * 識別子を取得する。
     * 
     * @return 識別子 
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
