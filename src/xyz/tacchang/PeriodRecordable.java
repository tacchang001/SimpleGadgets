/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

package xyz.tacchang;

/**
 *
 */
public interface PeriodRecordable {

    void begin(final Uniquable id);
    
    void end(final Uniquable id);
}
