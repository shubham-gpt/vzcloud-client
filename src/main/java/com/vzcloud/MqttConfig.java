package com.vzcloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mqtt")
public class MqttConfig {

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    private String brokerUrl;

    private String minTaskTopic;

    public String getMinTaskTopic() {
        return minTaskTopic;
    }

    public void setMinTaskTopic(String minTaskTopic) {
        this.minTaskTopic = minTaskTopic;
    }

    public String getAgentInfoTopic() {
        return agentInfoTopic;
    }

    public void setAgentInfoTopic(String agentInfoTopic) {
        this.agentInfoTopic = agentInfoTopic;
    }

    private String agentInfoTopic;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String username;
    private String password;

}
