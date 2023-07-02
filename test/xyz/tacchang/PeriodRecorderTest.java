/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */
package xyz.tacchang;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import xyz.tacchang.PeriodRecorder.Record;

/**
 *
 */
public class PeriodRecorderTest {

    public PeriodRecorderTest() {
    }

    @Test
    public void test01() {
        PeriodRecorder recorder = PeriodRecorder.getInstance();
        assertNotNull(recorder);
        recorder.clear();

        try (Period p = new Period()) {
            assertEquals(1, recorder.getImmutableRecords().size());
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        assertEquals(2, recorder.getImmutableRecords().size());
        
        try (Period p = new Period()) {
            assertEquals(3, recorder.getImmutableRecords().size());
            try {
                Thread.sleep(150);
            } catch (InterruptedException ex) {
            }
        }
        assertEquals(4, recorder.getImmutableRecords().size());
    }

    @Test
    public void test02() {
        PeriodRecorder recorder = PeriodRecorder.getInstance();
        recorder.clear();
        
        final int loop = 3;
        for (int i=0; i<loop; i++) {
            try (Period p = new Period()) {
                try (Period p1 = new Period()) {
                    try (Period p11 = new Period()) {
                        try (Period p121 = new Period()) {
                            try (Period p1211 = new Period()) {
                            }
                            try (Period p1212 = new Period()) {
                            }
                        }
                        try (Period p122 = new Period()) {
                        }
                    }
                    try (Period p12 = new Period()) {
                    }
                }

                try (Period p2 = new Period()) {
                    try (Period p21 = new Period()) {
                    }        
                    try (Period p22 = new Period()) {
                        try (Period p221 = new Period()) {
                        }        
                        try (Period p222 = new Period()) {
                        }        
                        try (Period p223 = new Period()) {
                        }        
                    }        
                    try (Period p23 = new Period()) {
                    }        
                    try (Period p24 = new Period()) {
                    }        
                }        
            }
        }

        List<Record> records = recorder.getImmutableRecords();
        assertEquals(16 * 2 * loop, records.size());
        
        final String TAB = "\t";
        Deque<Record> deep = new ArrayDeque<>();
        for(Record r : records) {
            if (r.isBegin()) {
                final String tab = new String(new char[deep.size()]).replace("\0", TAB);
                System.out.printf("%s%s%n", tab, r);
            
                deep.push(r);
            } else {
                assertFalse(deep.isEmpty());
                Record rb = deep.pop();

                final long beginNano = rb.getEpochNano();
                final long endNano = r.getEpochNano();
                final String tab = new String(new char[deep.size()]).replace("\0", TAB);
                System.out.printf("%s%s - %s%n", tab, r, Double.valueOf((endNano - beginNano)/1000.0));
            
                assertEquals(rb.getId(), r.getId());
            }
        }
    }

}
