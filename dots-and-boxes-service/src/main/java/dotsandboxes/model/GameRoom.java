package dotsandboxes.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class GameRoom {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column
	@JsonProperty
	private String gameRoomId;

	@Column
	@JsonProperty
	private String gameRoomName;

	@Column
	@JsonProperty
	private String gameTitle;

	@Column
	@JsonProperty
	private boolean active;

	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "game_id", referencedColumnName = "game_id")
	@JsonIgnore
	private Game game;

	@Column(name = "first_user_id")
	@JsonProperty
	private String firstUser;

	@Column
	@JsonProperty
	private String firstUserSymbol;
	
	@Column
	@JsonProperty
	private int firstUserScore;

	@Column(name = "second_user_id")
	@JsonProperty
	private String secondUser;

	@Column
	@JsonProperty
	private String secondUserSymbol;
	
	@Column
	@JsonProperty
	private int secondUserScore;
}
