package site.cnkj.utils;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/*
 * @version 1.0 created by LXW on 2019/11/20 10:20
 */
public class KafkaUtil {


    /**
     * 获取当前topic下的全部分区的偏移量信息
     *
     * @param properties 配置信息
     * @param partitions Collection<TopicPartition> partitions
     * @return {partition:offset}
     */
    public static Map<String, Long> getConsumerPartitionsOffset(Map<String, Object> properties, Collection<TopicPartition> partitions) {
        KafkaConsumer consumer = new KafkaConsumer(properties);
        Map<String, Long> partitionsOffset = new HashMap<>();
        try {
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);
            for (TopicPartition topicPartition : endOffsets.keySet()) {
                partitionsOffset.put(topicPartition.toString(), endOffsets.get(topicPartition));
            }
            return partitionsOffset;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            consumer.close();
        }
    }


    /**
     * 获取当前服务消费的topic的每个分区的当前偏移量
     *
     * @param properties 配置信息
     * @param topics Collection<String> topics
     * @return {
     *          topic:
     *           {
     *             partitionInfo:offset
     *           }
     *         }
     */
    public static Map<String, Map<String, Long>> getConsumerTopicPartitionsOffset(Map<String, Object> properties, Set<String> topics){
        Map<String, Map<String, Long>> topicPartitionMap = new HashMap<>();
        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        try {
            for (String topic : topics) {
                List<PartitionInfo> partitionsInfo = kafkaConsumer.partitionsFor(topic);
                Set<TopicPartition> topicPartitions = new HashSet<>();
                for (PartitionInfo partitionInfo : partitionsInfo) {
                    TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                    topicPartitions.add(topicPartition);
                }
                Map<String, Long> topicPartitionsOffset = getConsumerPartitionsOffset(properties, topicPartitions);
                topicPartitionMap.put(topic, topicPartitionsOffset);
            }
            return topicPartitionMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            kafkaConsumer.close();
        }
    }

}
