package com.example.kafka_spring_cloud_stream.events;

import java.util.Date;

public record PageEvent (String name, String user , Date date ,long duration) {
}
