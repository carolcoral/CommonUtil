package site.cnkj.common.utils.io;

import site.cnkj.common.utils.logger.LoggerUtil;

import java.io.*;

/**
 * @version 1.0 created by duanhao on 2019/4/23 10:35
 */
public class CopyUtil {

    public static Object copy(Object orig) throws IOException {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LoggerUtil.error("复制失败IO", e);
        }
        return obj;
    }

}

