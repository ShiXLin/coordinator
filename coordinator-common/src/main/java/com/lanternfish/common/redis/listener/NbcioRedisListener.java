package com.lanternfish.common.redis.listener;

import com.lanternfish.common.base.BaseMap;


/**
 *  自定义消息监听
 *  @author Liam
 *  @date 2023-09-20
 */

public interface NbcioRedisListener {
	void onMessage(BaseMap message);
}
