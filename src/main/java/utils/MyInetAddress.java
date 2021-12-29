package utils;

import java.net.InetAddress;

/**
 * @ClassName MyInetAddress
 * @Description 获取本机的IP
 * @Author lkj
 * @Date 2020/12/28
 */
public class MyInetAddress {

    /**
     * <strong>Title : getLocalHost</strong><br/>
     * <strong>Description : 获取本机IP </strong><br/>
     * <strong>Create on : 2020/12/28 上午10:44</strong><br/>
     *
     * @return java.lang.String
     * @author kejin.liu
     * @version <strong>v1.0</strong><br/>
     * <p>
     * <strong>修改历史:</strong><br/>
     * 修改人 | 修改日期 | 修改描述<br/>
     * -------------------------------------------<br/>
     * </p>
     */
    public static String getLocalHost() {
        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();
            String hostAddress = ia.getHostAddress();
            return hostAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return "192.168.0.-1";
        }
    }
}
