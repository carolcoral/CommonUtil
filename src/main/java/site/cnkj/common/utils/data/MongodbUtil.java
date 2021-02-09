package site.cnkj.common.utils.data;

import com.mongodb.client.ClientSession;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.List;

/*
 * @version 1.0 created by LXW on 2019/11/22 17:21
 */
public class MongodbUtil {

    private MongoTemplate mongoTemplate;

    private final String mongoName;

    public MongodbUtil(MongoTemplate mongoTemplate, String mongoName){
        this.mongoTemplate = mongoTemplate;
        this.mongoName = mongoName;
    }

    public Class save(Class document){
        try {
            String collectionName = mongoTemplate.getCollectionName(document);
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            return mongoTemplate.save(document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class save(Class c, String collectionName){
        try {
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            return mongoTemplate.save(c, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> Collection<T> insert(Collection<? extends T> batchToSave, String collectionName){
        try {
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            return mongoTemplate.insert(batchToSave, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定名称下执行值的数据
     * @param key 名字
     * @param value 值
     * @param document 文档对象
     * @return 成功删除的文档数量
     */
    public <T> Long removeByQuery(String key, String value, Class<T> document){
        try {
            Query query = Query.query(Criteria.where(key).is(value));
            return mongoTemplate.remove(query, document).getDeletedCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除指定名称下执行值的数据
     * @param key 名字
     * @param value 值
     * @param collectionName 集合名称
     * @return 成功删除的文档数量
     */
    public Long removeByQuery(String key, String value, String collectionName){
        try {
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(key).is(value));
            return mongoTemplate.remove(query, collectionName).getDeletedCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过对象删除文档
     * @param document 对象
     */
    public <T> void dropCollection(Class<T> document){
        try {
            mongoTemplate.dropCollection(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过集合名删除文档
     * @param collectionName 集合名
     */
    public void dropCollection(String collectionName){
        try {
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            mongoTemplate.dropCollection(collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除当前数据库
     */
    public void dropDataBase(){
        try {
            mongoTemplate.getDb().drop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除当前数据库
     */
    public void dropDataBase(ClientSession clientSession){
        try {
            mongoTemplate.getDb().drop(clientSession);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T findAndRemoveByQuery(String key, String value, Class<T> document){
        return findAndRemoveByQuery(key, value, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 查询出符合条件的第一个结果，并将符合条件的数据删除,只会删除第一条
     * @param key 索引
     * @param value 值
     * @param document 文档
     * @param collectionName 集合名
     * @return 返回删除的记录
     */
    public <T> T findAndRemoveByQuery(String key, String value, Class<T> document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(key).is(value));
            return mongoTemplate.findAndRemove(query, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> findAllAndRemoveByQuery(String key, String value, Class<T> document){
        return findAllAndRemoveByQuery(key, value, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 查询出符合条件的所有结果，并将符合条件的所有数据删除
     * @param key 索引
     * @param value 值
     * @param document 文档
     * @param collectionName 集合名
     * @return 返回删除的记录
     */
    public <T> List<T> findAllAndRemoveByQuery(String key, String value, Class<T> document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(key).is(value));
            return mongoTemplate.findAllAndRemove(query, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UpdateResult updateFirstByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document){
        return updateFirstByQuery(findKey, findValue, updateKey, updateValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 修改查询到的结果中的第一条记录
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult updateFirstByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue);
            return mongoTemplate.updateFirst(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public UpdateResult updateAllByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document){
        return updateAllByQuery(findKey, findValue, updateKey, updateValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 更新查询到的全部结果
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult updateAllByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UpdateResult updateOrInsertByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document){
        return updateOrInsertByQuery(findKey, findValue, updateKey, updateValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 特殊更新，更新数据，如果没有数据则以此条件创建一条新的数据
     * 当没有符合条件的文档，就以这个条件和更新文档为基础创建一个新的文档，如果找到匹配的文档就正常的更新
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult updateOrInsertByQuery(String findKey, String findValue, String updateKey, String updateValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue);
            return mongoTemplate.upsert(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UpdateResult setByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Object setValue, Class document){
        return setByQuery(findKey, findValue, updateKey, updateValue, setKey, setValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 更新指定内容的数据，如果更新的setKey不存在则创建一个新的key
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param setKey 设置key
     * @param setValue 设置value
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult setByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Object setValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue).set(setKey, setValue);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UpdateResult incByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Number setValue, Class document){
        return incByQuery(findKey, findValue, updateKey, updateValue, setKey, setValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 累加单条数据中的指定setKey的值
     * inc方法用于做累加操作，将setKey的值在之前的基础上加上setValue
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param setKey 设置key
     * @param setValue 增长的值
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult incByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Number setValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue).inc(setKey, setValue);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public UpdateResult renameByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, String setValue, Class document){
        return renameByQuery(findKey, findValue, updateKey, updateValue, setKey, setValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 修改单条数据中的setKey的名称
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param setKey 修改前的名字
     * @param setValue 修改后的名字
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult renameByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, String setValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue).rename(setKey, setValue);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public UpdateResult unsetByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Class document){
        return unsetByQuery(findKey, findValue, updateKey, updateValue, setKey, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 删除指定内容数据中的key
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param setKey 需要删除的key
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult unsetByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue).unset(setKey);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public UpdateResult pullByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, String setValue, Class document){
        return pullByQuery(findKey, findValue, updateKey, updateValue, setKey, setValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * update的pull方法用于删除数组中的值
     *
     * @param findKey 查询key
     * @param findValue 查询value
     * @param updateKey 更新key
     * @param updateValue 更新value
     * @param setKey 数组名
     * @param setValue 删除的值
     * @param document 文档对象
     * @param collectionName 集合名称
     * @return 更新结果
     */
    public UpdateResult pullByQuery(String findKey, String findValue, String updateKey, String updateValue, String setKey, String setValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            Update update = Update.update(updateKey, updateValue).pull(setKey, setValue);
            return mongoTemplate.updateMulti(query, update, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> findByQuery(String findKey, String findValue, Class<T> document){
        return findByQuery(findKey, findValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 查询符合条件的数据集合
     *
     * @param findKey
     * @param findValue
     * @param document
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> List<T> findByQuery(String findKey, String findValue, Class<T> document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            return mongoTemplate.find(query, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T findOneByQuery(String findKey, String findValue, Class<T> document){
        return findOneByQuery(findKey, findValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 查询符合条件的一条数据
     *
     * @param findKey
     * @param findValue
     * @param document
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> T findOneByQuery(String findKey, String findValue, Class<T> document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            return mongoTemplate.findOne(query, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> findAll(Class<T> document){
        return findAll(document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 查询符合条件的一条数据
     *
     * @param document
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            return mongoTemplate.findAll(document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long countByQuery(String findKey, String findValue, Class document){
        return countByQuery(findKey, findValue, document, mongoTemplate.getCollectionName(document));
    }

    /**
     * 统计符合查询条件的数据数量
     *
     * @param findKey
     * @param findValue
     * @param document
     * @param collectionName
     * @return
     */
    public Long countByQuery(String findKey, String findValue, Class document, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            return mongoTemplate.count(query, document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long countByQuery(String findKey, String findValue, String collectionName){
        try {
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            Query query = Query.query(Criteria.where(findKey).is(findValue));
            return mongoTemplate.count(query, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T findById(Class<T> document, String id){
        return findById(document, id, mongoTemplate.getCollectionName(document));
    }

    /**
     * 通过主键id进行查询
     *
     * @param document
     * @param id
     * @param collectionName
     * @param <T>
     * @return
     */
    public <T> T findById(Class<T> document, String id, String collectionName){
        try {
            if (StringUtils.isEmpty(collectionName)){
                collectionName = mongoTemplate.getCollectionName(document);
            }
            if (StringUtils.isNotEmpty(mongoName)){
                collectionName = mongoName + ":" + collectionName;
            }
            return mongoTemplate.findById(new ObjectId(id), document, collectionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}
