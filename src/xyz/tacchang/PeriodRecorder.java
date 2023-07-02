/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

package xyz.tacchang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 */
public class PeriodRecorder implements PeriodRecordable {
    
    /**
     * シングルトンインスタンス
     * Bill Push型シングルトンである。
     */
    protected static class SingletonHolder {
        private static final PeriodRecorder INSTANCE = new PeriodRecorder();  
    }
    
    protected PeriodRecorder() {}
        
    /**
     * 標準処理時間記録オブジェクトを取得する。
     * 
     * @return 標準処理時間記録オブジェクト
     */
    public static PeriodRecorder getInstance() {
        return SingletonHolder.INSTANCE;
    }
    
    class Record {
        private final Uniquable id;
        private final long epochNano;
        private final boolean begin;
                
        Record(final Uniquable id, final boolean begin, final long epochNano) {
            this.id = id;
            this.begin = begin;
            this.epochNano = epochNano;
        }
        
        public Uniquable getId() {
            return this.id;
        }
        
        public boolean isBegin() {
            return this.begin;
        }
        
        public long getEpochNano() {
            return this.epochNano;
        }
        
        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(256);
            result.append("{");
            result.append(this.id.getKey());
            result.append(",");
            result.append(this.begin ? "begin" : "end");
            result.append(",");
            result.append(this.epochNano);
            result.append("}");
            return result.toString();
        }
    }
    
    private List<Record> records = new ArrayList<>();
    
    @Override
    public void begin(Uniquable id) {
        final long epochNano = System.nanoTime();
        final Record record = new Record(id, true, epochNano);
        final Tuple<String, Boolean> key = new Tuple(id.getKey(), true);
        records.add(record);
    }

    @Override
    public void end(Uniquable id) {
        final long epochNano = System.nanoTime();
        final Record record = new Record(id, false, epochNano);
        final Tuple<String, Boolean> key = new Tuple(id.getKey(), false);
        records.add(record);
    }
    
    /**
     * 記録簿を取得する。
     * 記録簿は読み取り専用である。
     * 
     * @return 記録簿 
     */
    public List<Record> getImmutableRecords() {
        return Collections.unmodifiableList(records);
    }
    
    public void clear() {
        this.records.clear();
    }
}