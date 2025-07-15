package aggregation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableEurekaClient
@EnableKafkaStreams
@EnableAsync
public class DataAggregationApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataAggregationApplication.class, args);
	}

}
