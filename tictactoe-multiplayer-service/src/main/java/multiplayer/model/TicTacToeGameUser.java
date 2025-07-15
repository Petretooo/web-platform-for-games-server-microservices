//package tictactoe.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "tictactoe_game_user")
//@Setter
//@Getter
//@AllArgsConstructor
//@RequiredArgsConstructor
//public class TicTacToeGameUser implements Serializable {
//	
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -5062013106673579972L;
//
//	@Id
//	@MapsId
//	@ManyToOne
//	@JoinColumn(name = "game_id")
//	private TicTacToeGame game;
//	
//	@Id
//	@Column
//	private String userId;
//
//}

package multiplayer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class TicTacToeGameUser {
	
	private TicTacToeGameUserId tictactoeGameUserId;
}


