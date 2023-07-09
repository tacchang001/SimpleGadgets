/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

package xyz.tacchang;

import java.util.AbstractMap;

/**
 *
 */
public class Pair<A, B> {
    private final A a;
    private final B b;
    
    public Pair(final A a, final B b) {
        this.a = a;
        this.b = b;
    }
    
    public A getOne() {
        return this.a;
    }
    
    public B getTwo() {
        return this.b;
    }
}
