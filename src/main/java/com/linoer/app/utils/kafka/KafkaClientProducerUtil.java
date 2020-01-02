package com.linoer.app.utils.kafka;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaClientProducerUtil {

    public static void produce(String topic, String servers) throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 10);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 1; i++) {
            JSONObject jsonObject = new JSONObject();
            String id = UUID.randomUUID().toString();
            jsonObject.put("user_id",id);
            Future future = producer.send(new ProducerRecord<>(topic, "sender_test", jsonObject.toJSONString()));
            System.out.println(future.get().toString());
        }
        producer.close();
    }
}
