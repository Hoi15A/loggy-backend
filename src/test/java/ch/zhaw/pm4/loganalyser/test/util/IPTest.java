package ch.zhaw.pm4.loganalyser.test.util;

import ch.zhaw.pm4.loganalyser.util.IP;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IPTest {
    private final static byte[] IP_BYTES = {127, 0, 0, 1};
    private final static long IP_STRING_AS_LONG = 2130706433L;

    @Test
    void testConvertIPToLong() {
        InetAddress addr = mock(InetAddress.class);
        when(addr.getAddress()).thenReturn(IP_BYTES);

        long result = IP.ipToLong(addr);

        assertEquals(IP_STRING_AS_LONG, result);
        verify(addr, times(1)).getAddress();
    }

    @Test
    void testConvertInvalidIPToLong() {
        assertThrows(NullPointerException.class, () -> IP.ipToLong(null));
    }
}
