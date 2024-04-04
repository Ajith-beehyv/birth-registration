package digit.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Consumer {

    /*
     * Uncomment the below line to start consuming record from kafka.topics.consumer
     * Value of the variable kafka.topics.consumer should be overwritten in application.properties
     */
    //@KafkaListener(topics = {"kafka.topics.consumer"})
    public void listen(final HashMap<String, Object> record) {

        //TODO

    }

}
