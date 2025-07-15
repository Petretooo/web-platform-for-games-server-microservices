package hangman.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlRootElement(name = "rankingList")
@XmlAccessorType(XmlAccessType.FIELD)
public class RankingDtoList {
	private List<RankingDto> dtoList;
}
