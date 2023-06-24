package xyz.tacchang;

/**
 * 処理時間計測。
 * 時間単位はプログラムを実行しているマシンの時間である。
 * 便宜上これを計算機クロック時刻と呼ぶ。
 */
public final class StopWatch implements AutoCloseable {
    
    /**
     * 計測開始している場合は真である。
     */
    private boolean started = false;
    
    /**
     * stop()メソッドではなく自動終了した場合は真である。
     */
    private boolean automaticallyStopped = false;
    
    /**
     * 開始時の計算機クロック時刻である。
     */
    private long beginNano = 0;
    
    /**
     * 終了時の計算機クロック数である。
     */
    private long endNano = 0;
    
    /**
     * MeasurementResultCollectableに渡す計測箇所識別子である。
     * 本クラスでは処理に使用しない。
     */
    private final String id;
    
    /**
     * 結果の出力先である。
     * NULL許容。NULLの場合は出力しない。
     */
    private final MeasurementResultCollectable reporter;
    
    /**
     * 最後のエラー内容。
     */
    private String lastError = "not started";
    
    /**
     * デフォルトコンストラクタ。
     * 計測を自動で開始する。
     * 結果は出力しない。
     */
    public StopWatch() {
        this(true, "", null);
    }
    
    /**
     * コンストラクタ。
     * 計測を自動で開始しない場合などクライアントで制御したい場合に使用する。
     * 
     * @param immediately 真の場合は計測を自動で開始する。 
     */
    public StopWatch(boolean immediately) {
        this(immediately, "", null);
    }
    
    /**
     * コンストラクタ。
     * 計測結果をMeasurementResultCollectableに登録する必要がある場合に使用する。
     * 
     * @param immediately 真の場合は計測を自動で開始する。
     * @param id MeasurementResultCollectableに渡す計測箇所識別子。
     * @param reporter MeasurementResultCollectableオブジェクト。
     */
    public StopWatch(
            boolean immediately,
            final String id, 
            final MeasurementResultCollectable reporter) {
        this.id = id;
        this.reporter = reporter;
        if (immediately) {
            start();
        }
    }
    
    /**
     * 計測を開始する。
     * 
     * @return 計測が開始できた場合は真である。
     */
    public boolean start() {
        if (!this.started) {
            this.beginNano = System.nanoTime();
            this.endNano = 0;
            this.started = true;
            
            return true;
        }
        return false;
    }
    
    /**
     * 計測を終了する。
     * 
     * @return 計測を終了できた場合は真である。 
     */
    public boolean stop() {
        if (this.started) {
            this.endNano = System.nanoTime();
            this.started = false;
            this.lastError = "";
            this.automaticallyStopped = false;
            
            if (reporter != null) {
                MeasurementReport report = new MeasurementReport(this.id, this.getElapsedEpochNano());
                reporter.append(report);
            }
            
            return true;
        }
        return false;
    }

    /**
     * 自動で計測を終了する。
     * もしオブジェクト破棄時にstop()が呼ばれていない場合は自動で計測を終了する。
     * try-with-resource文におけるリソース指定を想定している。
     */
    @Override
    public void close() {
        try {
            if (!stop()) {
                this.automaticallyStopped = true;
            }
        }
        catch (Exception ex) {
            this.lastError = ex.getMessage();
        }
    }
    
    /**
     * 開始時の計算機クロック時刻を返す。
     * 
     * @return 開始時の計算機クロック時刻
     */
    public long getBeginEpochNano() {
        return this.beginNano;
    }
    
    /**
     * 終了時の計算機クロック時刻
     * 
     * @return 終了時の計算機クロック時刻
     */
    public long getEndEpochNano() {
        return this.endNano;
    }

    /**
     * 処理時間を計算機クロック(ナノ秒)で返す。
     * 
     * @return 処理時間（ナノ秒）
     */
    public long getElapsedEpochNano() {
        return this.endNano - this.beginNano;
    }
    
    /**
     * 処理時間を計算機クロック(ミリ秒)で返す。
     * 
     * @return 処理時間（ミリ秒）
     */
    public long getElapsedEpochMilli() {
        return (this.endNano - this.beginNano) / 1000 / 1000;
    }
    
    /**
     * 処理時間を取得してみる。
     * 
     * @param ms 取得できた場合は処理時間（ナノ秒）。
     * @return 取得できた場合は真である。
     */
    public boolean tryElapsedEpochNano(Long ms) {
        if (!started) {
            long elapsed = this.endNano - this.beginNano;
            ms = elapsed / 1000 / 1000;
            return true;
        }
        return false;
    }
    
    /**
     * 自動終了したか否かを返す。
     * 
     * @return 自動終了した場合は真である。 
     */
    public boolean isAutomaticallyStopped() {
        return this.automaticallyStopped;
    }
    
    /**
     * 最後のエラーを返す。
     * 
     * @return 最後のエラー 
     */
    public String getLastMessage() {
        return this.lastError;
    }
}
