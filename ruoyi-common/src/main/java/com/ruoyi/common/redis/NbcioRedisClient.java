package com.ruoyi.common.redis;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.base.BaseMap;
import com.ruoyi.common.constant.GlobalConstants;

/**
 * redis客户端
 * 
 * @author nbacheng
 * @date 2023-09-20
 */
@Configuration
public class NbcioRedisClient {

	@Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 发送消息
     *
     * @param handlerName
     * @param params
     * @throws JsonProcessingException 
     */
    public void sendMessage(String handlerName, BaseMap params) throws JsonProcessingException {
        params.put(GlobalConstants.HANDLER_NAME, handlerName);
        ObjectMapper objectMapper = new ObjectMapper();
        // 将HashMap转换为JSON字符串
        String json = objectMapper.writeValueAsString(params);
        redisTemplate.convertAndSend(GlobalConstants.REDIS_TOPIC_NAME, json);
    }

}
