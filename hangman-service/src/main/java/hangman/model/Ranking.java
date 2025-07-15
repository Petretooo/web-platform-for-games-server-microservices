package hangman.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import hangman.dto.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ranking {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rank_id")
	private String id;
	@Column(name = "id_user_rank")
	private String idUserRank;
	private transient User user;
	@Column
	private String word;
	@Column(name = "score")
	private int score;
	@Column(name = "date_game")
	private LocalDate date;
	
}
