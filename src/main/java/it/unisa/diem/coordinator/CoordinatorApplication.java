package it.unisa.diem.coordinator;

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoordinatorApplication {
	
	
	protected static MQTTClient client;
	public static final String CONFIGURATION_FILE = "src/main/resources/application.properties";
	protected static String clientID, username, password, serverURI, broadcastTopic;
	protected static int connectionTimeout;
	
	public static void main(String[] args) throws Exception {
		PropertiesConfiguration prop = new PropertiesConfiguration(CONFIGURATION_FILE);
		
		serverURI = prop.getString("coordinator.serverURI");
		username = prop.getString("coordinator.username");
		password = prop.getString("coordinator.password");
		connectionTimeout = prop.getInt("coordinator.connectionTimeout");
		broadcastTopic = prop.getString("coordinator.broadcastTopic");
		
		clientID = UUID.randomUUID().toString();
		client = new MQTTClient(serverURI, clientID, broadcastTopic);
		
		SpringApplication.run(CoordinatorApplication.class, args);
	}

	@PreDestroy
	public void destroy() throws Exception {
		client.disconnect();
	}
	
}
