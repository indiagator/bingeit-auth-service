package com.secor.bingeitauthservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secor.bingeitauthservice.Analytic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer
{
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "auth-events";

    @Autowired //DEPENDENCY INJECTION PROMISE FULFILLED AT RUNTIME
    private KafkaTemplate<String, String> kafkaTemplate ;

//    @Autowired
//    SocialEvent1 socialEvent1;

    public void publishAuthDatum(String username, String description) throws JsonProcessingException // LOGIN | REGISTER
    {

        Analytic authDatum = new Analytic();
        authDatum.setPrincipal(username);
        authDatum.setType("AUTH");
        authDatum.setDescription(description);
        logger.info("created the AuthDatum object: "+authDatum);
//
        // convert to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String datum =  objectMapper.writeValueAsString(authDatum);
        logger.info("converted the AuthDatum object to JSON: "+datum);
//
        logger.info(String.format("#### -> Producing message -> %s", datum));
        this.kafkaTemplate.send(TOPIC, datum);
    }

}
