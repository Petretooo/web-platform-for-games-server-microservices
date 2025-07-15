package hangman.service.word;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hangman.model.Word;
import hangman.repository.word.WordDao;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class WordServiceImpl implements WordService {

	@Autowired
	private WordDao wordDao;
	
	private static final int PAGE_SIZE = 10;

	@Override
	public Word randomWordGenerator() {
		Random random = new Random();
		Word[] WORDSARR = new Word[findAllWords().size()];
		WORDSARR = findAllWords().toArray(WORDSARR);
		return WORDSARR[random.nextInt(findAllWords().size())];
	}

	@Override
	public Word saveWord(String word, int levelDif) {
		Word w = new Word();
		w.setWordName(word);
		w.setLevelDifficulty(levelDif);
		return wordDao.save(w);
	}

	@Override
	public void deleteWord(String word) {
		Word w = wordDao.findBywordName(word);
		wordDao.delete(w);
	}

	@Override
	public List<Word> findAllWords() {
		return  wordDao.findAll();
	}

	@Override
	public List<Word> findByPage(int page) {
		Pageable pageWords = PageRequest.of(page-1, PAGE_SIZE, Sort.by("wordName"));
		
		return wordDao.findAll(pageWords).toList();
	}

	@Override
	public long numberPages() {
		long wordCount = wordDao.count();
		return wordCount % PAGE_SIZE == 0 ? wordCount / PAGE_SIZE : wordCount / PAGE_SIZE + 1;
	}

}
