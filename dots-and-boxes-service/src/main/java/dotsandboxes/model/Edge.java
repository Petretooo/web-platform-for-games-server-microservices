//package dotsandboxes.model;
//
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//
//import org.springframework.hateoas.RepresentationModel;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Edge extends RepresentationModel<Edge> {
//	
//	@EmbeddedId
//	private EdgeId edgeId;
//	
//	@ManyToOne
//	@MapsId("boxId")
//	@JoinColumn(name="box_id")
//	private Box box;
//	
//	@ManyToOne
//	@MapsId("fromDotId")
//	@JoinColumn(name="from_dot_id")
//	private Dot fromDot;
//	
//	@ManyToOne
//	@MapsId("toDotId")
//	@JoinColumn(name="to_dot_id")
//	private Dot toDot;
//	
//	@Column
//	@JsonProperty
//	private boolean isEdgeAvailable;
//	
//	@Column
//	@JsonProperty
//	private String userId;
//}


package dotsandboxes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Edge extends RepresentationModel<Edge> {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "edge_id")
	@JsonProperty
	String edgeId;
	
	@Column
	@JsonProperty
	private String gameId;
	
	@ManyToOne
	@JoinColumn(name="box_id")
	private Box box;
	
	@ManyToOne
	@JoinColumn(name="from_dot_id")
	private Dot fromDot;
	
	@ManyToOne
	@JoinColumn(name="to_dot_id")
	private Dot toDot;
	
	@Column
	@JsonProperty
	private boolean isEdgeAvailable;
	
	@Column
	@JsonProperty
	private String userId;
	
	@Column
	@JsonProperty
	private int orderEdge;
}
