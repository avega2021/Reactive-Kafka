package com.angelvb.kafka_listener.config;

import com.angelvb.kafka_listener.client.DemoClient;
import com.angelvb.kafka_listener.property.NotificationServiceProperty;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@AllArgsConstructor
public class WebClientConfig {

  private NotificationServiceProperty notificationServiceProperty;

  @Bean
  public DemoClient demoWebClient() {
    WebClient webClient = webClientBuilder().baseUrl(notificationServiceProperty.getUrl()).build();
    return newHttpServiceProxy(webClient, DemoClient.class);
  }

  @Bean
  public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
  }

  private static <T> T newHttpServiceProxy(WebClient webClient, Class<T> serviceType) {
    return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build().createClient(serviceType);
  }

}
