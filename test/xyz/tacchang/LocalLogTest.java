/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

package xyz.tacchang;

import java.util.logging.Level;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class LocalLogTest {

    public LocalLogTest() {
    }

    @Test
    public void test01() {
        LocalLog.finest("log level=%s", String.valueOf(Level.FINEST));
        LocalLog.finer("log level=%s", String.valueOf(Level.FINER));
        LocalLog.fine("log level=%s", String.valueOf(Level.FINE));
        LocalLog.info("log level=%s", String.valueOf(Level.INFO));
        LocalLog.warning("log level=%s", String.valueOf(Level.WARNING));
        LocalLog.config("log level=%s", String.valueOf(Level.CONFIG));
        LocalLog.severe("log level=%s", String.valueOf(Level.SEVERE));
    }

}