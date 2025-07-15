package hangman.model;

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

import hangman.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class HangmanGameRoom {
	
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

	@Column
	@JsonProperty
	private String word;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "first_user_game", referencedColumnName = "game_id")
	@JsonIgnore 
	private HangmanGame firstUserGame;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "second_user_game", referencedColumnName = "game_id")
	@JsonIgnore 
	private HangmanGame secondUserGame;

	@Column(name = "first_user_id")
	@JsonIgnore 
	private String firstUserId;

	@Column(name = "second_user_id")
	@JsonIgnore 
	private String secondUserId;
	
	private transient User firstUser;
	private transient User secondUser;
}
