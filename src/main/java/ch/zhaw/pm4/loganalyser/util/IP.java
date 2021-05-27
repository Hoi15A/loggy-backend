package ch.zhaw.pm4.loganalyser.util;

import lombok.experimental.UtilityClass;

import java.net.InetAddress;

/**
 * Utility class for working with IP addresses
 */
@UtilityClass
public class IP {
    /**
     * Converts an InetAddress into a long
     * @param ip IP address that should be converted into a long
     * @return The address as a numerical value
     */
    public long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet;
        }
        return result;
    }
}
