package site.cnkj.common.utils.data;

import org.apache.commons.codec.binary.Base64;

public class UUIDUtil {

    public UUIDUtil() {
    }

    public static String longUuid() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    public static String shortUuid() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        return compressedUUID(uuid);
    }

    private static String compressedUUID(java.util.UUID uuid) {
        byte[] byUuid = new byte[16];
        long least = uuid.getLeastSignificantBits();
        long most = uuid.getMostSignificantBits();
        long2bytes(most, byUuid, 0);
        long2bytes(least, byUuid, 8);
        String compressUUID = Base64.encodeBase64URLSafeString(byUuid);
        return compressUUID;
    }

    private static void long2bytes(long value, byte[] bytes, int offset) {
        for(int i = 7; i > -1; --i) {
            bytes[offset++] = (byte)((int)(value >> 8 * i & 255L));
        }

    }

    public static String longUuid2short(String longUuid) {
        java.util.UUID uuid = java.util.UUID.fromString(longUuid);
        return compressedUUID(uuid);
    }

    public static String shortUuid2long(String shortUuid) {
        if (shortUuid.length() != 22) {
            throw new IllegalArgumentException("Invalid uuid!");
        } else {
            byte[] byUuid = Base64.decodeBase64(shortUuid + "==");
            long most = bytes2long(byUuid, 0);
            long least = bytes2long(byUuid, 8);
            java.util.UUID uuid = new java.util.UUID(most, least);
            return uuid.toString();
        }
    }

    private static long bytes2long(byte[] bytes, int offset) {
        long value = 0L;

        for(int i = 7; i > -1; --i) {
            value |= (long)((bytes[offset++] & 255) << 8 * i);
        }

        return value;
    }

}
