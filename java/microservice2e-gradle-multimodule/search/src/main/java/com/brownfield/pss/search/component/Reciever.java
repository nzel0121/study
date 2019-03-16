package com.brownfield.pss.search.component;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Reciever {

    @Autowired
    private SearchComponent searchComponent;

    Queue queue(){
        return new Queue("SearchQ",false);
    }

    @RabbitListener(queues = "SearchQ")
    public void process(Map<String,Object> fare){
        searchComponent.updateInventory((String)fare.get("FLIGHT_NUMBER"),(String)fare.get("FLIGHT_DATE"),(int)fare.get("NEW_INVENTORY"));
    }
}
