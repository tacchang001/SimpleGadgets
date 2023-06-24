package xyz.tacchang;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class StopWatchTest {
    
    public StopWatchTest() {
    }

    @Test
    public void test01() {
        StopWatch sp = new StopWatch();
        assertNotNull(sp);
        assertFalse(sp.getLastMessage().isEmpty());
        final int waitMs = 50;
        try {
            Thread.sleep(waitMs);
        } catch (InterruptedException ex) {
            Logger.getLogger(StopWatchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long nano = 0L;
        assertFalse(sp.tryElapsedEpochNano(nano));
        assertFalse(sp.getLastMessage().isEmpty());
        assertTrue(sp.stop());
        assertTrue(sp.tryElapsedEpochNano(nano));
        assertTrue(sp.getElapsedEpochMilli() >= waitMs - 2);
        assertTrue(sp.getLastMessage().isEmpty());
        assertFalse(sp.isAutomaticallyStopped());
    }

    @Test
    public void test02() {
        StopWatch sp = new StopWatch(false);
        assertNotNull(sp);
        assertTrue(sp.start());
        assertFalse(sp.getLastMessage().isEmpty());
        final int waitMs = 50;
        try {
            Thread.sleep(waitMs);
        } catch (InterruptedException ex) {
            Logger.getLogger(StopWatchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Long nano = 0L;
        assertFalse(sp.tryElapsedEpochNano(nano));
        assertFalse(sp.getLastMessage().isEmpty());
        assertTrue(sp.stop());
        assertTrue(sp.tryElapsedEpochNano(nano));
        assertTrue(sp.getElapsedEpochMilli() >= waitMs - 2);
        assertTrue(sp.getLastMessage().isEmpty());
        assertFalse(sp.isAutomaticallyStopped());
    }

    @Test
    public void test03() {
        MeasurementResult reporter = MeasurementResult.getInstance();
        assertNotNull(reporter);

        try (StopWatch sp = new StopWatch(true, "id01", reporter))
        {
            assertNotNull(sp);
            assertFalse(sp.getLastMessage().isEmpty());
            final int waitMs = 50;
            try {
                Thread.sleep(waitMs);
            } catch (InterruptedException ex) {
                Logger.getLogger(StopWatchTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            Long nano = 0L;
            assertFalse(sp.tryElapsedEpochNano(nano));
            assertFalse(sp.getLastMessage().isEmpty());
        }
            
        Map<LocalDateTime, MeasurementReport> records = reporter.getImmutableRecords();
        assertNotNull(records);
        assertTrue(records.size() == 1);
        for (Map.Entry<LocalDateTime, MeasurementReport> entry : records.entrySet()) {
            LocalDateTime t = entry.getKey();
            MeasurementReport record = entry.getValue();
            System.out.printf("%s, %s, %d%n", t, record.getId(), record.getElapsedEpochNano());
        }
    }
    
}
