package aggregation.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

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
public class Stats {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "stats_id")
	@JsonProperty
	private String id;

	@Column
	@JsonProperty
	private String gameTitle;

	@Column
	@JsonProperty
	private String gameMode;

	@Column
	@JsonProperty
	private int popularityCounter;

	@Override
	public String toString() {
		return "Stats{" + "id='" + this.id + '\'' + ", gameTitle='" + this.gameTitle + '\'' + ", gameMode='"
				+ this.gameMode + '\'' + ", popularityCounter=" + this.popularityCounter + '}';
	}
}
