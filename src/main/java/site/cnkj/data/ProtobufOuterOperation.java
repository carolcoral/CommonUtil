package site.cnkj.data;

import com.google.protobuf.ByteString;
import com.googlecode.protobuf.format.JsonFormat;

import java.util.HashMap;
import java.util.Map;

/*
 * @author  LXW
 * @create  2019/12/27 11:04
 * @Description
 *          1. body 是.proto文件中设置的对象属性，因为设置的是字节格式，所以这里所有对body对象数据的操作都应当为字节格式
 *          2. headers 是.proto文件中设置的对象属性，因为是map格式，所以这里面对headers对象的操作应当全部为map格式
 *          3. 并且因为pb数据的特性，map对象是不可变的，因为当使用getMap.put()方法的时候会全部抛异常
 */
public class ProtobufOuterOperation {

    /**
     * 生成pb数据
     * @param body 主题
     * @param headers 头
     * @return pb数据的字节数组
     */
    private byte[] create(byte[] body, Map headers){
        try {
            ProtobufOuter.ProtobufEntity.Builder protobufEntity = ProtobufOuter.ProtobufEntity.newBuilder();
            protobufEntity.setBody(ByteString.copyFrom(body));
            protobufEntity.putAllHeaders(headers);
            return protobufEntity.build().toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取pb数据
     * @param pb pb数据字节数组
     * @return json字符串
     */
    private String get(byte[] pb){
        try {
            ProtobufOuter.ProtobufEntity protobufEntity = ProtobufOuter.ProtobufEntity.parseFrom(pb);
            //获取指定body内容
            ByteString body = protobufEntity.getBody();
            //获取指定headers内容
            Map<String, String> headersMap = protobufEntity.getHeadersMap();
            //字节转换pb数据对象为jsonString
            String string = JsonFormat.printToString(protobufEntity);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 编辑headers，因为pb数据特性，获取到的map对象是不可变的，所以我们只能重新new一个对象然后把数据放进去
     *  - 当新的map对象的属性与原来的属性有重复，则会覆盖原来的数据
     *  - 否则效果等于新增
     * - 新增body同理
     * @param pb 获取到的pb数据
     * @param headers 需要新增的headers对象
     * @return 完成新增headers后的pb数据字节
     */
    private byte[] editHeaders(byte[] pb, Map headers){
        ProtobufOuter.ProtobufEntity.Builder builder = ProtobufOuter.ProtobufEntity.newBuilder();
        try {
            ProtobufOuter.ProtobufEntity protobufEntity = ProtobufOuter.ProtobufEntity.parseFrom(pb);
            builder.setBody(protobufEntity.getBody());
            Map map = new HashMap();
            map.putAll(protobufEntity.getHeadersMap());
            map.putAll(headers);
            builder.putAllHeaders(map);
            return builder.build().toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
