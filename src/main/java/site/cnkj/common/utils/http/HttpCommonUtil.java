package site.cnkj.common.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/*
 * @version 1.0 created by LXW on 2018/10/25 10:04
 */
public class HttpCommonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpCommonUtil.class);

    public static String hostName = getHostName();

    /**
     * 获取客户端请求的地址
     *
     * @param request 客户端请求信息
     * @return 客户端地址
     */
    public static String getCustomsAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if(ip.equals("127.0.0.1")){
                //根据网卡取本机配置的IP
                InetAddress interNet=null;
                try {
                    interNet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (interNet != null){
                    ip= interNet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ip != null && ip.length() > 15){
            if(ip.indexOf(",")>0){
                ip = ip.substring(0,ip.indexOf(","));
            }
        }
        return ip;
    }

    //获取本地ip
    public static String getLocalIp(){
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本地机器名
    public static String getLocalHostName(){
        return hostName;
    }

    private static String getHostName(){
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
            return address.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前服务的PID
     * @return PID
     */
    public static Integer getCurrentPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return Integer.valueOf(pid);
    }

    /**
     * 获取当前服务的PID和名字
     * @return PID@NAME
     */
    public static String getCurrentPidAndName(){
        // get name representing the running Java virtual machine.
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    /**
     * 获取本地ip地址，不含 127.0.0.1
     * @return
     */
    public static String getHostIp(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")
                            && !ip.toString().startsWith("2.")){
                        LOGGER.warn("本机的IP = " + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
