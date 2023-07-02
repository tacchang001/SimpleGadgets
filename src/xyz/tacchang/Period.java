/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

package xyz.tacchang;


/**
 *
 */
public final class Period implements AutoCloseable {
    
    /**
     * 計測開始している場合は真である。
     */
    private boolean started = false;
    
    /**
     * stop()メソッドではなく自動終了した場合は真である。
     */
    private boolean automaticallyStopped = false;
    
    /**
     * PeriodRecordableに渡す計測箇所識別子である。
     * 本クラスでは処理に使用しない。
     */
    private final PeriodId id;
    
    /**
     * 結果の出力先である。
     * NULL許容。NULLの場合は出力しない。
     */
    private final PeriodRecordable recorder;
    
    /**
     * 最後のエラー内容。
     */
    private String lastError = "not started";
    
    public Period() {
        this.id = new PeriodId();
        this.recorder = PeriodRecorder.getInstance();
        start();
    }
    
    /**
     * コンストラクタ。
     * 自動で開始する。
     * 
     * @param id PeriodRecordableに渡す計測箇所識別子。
     * @param recorder MeasurementResultCollectableオブジェクト。
     */
    public Period(
            final PeriodId id, 
            final PeriodRecordable recorder) {
        this.id = id;
        this.recorder = recorder;
        start();
    }
    
    /**
     * コンストラクタ。
     * 
     * @param id PeriodRecordableに渡す計測箇所識別子。
     * @param recorder PeriodRecordableオブジェクト。
     * @param immediately 真の場合は計測を自動で開始する。
     */
    public Period(
            final PeriodId id, 
            final PeriodRecordable recorder,
            boolean immediately) {
        this.id = id;
        this.recorder = recorder;
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
            this.started = true;
            recorder.begin(id);
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
            this.started = false;
            recorder.end(id);
            this.lastError = "";
            this.automaticallyStopped = false;
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
    
    /**
     *
     */
    public class PeriodId implements Uniquable, Comparable<PeriodId> {

        final String className;

        final String methodName;

        final String fileName;

        final int lineNumber;

        final long threadId;

        public PeriodId() {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
            this.className = ste.getClassName();
            this.methodName = ste.getMethodName();
            this.fileName = ste.getFileName();
            this.lineNumber = ste.getLineNumber();
            this.threadId = Thread.currentThread().getId();
        }

        public PeriodId(
                final String className,
                final String methodName,
                final String fileName,
                int lineNumber,
                long threadId
        ) {
            this.className = className;
            this.methodName = methodName;
            this.fileName = fileName;
            this.lineNumber = lineNumber;
            this.threadId = threadId;
        }

        public PeriodId(final PeriodId rhs) {
            this.className = rhs.className;
            this.methodName = rhs.methodName;
            this.fileName = rhs.fileName;
            this.lineNumber = rhs.lineNumber;
            this.threadId = rhs.threadId;
        }

        public String getClassName() {
            return this.className;
        }

        public String getMethodName() {
            return this.methodName;
        }

        public String getFileName() {
            return this.fileName;
        }

        public int getLineNumber() {
            return this.lineNumber;
        }

        public long getThreadId() {
            return this.threadId;
        }

        @Override
        public String getKey() {
            StringBuilder key = new StringBuilder(256);
            key.append(this.className);
            key.append('-');
            key.append(String.valueOf(this.lineNumber));
            key.append('-');
            key.append(String.valueOf(this.threadId));
            return key.toString();
        }

        @Override
        public int compareTo(PeriodId o) {
            return this.getKey().compareTo(o.getKey());
        }
    }
}
