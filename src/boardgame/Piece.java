package boardgame;

public class Piece {
	
	protected Position position;// Pe�a fica como null, por isso n�o entra no construtor
	private Board board;
	
	public Piece(Board board) {
		this.board = board; // O construtor tem apenas o tabuleiro
		position = null; // Se n�o colocar, o java j� atribui como null
	}
	// Somente classes do mesmo pacote e subclasses podem acessar esse m�todo, por isso protected
	protected Board getBoard() {// Usa-se somente o get pois n�o instancia outro tabuleiro
		return board;
	}
}
