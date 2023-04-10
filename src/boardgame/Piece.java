package boardgame;

public abstract class Piece {
	
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
	
	public abstract boolean[][] possibleMoves();
		
	public boolean PossibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];//método para retornar possível movimento
	}
	
	public boolean isThereAnyPossibleMove() {//Método para verif se existe pelo menos 1 movimento possível da peça
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;// se em algum ponto da matriz retornar true é possivel mover a peça
				}
			}
		}
		return false;// do contrário retorna false
	}
}
