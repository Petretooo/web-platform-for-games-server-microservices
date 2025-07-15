//package dotsandboxes.model.neo4j;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import dotsandboxes.model.Box;
//import dotsandboxes.model.Dot;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
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
//public class GraphEdge {
//
//    @Id
//    @GeneratedValue(UUIDStringGenerator.class)
//    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @JsonProperty
//    String edgeId;
//
//    @JsonProperty
//    private String gameId;
//
////    @ManyToOne
////    @JoinColumn(name="box_id")
//    private Box box;
//
////    @ManyToOne
////    @JoinColumn(name="from_dot_id")
//    private Dot fromDot;
//
////    @ManyToOne
////    @JoinColumn(name="to_dot_id")
//    private Dot toDot;
//
//    @JsonProperty
//    private boolean isEdgeAvailable;
//
//    @JsonProperty
//    private String userId;
//
//    @JsonProperty
//    private int orderEdge;
//}
