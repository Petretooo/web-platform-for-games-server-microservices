package dotsandboxes.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "game_user")
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class GameUser {
	
	@EmbeddedId
	private GameUserId dotsAndBoxesGameUserId;

}


