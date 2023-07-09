/*
 *  Copyright (c) 2023 YOKOUCHI Tatsuro
 *  Released under the MIT license
 *  https://opensource.org/licenses/mit-license.php
 */

/*
    @authority https://github.com/jbock-java/either
*/
package xyz.tacchang;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class EitherTest {

    public EitherTest() {
    }

    @Test
    public void test01() {
        assertEquals(Either.right("1"), Either.right("1"));
        assertEquals(Either.right("1").hashCode(), Either.right("1").hashCode());
        
        assertNotEquals(Either.right("1"), Either.right("2"));
        assertNotEquals(Either.right("1").hashCode(), Either.right("2").hashCode());
        assertNotEquals(Either.<Object, Object>right("1"), Either.right(1));

        assertEquals(Either.left("1"), Either.left("1"));
        assertEquals(Either.left("1").hashCode(), Either.left("1").hashCode());

        assertNotEquals(Either.left("1"), Either.left("2"));
        assertNotEquals(Either.left("1").hashCode(), Either.left("2").hashCode());
        assertNotEquals(Either.<Object, Object>left("1"), Either.left(1));

        assertNotEquals(Either.right("1"), Either.left("1"));
        assertNotEquals(Either.right("1").hashCode(), Either.left("1").hashCode());
    }

    @Test
    public void test02() {
        Either<?, String> either = Either.right("1");
        assertTrue(either.isRight());
        assertFalse(either.isLeft());
        assertEquals(Optional.of("1"), either.getRight());
        assertEquals(Optional.empty(), either.getLeft());
    }

    @Test
    public void test03() {
        Either<String, ?> either = Either.left("1");
        assertTrue(either.isLeft());
        assertFalse(either.isRight());
        assertEquals(Optional.of("1"), either.getLeft());
        assertEquals(Optional.empty(), either.getRight());
    }

    @Test
    public void test04() {
        Either<String, String> left = Either.left("1");
        assertSame(left, left.map(s -> "A")); // Left is unchanged
        Either<?, String> right = Either.right("1");
        assertEquals(Either.right(1), right.map(Integer::parseInt));
    }

    @Test
    public void test05() {
        Either<Integer, String> left = Either.left(2);
        assertSame(left, left.flatMap(s -> Either.right("1"))); // Left is unchanged
        assertSame(left, left.flatMap(s -> Either.left(1))); // Left is unchanged
        Either<Integer, String> right = Either.right("1");
        assertEquals(Either.right(11), right.flatMap(s -> Either.right(11)));
        assertEquals(Either.left(11), right.flatMap(s -> Either.left(11)));
    }

    @Test
    public void test06() {
        Either<String, ?> left = Either.left("1");
        assertEquals(Either.left(1), left.mapLeft(Integer::parseInt));
        Either<String, String> right = Either.right("1");
        assertSame(right, right.mapLeft(s -> "A")); // Right is unchanged
    }

    @Test
    public void test07() {
        Either<String, Integer> left = Either.left("1");
        assertEquals(Either.left(11), left.flatMapLeft(s -> Either.left(11)));
        assertEquals(Either.right(11), left.flatMapLeft(s -> Either.right(11)));
        Either<String, Integer> right = Either.right(2);
        assertSame(right, right.flatMapLeft(s -> Either.left("1"))); // Right is unchanged
        assertSame(right, right.flatMapLeft(s -> Either.right(1))); // Right is unchanged
    }

    @Test
    public void test08() {
        Either<String, ?> left = Either.left("1");
        assertSame(left, left.filter(r -> Optional.of("2"))); // Left is unchanged
        assertSame(left, left.filter(r -> Optional.empty())); // Left is unchanged
        Either<String, String> right = Either.right("1");
        assertEquals(Either.left("2"), right.filter(r -> Optional.of("2")));
        assertEquals(right, right.filter(r -> Optional.empty()));
    }

    @Test
    public void test09() {
        Either<?, String> right = Either.right("1");
        assertSame(right, right.filterLeft(r -> Optional.of("2"))); // Right is unchanged
        assertSame(right, right.filterLeft(r -> Optional.empty())); // Right is unchanged
        Either<String, String> left = Either.left("1");
        assertEquals(Either.right("2"), left.filterLeft(r -> Optional.of("2")));
        assertEquals(left, left.filterLeft(r -> Optional.empty()));        
    }

    @Test
    public void test10() {
        String[] output = {"1"};
        Either<Integer, Integer> left = Either.left(1);
        left.ifLeftOrElse(l -> output[0] = "L", r -> output[0] = "R");
        assertEquals("L", output[0]);
        Either<Integer, Integer> right = Either.right(1);
        right.ifLeftOrElse(l -> output[0] = "L", r -> output[0] = "R");
        assertEquals("R", output[0]);        
    }

    @Test
    public void test11() {
        Either<String, Integer> left = Either.left("1");
        assertEquals("1", left.fold(Objects::toString, Objects::toString));
        Either<String, Integer> right = Either.right(2);
        assertEquals("2", right.fold(Objects::toString, Objects::toString));        
    }

    @Test
    public void test12() {
        Either<String, ?> left = Either.left("1");
        IOException x = assertThrows(IOException.class, () -> left.orElseThrow(IOException::new));
        assertEquals("1", x.getMessage());
        Either<String, String> right = Either.right("2");
        assertEquals("2", right.orElseThrow(IllegalArgumentException::new));        
    }
}