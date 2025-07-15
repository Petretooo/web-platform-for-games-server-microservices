package hangman.service.word;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import hangman.model.Word;
import hangman.repository.word.WordDao;

public class WordServiceImplTest {
	
	private static final int EXPECTED_PAGES = 3;
	private static final long WORD_NUMBER = 28;
	private static final int NUMBER_PER_PAGE = 3;
	private static final String WORD_NAME_THREE = "wordThree";
	private static final int METHOD_EXECUTION_TIMES = 1;
	private static final int LEVEL_OF_DIFFICULTY = 2;
	private static final String WORD_NAME_TWO = "Word Two";
	private static final String WORD_NAME_ONE = "Word One";

	@Mock
	private WordDao wordDao;

    @InjectMocks
    private WordServiceImpl wordServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
	
	@Test
	public void shouldReturnRandomWord() {
		
		List<Word> words = new ArrayList<>();
		
		Word wordOne = new Word();
		wordOne.setWordName(WORD_NAME_ONE);
		wordOne.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
		
		Word wordTwo = new Word();
		wordTwo.setWordName(WORD_NAME_TWO);
		wordTwo.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
		
		words.add(wordOne);
		words.add(wordTwo);
		
		when(wordDao.findAll()).thenReturn(words);
		
		Word retrievedWord = wordServiceImpl.randomWordGenerator();
		
		assertNotNull(retrievedWord);
	}
	
    @Test
    void shouldSaveWord() {

        Word savedWord = new Word();
        savedWord.setWordName(WORD_NAME_ONE);
        savedWord.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
        when(wordDao.save(any(Word.class))).thenReturn(savedWord);

        Word result = wordServiceImpl.saveWord(WORD_NAME_ONE, LEVEL_OF_DIFFICULTY);

        assertNotNull(result);
        assertEquals(WORD_NAME_ONE, result.getWordName());
        assertEquals(LEVEL_OF_DIFFICULTY, result.getLevelDifficulty());
    }

    @Test
    void shouldDeleteWord() {      
        
        Word wordToDelete = new Word();
        wordToDelete.setWordId(WORD_NAME_ONE);
        wordToDelete.setLevelDifficulty(LEVEL_OF_DIFFICULTY);

        when(wordDao.findBywordName(WORD_NAME_ONE)).thenReturn(wordToDelete);

        wordServiceImpl.deleteWord(WORD_NAME_ONE);

        verify(wordDao, times(METHOD_EXECUTION_TIMES)).delete(wordToDelete);
    }

    @Test
    void shouldFindAllWords() {
    	        
        Word wordOne = new Word();
        wordOne.setWordName(WORD_NAME_ONE);
        wordOne.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
        Word wordTwo = new Word();
        wordTwo.setWordName(WORD_NAME_TWO);
        wordTwo.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
        Word wordThree = new Word();
        wordThree.setWordName(WORD_NAME_THREE);
        wordThree.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
    	List<Word> words = Arrays.asList(wordOne, wordTwo, wordThree);

        when(wordDao.findAll()).thenReturn(words);

        List<Word> result = wordServiceImpl.findAllWords();

        assertNotNull(result);
        assertEquals(words.size(), result.size());
        assertTrue(result.containsAll(words));
    }

    @Test
    void shouldFindByPage() {
        
        Word wordOne = new Word();
        wordOne.setWordName(WORD_NAME_ONE);
        wordOne.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
        Word wordTwo = new Word();
        wordTwo.setWordName(WORD_NAME_TWO);
        wordTwo.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
        Word wordThree = new Word();
        wordThree.setWordName(WORD_NAME_THREE);
        wordThree.setLevelDifficulty(LEVEL_OF_DIFFICULTY);
        
    	List<Word> words = Arrays.asList(wordOne, wordTwo, wordThree);
    	
        when(wordDao.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(words));

        List<Word> result = wordServiceImpl.findByPage(NUMBER_PER_PAGE);

        assertNotNull(result);
        assertEquals(NUMBER_PER_PAGE, result.size());
        assertTrue(result.containsAll(words));
    }

    @Test
    void shouldРетурнNumberPages() {

        when(wordDao.count()).thenReturn(WORD_NUMBER);

        long result = wordServiceImpl.numberPages();

        assertEquals(EXPECTED_PAGES, result);
    }
	
}
