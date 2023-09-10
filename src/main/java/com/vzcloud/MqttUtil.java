package com.vzcloud;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class MqttUtil {
    @Autowired
    private MqttConfig mqttConfig;
    @Autowired
    private CommonConfig commonConfig;

    public void publish(String topic, JSONObject jsonObject) {
        if (null != jsonObject) {
            jsonObject.put("wgToken", MD5Utils.GetMD5Code(commonConfig.getWgToken()));
        }
        try {
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(mqttConfig.getUsername());
            if (!StringUtils.isEmpty(mqttConfig.getPassword())) {
                connectOptions.setPassword(mqttConfig.getPassword().toCharArray());
            }
            String clientId = UUID.randomUUID().toString();
            MqttClient client = new MqttClient(mqttConfig.getBrokerUrl(), clientId, new MemoryPersistence());
            client.connect(connectOptions);
            MqttMessage message = new MqttMessage(JSONUtil.toJsonStr(jsonObject).getBytes());
            message.setQos(0);
            client.publish(topic, message);
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
