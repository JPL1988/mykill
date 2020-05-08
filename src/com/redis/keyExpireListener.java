package com.redis;

import com.service.killService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

/**
 * @author false
 * @date 20/4/23 13:04
 */
public class keyExpireListener extends KeyExpirationEventMessageListener {
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private com.service.killService killService;

    public keyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        long key = Long.valueOf(new String(message.getBody(), StandardCharsets.UTF_8));
        logger.debug("long key :"+key);
        killService.dealTimeoutOrder(key);
    }
}
