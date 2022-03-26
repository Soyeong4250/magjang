package B208.mag_jang;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Stomp를 사용하기위해 선언하는 어노테이션
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 또는 SockJS Client가 웹소켓 핸드셰이크 커넥션을 생성할 경로 지정
        registry.addEndpoint("/stomp/chat").setAllowedOriginPatterns("*").withSockJS();
//        registry.addEndpoint("/").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/pub" 경로로 시작하는 STOMP 메세지의 destination 헤더는
        // @Controller 객체의 @MessageMapping 메서드로 라우팅

        registry.setApplicationDestinationPrefixes("/pub", "/game");


        // 내장된 메세지 브로커를 사용해 Client에게 Subscriptions, Broadcasting 기능을 제공
        // "/sub", "/queue"로 시작하는 destination 헤더를 가진 메세지를 브로커로 라우팅
        registry.enableSimpleBroker("/sub");
    }
}
