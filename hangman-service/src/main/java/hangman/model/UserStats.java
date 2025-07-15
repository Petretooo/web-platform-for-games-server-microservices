package hangman.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserStats {

	@Id
	@GeneratedValue(generator = "UUID")
	  @GenericGenerator(
	          name = "UUID",
	          strategy = "org.hibernate.id.UUIDGenerator"
	  )
    @Column(name = "user_stats_id")
	private String id;
	@Column
	private int score;
	@Column
	private String word;
	@Column(name = "user_id", insertable =false, updatable =false)
	private String idUserStats;
	@Column(name = "user_id")
	private String userId;
}
