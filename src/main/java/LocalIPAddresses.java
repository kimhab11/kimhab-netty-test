import lombok.CustomLog;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class LocalIPAddresses {
    public static void main(String[] args) throws SocketException {
//        try {
//            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
//            while (networkInterfaces.hasMoreElements()) {
//                NetworkInterface networkInterface = networkInterfaces.nextElement();
//                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
//                while (inetAddresses.hasMoreElements()) {
//                    InetAddress inetAddress = inetAddresses.nextElement();
//                    System.out.println("Local IP Address: " + inetAddress.getHostAddress());
//                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
//                        System.out.println("Local IP Address 1: " + inetAddress.getHostAddress());
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }

        List<String> hosts =new ArrayList<>();
        Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                continue;
            } else if (netInterface.getDisplayName().contains("VMware") || netInterface.getDisplayName().contains("Virtual")) {
                continue;
            }
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            System.out.println("IP: "+netInterface.getDisplayName());
            while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip instanceof Inet4Address) {
                    hosts.add(ip.getHostAddress());
                }
            }
        }
        System.out.println("Server hosts: {}" +hosts);
        if (hosts.isEmpty()) {
            throw new RuntimeException("get locale ip error");
        }
        System.out.println("K"+hosts);
//        return hosts.get(0);
    }
}
