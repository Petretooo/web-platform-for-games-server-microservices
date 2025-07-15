package hangman.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Word extends RepresentationModel<Word>{

	@Id
	@GeneratedValue(generator = "UUID")
	  @GenericGenerator(
	          name = "UUID",
	          strategy = "org.hibernate.id.UUIDGenerator"
	  )
    @Column
	private String wordId;
	@Column(name = "word")
	private String wordName;
	@Column(name = "level_difficulty")
	private int levelDifficulty;
}
