package org.skillsmapper.factservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
public class FactChangedNotifierPubSub implements FactChangedNotifier {

    private static final Logger logger = LoggerFactory.getLogger(FactChangedNotifierPubSub.class);

    private final FactRepository factRepository;

    private final ObjectMapper objectMapper;

    private final String pubsubTopic;

    private final PubSubTemplate pubsubTemplate;

    public FactChangedNotifierPubSub(FactRepository factRepository, @Value("${pubsub.topic.factchanged}") String pubsubTopic, PubSubTemplate pubsubTemplate) {
        this.factRepository = factRepository;
        this.pubsubTopic = pubsubTopic;
        this.pubsubTemplate = pubsubTemplate;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE);
    }

    public void factsChanged(Fact fact) {
        List<Fact> facts = factRepository.findByUser(fact.getUser());
        FactsChanged factsChanged = new FactsChanged(fact.getUser(), facts, OffsetDateTime.now());
        try {
            String jsonString = objectMapper.writeValueAsString(factsChanged);
            logger.info("Sending message to Pub/Sub: {}", jsonString);
            pubsubTemplate.publish(pubsubTopic, jsonString);
        } catch (JsonProcessingException e) {
            logger.error("Error serialising message send to Pub/Sub: {}", e.getMessage());
        }
    }
}
