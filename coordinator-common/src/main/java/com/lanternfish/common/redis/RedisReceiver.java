package com.lanternfish.common.redis;


import org.springframework.stereotype.Component;

import com.lanternfish.common.base.BaseMap;
import com.lanternfish.common.constant.GlobalConstants;
import com.lanternfish.common.redis.listener.NbcioRedisListener;
import com.lanternfish.common.utils.SpringContextHolder;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 *  接受消息并调用业务逻辑处理器
 *  @author Liam
 *  @date 2023-09-20
 */
@Component
@Data
public class RedisReceiver {

    public void onMessage(BaseMap params) {
        Object handlerName = params.get(GlobalConstants.HANDLER_NAME);
        NbcioRedisListener messageListener = SpringContextHolder.getHandler(handlerName.toString(), NbcioRedisListener.class);
        if (ObjectUtil.isNotEmpty(messageListener)) {
            messageListener.onMessage(params);
        }
    }

}
