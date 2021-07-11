package omok;

import java.util.ArrayList;

public class Minimax {
	// 몇 수 앞서볼건지! (난이도에 따라 달라진다.)
	private int depth;
	private int[][] matrix;
	public static int evaluationCount = 0;
	private static final int winScore = 100000000;

	// constructor
	public Minimax(int depth) {
		this.depth = depth;
	}

	// get winScore
	public static int getWinScore() {
		return winScore;
	}

	public static double evaluateBoardForWhite(int[][] matrix, boolean blacksTurn) {
		evaluationCount++;

		double blackScore = getScore(matrix, true, blacksTurn);
		double whiteScore = getScore(matrix, false, blacksTurn);

		if (blackScore == 0)
			blackScore = 1.0;

		return whiteScore / blackScore;

	}

	// 점수 받아오는 점수
	public static int getScore(int[][] matrix, boolean forBlack, boolean isBlack) {

		int[][] boardMatrix = matrix; /// ?

		return evaluateHorizontal(boardMatrix, forBlack, isBlack) + evaluateVertical(boardMatrix, forBlack, isBlack)
				+ evaluateDiagonal(boardMatrix, forBlack, isBlack);
	}

	public int[] think(int[][] board) {
		// init
		this.matrix = board;

		// AI의 최종적 생각
		int[] move = new int[2];

		long startTime = System.currentTimeMillis();

		// check if any available move can finish the game
		Object[] bestMove = searchWinningMove(matrix);

		// 존재하면,
		if (bestMove != null) {
			move[0] = (Integer) (bestMove[1]);
			move[1] = (Integer) (bestMove[2]);
		}
		// 존재하지 않는다면,
		else {
			// minimax 가동
			bestMove = minimaxSearch(depth, matrix, true, -1.0, getWinScore());

			// check
			//System.out.println("bestMove: " + bestMove[0] + " " + bestMove[1] + " " + bestMove[2]);

			// 답이 없으면,
			if (bestMove[1] == null) {
				move = null;
			}
			// 도출된 답이 있으면,
			else {
				move[0] = (Integer) (bestMove[1]);
				move[1] = (Integer) (bestMove[2]);
			}
		}
		// check calculate time
		System.out.println("Calculated cases: " + evaluationCount + "\n Calculation time: "
				+ (System.currentTimeMillis() - startTime) + " ms");

		// print on showProcess textArea.
		if (Board_PvE.show != null) {
			Board_PvE.show.textArea.append("[ Cases calculated: " + evaluationCount + " ]\n" + "[ Calculation time: "
					+ (System.currentTimeMillis() - startTime) + " ms ]" + "\n");
		} /// printer
		
		// init
		evaluationCount = 0;

		// check
		System.out.println("moves: [" + move[0] + ", " + move[1] + "]");

		return move;
	}

	// MINIMAX WITH ALPHA BETA
	private static Object[] minimaxSearch(int depth, int[][] matrix, boolean max, double alpha, double beta) {
		if (depth == 0) {
			Object[] x = { evaluateBoardForWhite(matrix, !max), null, null };
			return x;
		}

		// 움직일 수 있는 곳들 저장.
		ArrayList<int[]> allPossibleMoves = generateMoves(matrix);

		// check
		//for (int[] a : allPossibleMoves) {
		//	System.out.println(a[0] + " " + a[1]);
		//}

		// 움직일 수 있는 곳이 없다면,
		if (allPossibleMoves.size() == 0) {
			Object[] x = { evaluateBoardForWhite(matrix, !max), null, null };
			return x;
		}

		// 본격적으로 계산합니다.
		Object[] bestMove = new Object[3];

		// max를 구해야 한다면,
		if (max) {
			bestMove[0] = -1.0;
			for (int[] move : allPossibleMoves) {
				// 임시로 matrix 만들고
				int[][] dummyMatrix = makeNewMatrix(matrix);
				// 돌 놓아보기.
				addStone(dummyMatrix, move[1], move[0], false);

				// 다음 깊이에 따라 minimax 실행(recursive). (max는 반대로! depth는 하나 빼고!)
				Object[] tempMove = minimaxSearch(depth - 1, dummyMatrix, !max, alpha, beta);

				// alpha update
				if ((Double) (tempMove[0]) > alpha) {
					alpha = (Double) (tempMove[0]);
				}
				// beta update
				if ((Double) (tempMove[0]) >= beta) {
					return tempMove;
				}
				if ((Double) tempMove[0] > (Double) bestMove[0]) {
					bestMove = tempMove;
					bestMove[1] = move[0];
					bestMove[2] = move[1];
				}
			}
		}
		// min을 구해야 한다면,
		else {
			// init
			bestMove[0] = 100000000.0;
			bestMove[1] = allPossibleMoves.get(0)[0];
			bestMove[2] = allPossibleMoves.get(0)[1];

			// allPossibleMoves에 대해 체크
			for (int[] move : allPossibleMoves) {
				// 임시로 matrix 만들고
				int[][] dummyMatrix = makeNewMatrix(matrix);
				// 돌 놓아보기.
				addStone(dummyMatrix, move[1], move[0], true);

				// 다음 깊이에 따라 minimax 실행. (max는 반대로! depth는 하나 빼고!)
				Object[] tempMove = minimaxSearch(depth - 1, dummyMatrix, !max, alpha, beta);

				// beta update
				if (((Double) tempMove[0]) < beta) {
					beta = (Double) (tempMove[0]);
				}
				// alpha update
				if ((Double) (tempMove[0]) <= alpha) {
					return tempMove;
				}
				if ((Double) tempMove[0] < (Double) bestMove[0]) {
					bestMove = tempMove;
					bestMove[1] = move[0];
					bestMove[2] = move[1];
				}
			}
		}
		return bestMove;
	}

	// 끝낼 수 있는 수가 있는지 찾는다.
	private static Object[] searchWinningMove(int[][] matrix) {

		ArrayList<int[]> allPossibleMoves = generateMoves(matrix);

		Object[] winningMove = new Object[3];

		// allPossibleMoves 리스트 체크
		for (int[] move : allPossibleMoves) {
			evaluationCount++;
			// 임시 matrix 만들기.
			int[][] dummyMatrix = makeNewMatrix(matrix);

			// dummy matrix에 임의로 돌 놓아보기.
			addStone(dummyMatrix, move[1], move[0], false);

			// 돌 놓은 곳이 확실히 이기는 곳이라면,
			if (getScore(dummyMatrix, false, false) >= winScore) {
				winningMove[1] = move[0];
				winningMove[2] = move[1];
				return winningMove;
			}
		}
		return null;
	}

	// 임의로 돌 놓아보는 함수.
	public static void addStone(int[][] matrix, int x, int y, boolean isBlack) {
		matrix[y][x] = isBlack ? 2 : 1;
	}

	// 돌 놓을 수 있는 곳들 ArratList에 저장시키는 함수.
	public static ArrayList<int[]> generateMoves(int[][] board) {
		ArrayList<int[]> moveList = new ArrayList<int[]>();

		int boardSize = Board_PvE.matrix.length;
		int[][] matrix = board;

		// Look for cells that has at least one stone in an adjacent cell.
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {

				if (matrix[i][j] > 0)
					continue;

				if (i > 0) {
					if (j > 0) {
						if (matrix[i - 1][j - 1] > 0 || matrix[i][j - 1] > 0) {
							int[] move = { i, j };
							moveList.add(move);
							continue;
						}
					}
					if (j < boardSize - 1) {
						if (matrix[i - 1][j + 1] > 0 || matrix[i][j + 1] > 0) {
							int[] move = { i, j };
							moveList.add(move);
							continue;
						}
					}
					if (matrix[i - 1][j] > 0) {
						int[] move = { i, j };
						moveList.add(move);
						continue;
					}
				}
				if (i < boardSize - 1) {
					if (j > 0) {
						if (matrix[i + 1][j - 1] > 0 || matrix[i][j - 1] > 0) {
							int[] move = { i, j };
							moveList.add(move);
							continue;
						}
					}
					if (j < boardSize - 1) {
						if (matrix[i + 1][j + 1] > 0 || matrix[i][j + 1] > 0) {
							int[] move = { i, j };
							moveList.add(move);
							continue;
						}
					}
					if (matrix[i + 1][j] > 0) {
						int[] move = { i, j };
						moveList.add(move);
						continue;
					}
				}

			}
		}

		return moveList;
	}

	// 계산함수들..
	public static int evaluateHorizontal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		for (int i = 0; i < boardMatrix.length; i++) {
			for (int j = 0; j < boardMatrix[0].length; j++) {
				if (boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					consecutive++;
				} else if (boardMatrix[i][j] == 0) {
					if (consecutive > 0) {
						blocks--;
						score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
						consecutive = 0;
						blocks = 1;
					} else {
						blocks = 1;
					}
				} else if (consecutive > 0) {
					score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
					consecutive = 0;
					blocks = 2;
				} else {
					blocks = 2;
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

			}
			consecutive = 0;
			blocks = 2;

		}
		return score;
	}

	public static int evaluateVertical(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;

		for (int j = 0; j < boardMatrix[0].length; j++) {
			for (int i = 0; i < boardMatrix.length; i++) {
				if (boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					consecutive++;
				} else if (boardMatrix[i][j] == 0) {
					if (consecutive > 0) {
						blocks--;
						score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
						consecutive = 0;
						blocks = 1;
					} else {
						blocks = 1;
					}
				} else if (consecutive > 0) {
					score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
					consecutive = 0;
					blocks = 2;
				} else {
					blocks = 2;
				}
			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

			}
			consecutive = 0;
			blocks = 2;

		}
		return score;
	}

	public static int evaluateDiagonal(int[][] boardMatrix, boolean forBlack, boolean playersTurn) {

		int consecutive = 0;
		int blocks = 2;
		int score = 0;
		// From bottom-left to top-right diagonally
		for (int k = 0; k <= 2 * (boardMatrix.length - 1); k++) {
			int iStart = Math.max(0, k - boardMatrix.length + 1);
			int iEnd = Math.min(boardMatrix.length - 1, k);
			for (int i = iStart; i <= iEnd; ++i) {
				int j = k - i;

				if (boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					consecutive++;
				} else if (boardMatrix[i][j] == 0) {
					if (consecutive > 0) {
						blocks--;
						score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
						consecutive = 0;
						blocks = 1;
					} else {
						blocks = 1;
					}
				} else if (consecutive > 0) {
					score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
					consecutive = 0;
					blocks = 2;
				} else {
					blocks = 2;
				}

			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

			}
			consecutive = 0;
			blocks = 2;
		}
		// From top-left to bottom-right diagonally
		for (int k = 1 - boardMatrix.length; k < boardMatrix.length; k++) {
			int iStart = Math.max(0, k);
			int iEnd = Math.min(boardMatrix.length + k - 1, boardMatrix.length - 1);
			for (int i = iStart; i <= iEnd; ++i) {
				int j = i - k;

				if (boardMatrix[i][j] == (forBlack ? 2 : 1)) {
					consecutive++;
				} else if (boardMatrix[i][j] == 0) {
					if (consecutive > 0) {
						blocks--;
						score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
						consecutive = 0;
						blocks = 1;
					} else {
						blocks = 1;
					}
				} else if (consecutive > 0) {
					score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);
					consecutive = 0;
					blocks = 2;
				} else {
					blocks = 2;
				}

			}
			if (consecutive > 0) {
				score += getConsecutiveSetScore(consecutive, blocks, forBlack == playersTurn);

			}
			consecutive = 0;
			blocks = 2;
		}
		return score;
	}

	public static int getConsecutiveSetScore(int count, int blocks, boolean currentTurn) {
		final int winGuarantee = 1000000;
		if (blocks == 2 && count < 5)
			return 0;
		switch (count) {
		case 5: {
			return winScore;
		}
		case 4: {
			if (currentTurn)
				return winGuarantee;
			else {
				if (blocks == 0)
					return winGuarantee / 4;
				else
					return 200;
			}
		}
		case 3: {
			if (blocks == 0) {
				if (currentTurn)
					return 50000;
				else
					return 200;
			} else {
				if (currentTurn)
					return 10;
				else
					return 5;
			}
		}
		case 2: {
			if (blocks == 0) {
				if (currentTurn)
					return 7;
				else
					return 5;
			} else {
				return 3;
			}
		}
		case 1: {
			return 1;
		}
		}
		return winScore * 2;
	}

	private static int[][] makeNewMatrix(int[][] matrix) {
		int[][] newMatrix = new int[15][15];
		for (int i = 0; i < newMatrix.length; i++) {
			for (int j = 0; j < newMatrix.length; j++) {
				newMatrix[i][j] = matrix[i][j];
			}
		}
		return newMatrix;
	}

}