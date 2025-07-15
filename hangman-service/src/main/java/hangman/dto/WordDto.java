package hangman.dto;

import java.util.List;

import hangman.model.Word;
import lombok.Data;

@Data
public class WordDto {
	
	private List<Word> words;
	private long pageCount;
}
