package com.example.kafka_spring_cloud_stream.handlers;

import com.example.kafka_spring_cloud_stream.events.PageEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.BridgeFrom;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class PageEventHandler {
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input)->{
            System.out.println("##############");
            System.out.println(input.toString());

        };
    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return ()->{
            return new PageEvent(
                    Math.random()>0.5 ? "P1":"P2",
                    Math.random()>0.5 ?"U1":"U2",
                    new Date(),
                   10 + new Random().nextInt(10000)
            );
        };
    }

    @Bean
    public Function<KStream<String, PageEvent>, KStream<String, Long>> kStreamFunction(){
        return (stream)->
                stream
                        //.filter((k,v)->v.duration()>100)
                        .map((k,v)->new KeyValue<>(v.name(), 0L))
                        .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                        .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
                        //.aggregate(()->0.0, (k,v,total)->total+v, Materialized.as("total-store"))
                        .count(Materialized.as("count-store"))
                        .toStream()
                        .map((k,v)->new KeyValue<>(k.key(), v))
                ;

    }




}
