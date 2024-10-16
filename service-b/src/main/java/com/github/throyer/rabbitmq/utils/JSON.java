package com.github.throyer.rabbitmq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {

    private JSON() { }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    public static <T> String stringify(final T object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            return "";
        }
    }
    
    public static <T> T parse(String json, Class<T> type) {
      try {
        return new ObjectMapper().readValue(json, type);  
      } catch (Exception exception) {
       throw  new RuntimeException(exception.getMessage(), exception); 
      }      
    }
}