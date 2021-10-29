package site.cnkj.common.utils.data;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.StringJoiner;

public class GlobalId {

    private static final Logger log = LoggerFactory.getLogger(GlobalId.class);
    private long twepoch = 1420041600000L;
    private static long workerIdBits = 5L;
    private static long datacenterIdBits = 5L;
    private static long maxWorkerId;
    private static long maxDatacenterId;
    private long sequenceBits = 12L;
    private long workerIdShift;
    private long datacenterIdShift;
    private long timestampLeftShift;
    private long sequenceMask;
    private long workerId;
    private long datacenterId;
    private long sequence;
    private long lastTimestamp;
    private static GlobalId defaultGlobalId;

    public GlobalId() {
        this.workerIdShift = this.sequenceBits;
        this.sequenceMask = ~(-1L << (int)this.sequenceBits);
        this.sequence = 0L;
        this.lastTimestamp = -1L;
        this.workerId = 0L;
        this.datacenterId = 0L;
        this.build();
    }

    public static long getDatacenterIdByIp(String ipAddr) {
        byte[] mac = (ipAddr + UUIDUtil.longUuid()).getBytes();
        long id = (255L & (long)mac[mac.length - 2] | 65280L & (long)mac[mac.length - 1] << 8) >> 6;
        id %= maxDatacenterId + 1L;
        return id;
    }

    public static long getDatacenterIdByIp() {
        return getDatacenterIdByIp(getIpAddress());
    }

    public static String getIpAddress() {
        String ipAddr = "0.0.0.0";

        try {
            InetAddress ip = InetAddress.getLocalHost();
            ipAddr = ip.getHostAddress();
        } catch (Exception var2) {
            log.warn("获取ip错误");
        }

        return ipAddr;
    }

    public static long getMaxWorkerId(long datacenterId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String mac = getMac();
        mpid.append(StringUtils.isEmpty(mac) ? UUIDUtil.longUuid() : mac);
        mpid.append(UUIDUtil.longUuid());
        return (long)(mpid.toString().hashCode() & '\uffff') % (maxWorkerId + 1L);
    }

    public static String getMac() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                return "";
            }

            byte[] mac = network.getHardwareAddress();
            if (null != mac) {
                StringJoiner j = new StringJoiner("-");

                for(int i = 0; i < mac.length; ++i) {
                    j.add(String.format("%02X", mac[i]));
                }

                return j.toString();
            }
        } catch (Exception var5) {
            log.warn(" get Mac: " + var5.getMessage());
        }

        return "";
    }

    public GlobalId(long workerId, long datacenterId) {
        this.workerIdShift = this.sequenceBits;
        this.sequenceMask = ~(-1L << (int)this.sequenceBits);
        this.sequence = 0L;
        this.lastTimestamp = -1L;
        this.build();
        if (workerId <= maxWorkerId && workerId >= 0L) {
            if (datacenterId <= maxDatacenterId && datacenterId >= 0L) {
                this.workerId = workerId;
                this.datacenterId = datacenterId;
            } else {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
    }

    private void build() {
        this.datacenterIdShift = this.sequenceBits + workerIdBits;
        this.timestampLeftShift = this.sequenceBits + workerIdBits + datacenterIdBits;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < this.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastTimestamp - timestamp));
        } else {
            if (this.lastTimestamp == timestamp) {
                this.sequence = this.sequence + 1L & this.sequenceMask;
                if (this.sequence == 0L) {
                    timestamp = this.tilNextMillis(this.lastTimestamp);
                }
            } else {
                this.sequence = 0L;
            }

            this.lastTimestamp = timestamp;
            return timestamp - this.twepoch << (int)this.timestampLeftShift | this.datacenterId << (int)this.datacenterIdShift | this.workerId << (int)this.workerIdShift | this.sequence;
        }
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for(timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public static GlobalId initialDefault(long datacenterId, long workerId) {
        defaultGlobalId = new GlobalId(workerId, datacenterId);
        return defaultGlobalId;
    }

    public static long generateNextId() {
        return defaultGlobalId.nextId();
    }

    static {
        maxWorkerId = ~(-1L << (int)workerIdBits);
        maxDatacenterId = ~(-1L << (int)datacenterIdBits);
        defaultGlobalId = new GlobalId(getMaxWorkerId(getDatacenterIdByIp()), getDatacenterIdByIp());
    }

}
