package boardgame;

public class Piece {
	
	protected Position position;// Peça fica como null, por isso não entra no construtor
	private Board board;
	
	public Piece(Board board) {
		this.board = board; // O construtor tem apenas o tabuleiro
		position = null; // Se não colocar, o java já atribui como null
	}
	// Somente classes do mesmo pacote e subclasses podem acessar esse método, por isso protected
	protected Board getBoard() {// Usa-se somente o get pois não instancia outro tabuleiro
		return board;
	}
}
