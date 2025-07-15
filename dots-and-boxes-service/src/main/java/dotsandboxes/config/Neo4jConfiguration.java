//package dotsandboxes.config;
//
//import org.neo4j.driver.AuthTokens;
//import org.neo4j.driver.Driver;
//import org.neo4j.driver.GraphDatabase;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class Neo4jConfiguration {
//
//    @Value("${spring.datasource.neo4j.url}")
//    private String url;
//
//    @Value("${spring.datasource.neo4j.username}")
//    private String username;
//
//    @Value("${spring.datasource.neo4j.password}")
//    private String password;
//
//    @Bean
//    public Driver neo4jDriver() {
//        return GraphDatabase.driver(url, AuthTokens.basic(username, password));
//    }
//
////    @Bean
////    public void dataSource() {
////        try (var driver = GraphDatabase.driver(url, AuthTokens.basic(username, password))) {
////            driver.verifyConnectivity();
////            System.out.println("Connection established.");
////        }
////    }
//}
