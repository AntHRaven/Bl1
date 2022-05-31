package com.example.bl_lab1.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Arrays;
import java.util.List;

@Configuration
@IntegrationComponentScan
public class MqttSenderConfig {
    @Value("${spring.mqtt.username}")
    private String username;
    
    @Value("${spring.mqtt.password}")
    private String password;
    
    @Value("${spring.mqtt.url}")
    private String hostUrl;
    
    @Value("${spring.mqtt.client.out.id}")
    private String clientOutId;
    
    @Value("${spring.mqtt.client.in.id}")
    private String clientInId;
    
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;
    
    //@Value("#{'${spring.mqtt.topics}'.split(',')}")
    private List<String> topics;
    
   // @Value("#{'${spring.mqtt.qosValues}'.split(',')}")
    private List<Integer> qosValues;
    
    // client factory
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setCleanSession(true);
//        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        // Устанавливаем время ожидания в секундах
        mqttConnectOptions.setConnectionTimeout(10);
        mqttConnectOptions.setMaxInflight(100000000);
        
        return mqttConnectOptions;
        
    }
    
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }
    
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientOutId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        messageHandler.setDefaultRetained(false);
        messageHandler.setDefaultQos(1);
        return messageHandler;
    }
    
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    
    
//    @Bean
//    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
//        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientInId,
//                                                                                              mqttClientFactory());
//        adapter.setCompletionTimeout(10 * 1000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(1);
//        adapter.addTopic(defaultTopic);
//       return adapter;
    //}
}
