package net.openhft.chronicle.bytes.readme;

import net.openhft.chronicle.bytes.HexDumpBytes;
import net.openhft.chronicle.bytes.NativeBytes;
import net.openhft.chronicle.bytes.StopCharTesters;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeFalse;

public class StringsTest {
    @Test
    public void testString() {
        assumeFalse(NativeBytes.areNewGuarded());
        HexDumpBytes bytes = new HexDumpBytes();
        bytes.comment("write8bit").write8bit("£ 1");
        bytes.comment("writeUtf8").writeUtf8("£ 1");
        bytes.comment("append8bit").append8bit("£ 1").append('\n');
        bytes.comment("appendUtf8").appendUtf8("£ 1").append('\n');

        System.out.println(bytes.toHexString());

        String a = bytes.read8bit();
        String b = bytes.readUtf8();
        String c = bytes.parse8bit(StopCharTesters.CONTROL_STOP);
        String d = bytes.parseUtf8(StopCharTesters.CONTROL_STOP);
        assertEquals("£ 1", a);
        assertEquals("£ 1", b);
        assertEquals("£ 1", c);
        assertEquals("£ 1", d);
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));
        System.out.println(System.identityHashCode(c));
        System.out.println(System.identityHashCode(d));
        // uses the pool but a different hash.
        // assertSame(a, c); // uses a string pool
        assertSame(b, c); // uses a string pool
        assertSame(b, d); // uses a string pool
        bytes.release();
    }

    @Test
    public void testNull() {
        HexDumpBytes bytes = new HexDumpBytes();
        bytes.comment("write8bit").write8bit((String) null);
        bytes.comment("writeUtf8").writeUtf8(null);

        System.out.println(bytes.toHexString());

        String a = bytes.read8bit();
        String b = bytes.readUtf8();
        assertEquals(null, a);
        assertEquals(null, b);
        bytes.release();
    }
}
