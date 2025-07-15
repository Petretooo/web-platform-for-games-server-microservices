package hangman.service.alphabet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hangman.model.Symbol;
import hangman.repository.symbol.SymbolDao;

class AlphabetServiceImplTest {

    @Mock
    private SymbolDao characterDao;

    @InjectMocks
    private AlphabetServiceImpl alphabetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldMakeTryInGameAlphabet() {
    	
        String gameId = "gameId";

        alphabetService.setGameAlphabet(gameId);

        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        
        alphabetService.setGameAlphabet(gameId);
        alphabetService.saveCharacter(gameId, symbolA);
                
        Map<Character, Boolean> gameAlphabet = alphabetService.getCurrentGameAlphabet(gameId);
        boolean isSymbolUsed = gameAlphabet.get('A');

        assertTrue(isSymbolUsed);
    }

    @Test
    void shouldCreateAlphabetWithEmptySymbolsAndSaveUsedSymbol() {
    	
        String gameId = "gameId";

        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        
        alphabetService.setGameAlphabet(gameId);

        Set<Symbol> symbols = Collections.EMPTY_SET;
        
        alphabetService.saveCharacterWithUsed(gameId, symbolA, symbols);
                
        Map<Character, Boolean> gameAlphabet = alphabetService.getCurrentGameAlphabet(gameId);
        boolean isSymbolUsed = gameAlphabet.get('A');

        assertTrue(isSymbolUsed);
    }
    
    @Test
    void shouldCreateAlphabetWithProvidedSymbols() {
    	
        String gameId = "gameId";

        Symbol symbolA = new Symbol();
        symbolA.setLetter('A');
        
        Set<Symbol> symbols = new HashSet<Symbol>();
        symbols.add(symbolA);
        
        alphabetService.getUnusedCharacters(gameId, symbols);
                
        Map<Character, Boolean> gameAlphabet = alphabetService.getCurrentGameAlphabet(gameId);

        assertNotNull(gameAlphabet);
    }


}