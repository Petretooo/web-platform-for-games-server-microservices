package hangman.repository.symbol;

import java.util.List;

import hangman.model.Symbol;

public interface SymbolDao {
	public Symbol get(String id);
	public void remove(String id);
	public List<Symbol> getAll();
	public void save(Symbol entity);
}
