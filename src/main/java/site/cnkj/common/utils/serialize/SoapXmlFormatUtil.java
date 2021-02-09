package site.cnkj.common.utils.serialize;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * @author  LXW
 * @create  2020/5/26 14:38
 * @Description
 *  解析body内所有层级及层级内的内容，目前最多支持到第三层
 */
public class SoapXmlFormatUtil {

    public static JSONObject parse(String soapXml) throws Exception{
        JSONObject object = new JSONObject();
        JSONObject firstElement = getFirstElement(soapXml);
        for (String s : firstElement.keySet()) {
            JSONObject jsonObject = new JSONObject();
            JSONObject firstElementJSONObject = firstElement.getJSONObject(s);
            if (firstElementJSONObject != null && firstElementJSONObject.size() > 0){
                for (String s1 : firstElementJSONObject.keySet()) {
                    Object s2 = firstElementJSONObject.get(s1);
                    if (s2 instanceof List){
                        List list = new ArrayList();
                        List<String> stringList = (List<String>) s2;
                        for (String o : stringList) {
                            if (o.contains("#")){
                                list.add(traverseNode(firstElement, o));
                            }else {
                                list.add(o);
                            }
                        }
                        jsonObject.put(s1, list);
                    }else {
                        String key = s2.toString();
                        if (key.contains("#")){
                            jsonObject.put(s1, traverseNode(firstElement, key));
                        }else {
                            jsonObject.put(s1, key);
                        }
                    }
                }
            }
            object.put(s, jsonObject);
        }
        return object;
    }

    private static JSONObject traverseNode(JSONObject rootElement, String key) throws Exception{
        JSONObject jsonObject = rootElement.getJSONObject(key.replaceAll("#", ""));
        for (String s2 : jsonObject.keySet()) {
            Object o1 = jsonObject.get(s2);
            if (o1 instanceof List){
                List<String> stringList1 = (List<String>) o1;
                List list1 = new ArrayList();
                for (String s3 : stringList1) {
                    if (s3.contains("#")){
                        JSONObject jsonObject1 = rootElement.getJSONObject(s3.replaceAll("#", ""));
                        list1.add(jsonObject1);
                    }else {
                        list1.add(s3);
                    }
                }
                jsonObject.put(s2, list1);
            }else {
                String key3 = o1.toString();
                if (key3.contains("#")){
                    JSONObject jsonObject1 = rootElement.getJSONObject(key3.replaceAll("#", ""));
                    jsonObject.put(s2, jsonObject1);
                }else {
                    jsonObject.put(s2, key3);
                }
            }
        }
        return jsonObject;
    }

    private static JSONObject getFirstElement(String soapXml) throws Exception{
        JSONObject jsonObject = new JSONObject();
        Document document = DocumentHelper.parseText(soapXml);
        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element next = (Element) iterator.next();
            Iterator elementIterator = next.elementIterator();
            while (elementIterator.hasNext()){
                Element next1 = (Element) elementIterator.next();
                String name = next1.getName();

                //if (!"multiRef".equals(name)){
                //    Iterator iterator1 = next1.elementIterator();
                //    while (iterator1.hasNext()){
                //        Element next2 = (Element) iterator1.next();
                //        JSONObject multiRefElement = getMultiRefElement(next2);
                //        jsonObject.put(name, multiRefElement);
                //    }
                //}else {
                //    String id = next1.attribute("id").getValue();
                //    JSONObject multiRefElement = getMultiRefElement(next1);
                //    jsonObject.put(id, multiRefElement);
                //}

                String value = "";
                Attribute attribute = next1.attribute("id");
                if (attribute == null){
                    value = name;
                }else {
                    value = attribute.getValue();
                }
                JSONObject multiRefElement = getMultiRefElement(next1);
                if (multiRefElement.size() == 0){
                    Iterator iterator1 = next1.elementIterator();
                    while (iterator1.hasNext()){
                        Element next2 = (Element) iterator1.next();
                        JSONObject multiRefElement1 = getMultiRefElement(next2);
                        jsonObject.put(value, multiRefElement1);
                    }
                }else {
                    jsonObject.put(value, multiRefElement);
                }

            }
        }
        return jsonObject;
    }

    private static JSONObject getMultiRefElement(Element element) throws Exception{
        JSONObject jsonObject = new JSONObject();
        Iterator iterator1 = element.elementIterator();
        while (iterator1.hasNext()){
            Element next = (Element) iterator1.next();
            String text1 = next.getText();
            String name1 = next.getName();
            Attribute attribute = next.attribute("type");
            Attribute attributeHref = next.attribute("href");
            if (attribute != null){
                String attributeValue = attribute.getValue();
                if (attributeValue.toLowerCase().contains("array")){
                    List list = new ArrayList();
                    Iterator iterator2 = next.elementIterator();
                    while (iterator2.hasNext()){
                        Element next2 = (Element) iterator2.next();
                        Attribute attribute1 = next2.attribute("href");
                        if (attribute1 != null){
                            list.add(attribute1.getValue());
                        }else {
                            list.add(next2.getText());
                        }
                    }
                    jsonObject.put(name1, list);
                }else if (attributeValue.toLowerCase().contains("string")){
                    jsonObject.put(name1, text1);
                }
            }else if (attributeHref != null){
                String attributeValueHref = attributeHref.getValue();
                jsonObject.put(name1, attributeValueHref);
            }
        }
        return jsonObject;
    }

}
