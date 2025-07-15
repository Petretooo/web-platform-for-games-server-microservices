//package dotsandboxes.model.neo4j;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.springframework.data.neo4j.core.schema.GeneratedValue;
//import org.springframework.data.neo4j.core.schema.Id;
//import org.springframework.data.neo4j.core.schema.Node;
//import org.springframework.data.neo4j.core.support.UUIDStringGenerator;
//
//
//@Node
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class GraphDot {
//
//    @Id
//    @GeneratedValue(UUIDStringGenerator.class)
//    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @JsonProperty
//    private String dotId;
//
//    @JsonProperty
//    private String xAxis;
//
//    @JsonProperty
//    private String yAxis;
//}
