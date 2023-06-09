package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch();
		List<ChessPiece> captured = new ArrayList<>();

		while (!chessMatch.getCheckMate()) {
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured);// M�todo para imprimir o tabuleiro
				System.out.println();
				System.out.print("Source: ");
				ChessPosition source = UI.readChessPosition(sc);
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves);
				
				System.out.println();
				System.out.print("Target: ");
				ChessPosition target = UI.readChessPosition(sc);
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if (capturedPiece != null) {
					captured.add(capturedPiece);
				}
				
				if (chessMatch.getPromoted() != null) {//Se for dif de null, uma pe�a foi promovida
					System.out.println("Enter piece for promotion (N/N/R/Q): ");
					String type = sc.nextLine().toUpperCase();
					while ((!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q"))) {//Fazer isso para n�o quebrar o prog e s� validar quando for uma das 4 letras
						System.out.println("Invalid value. Enter piece for promotion (N/N/R/Q): ");
						type = sc.nextLine().toUpperCase();						
					}
					chessMatch.replacePromotedPiece(type);
				}
			}
			catch (ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();//faz isso para o programa aguardar a entrada do Enter				
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();//faz isso para o programa aguardar a entrada do Enter				
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}
}
