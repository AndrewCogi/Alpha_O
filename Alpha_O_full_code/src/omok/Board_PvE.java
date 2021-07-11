package omok;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import omok.Board_PvE;
import omok.CheckGameOver_PvE;
import omok.MainMenu;

public class Board_PvE {

	public static boolean isBlack = true; // check turn (stone color)
	private static int boardSize = 15; // 임시지정.
	public static JButton[] button = new JButton[boardSize * boardSize]; // matrix buttons
	public static int[][] matrix = new int[boardSize][boardSize]; // matrix
	public static Stack<int[]> stack = new Stack<>(); // 뒤로가기를 위한 stack
	public static JButton undo; // 뒤로가기 버튼
	public static JButton showProcess; // 프로세스 보여주는 버튼
	public static ShowProcess show; // process 창 담당.
	public static Minimax AI; // AI
	public static boolean computerTurn; // check computer turn
	public static int depth; // think depth

	// constructor
	public Board_PvE() {

		// basic setting of computer AI
		// level & turn

		// easy selected
		if (MainMenu.seq[2].equalsIgnoreCase("easy")) {
			depth = 3;
			AI = new Minimax(depth);
		}
		// hard selected
		else if (MainMenu.seq[2].equalsIgnoreCase("hard")) {
			depth = 5;
			AI = new Minimax(depth);
		}

		// check computer turn
		// "human" <- black selected
		if (MainMenu.seq[1].equalsIgnoreCase("black")) {
			computerTurn = false;
		}
		// "human" <- white selected
		else if (MainMenu.seq[1].equalsIgnoreCase("white")) {
			computerTurn = true;
		}

		// make panel
		JPanel middlePane = new JPanel();
		JPanel bottomPane = new JPanel();
		middlePane.setLayout(new GridLayout(boardSize, boardSize));

		// add board buttons
		for (int i = 0; i < boardSize * boardSize; i++) {
			if (i == 0) {// 왼쪽 위 가장자리
				button[i] = new JButton(new ImageIcon("./img/board/leftUp.png"));
			} else if (i == 14) {// 오른쪽 위 가장자리
				button[i] = new JButton(new ImageIcon("./img/board/rightUp.png"));
			} else if (i == 210) {// 왼쪽 아래 가장자리
				button[i] = new JButton(new ImageIcon("./img/board/leftDown.png"));
			} else if (i == 224) {// 오른쪽 아래 가장자리
				button[i] = new JButton(new ImageIcon("./img/board/rightDown.png"));
			} else if (i >= 1 && i <= 13) { // 위 edge
				button[i] = new JButton(new ImageIcon("./img/board/topEdge.png"));
			} else if (i >= 211 && i <= 224) { // 아래 edge
				button[i] = new JButton(new ImageIcon("./img/board/bottomEdge.png"));
			} else if (i % 15 == 0 && i >= 1) { // 왼쪽 edge
				button[i] = new JButton(new ImageIcon("./img/board/leftEdge.png"));
			} else if (i % 15 == 14) { // 오른쪽 edge
				button[i] = new JButton(new ImageIcon("./img/board/rightEdge.png"));
			} else { // 가운데
				button[i] = new JButton(new ImageIcon("./img/board/middle.png"));
			}

			// button의 윤곽선 없애기, 선택되었을 때 윤곽선 없애기
			button[i].setBorderPainted(false);
			button[i].setFocusPainted(false);

			// add button into panel
			middlePane.add(button[i]);
		}

		// add button listener
		for (int a = 0; a < boardSize * boardSize; a++) {
			button[a].addActionListener(new Listener_PvE(a));
		}

		// add bottom buttons & icons
		// JButton backToMainMenu = new JButton(new
		// ImageIcon("./img/menubar/backToMainMenu.png"));
		// undo = new JButton(new ImageIcon("./img/menubar/undo.png"));
		// JButton newGame = new JButton(new ImageIcon("./img/menubar/newGame.png"));
		// showProcess = new JButton(new ImageIcon("./img/menubar/showProcess.png"));
		// JButton exit = new JButton(new ImageIcon("./img/menubar/exit.png"));

		JButton backToMainMenu = new JButton("Back to Main Menu");
		undo = new JButton("Undo");
		JButton newGame = new JButton("New Game");
		showProcess = new JButton("Show Process");
		JButton exit = new JButton("Exit");

		bottomPane.add(backToMainMenu);
		bottomPane.add(undo);
		bottomPane.add(newGame);
		bottomPane.add(showProcess);
		bottomPane.add(exit);

		// add bottom button ActionListener

		// exit
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// yes? no?
				int result = JOptionPane.showConfirmDialog(MainMenu.getFrame(), "EXIT?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				// yes : 종료.
				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				// no : 무시하고 그대로 둔다.
				// 그냥 창을 닫은 경우 : 마찬가지.
				else {
					return;
				}
			}
		});

		// back to main menu
		backToMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// yes? no?
				int result = JOptionPane.showConfirmDialog(MainMenu.getFrame(), "GOTO MainMenu?", "Confirm",
						JOptionPane.YES_NO_OPTION);
				// yes : 하던거 다 없애고 (초기값으로 두고) 메인메뉴로 나가기.
				if (result == JOptionPane.YES_OPTION) {
					// system상 확인
					System.out.println("GO TO MAINMENU");

					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("==============================================" + "\n");
					} /// printer

					// 초기값으로 돌립니다.

					// 0. stack안에 값이 있다면,
					// 1. stack을 비워주고,
					// 2. matrix 0으로 싹 초기화해주고
					// 3. turn 흑돌로(true로) 배치해주기.
					// 4. undo, showProcess 버튼 활성화
					// 5. 화면 지우기
					if (Board_PvE.stack.isEmpty() == false)
						Board_PvE.stack.removeAllElements();
					for (int i = 0; i < 225; i++) {
						Board_PvE.matrix[i / 15][i % 15] = 0;
					}
					Board_PvE.isBlack = true;

					Board_PvE.undo.setEnabled(true);
					Board_PvE.showProcess.setEnabled(true);

					MainMenu.getContainer().removeAll();
					MainMenu.getContainer().revalidate();
					MainMenu.getContainer().repaint();

					// 새로운 시작.
					MainMenu.MakeNewMain();
				}
				// no : 그냥 무시하고 계속 진행.
				// 그냥 창을 닫은 경우 : 무시하고 계속 진행.
				else {
					return;
				}
			}
		});

		// 뒤로가기 실행버튼 (PvE는 두번 빼줍니다.)
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0-1. stack안에 값이 있다면,
				// 1. stack에서 맨 윗 값을 빼온다.
				// 2. 빼온 값에 대한 좌표의 버튼이미지를 default로 바꾼다.
				// 3. matrix의 값도 0으로 바꾸어 준다.
				// 4. turn도 바꿔준다.

				// 0-2. stack안에 값이 없다면,
				// return; (nothing to do)

				if (Board_PvE.stack.isEmpty() == false) {
					// 빼고,
					int[] temp1 = Board_PvE.stack.pop();
					int num1 = temp1[0] * 15 + temp1[1];
					Board_PvE.drawStoneDefault(num1);
					Board_PvE.matrix[temp1[0]][temp1[1]] = 0;
					Board_PvE.isBlack = !isBlack;

					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Undo >> [" + temp1[0] + ", " + temp1[1] + "]" + "\n");
					} /// printer

					// 1번 더 뺍니다.
					int[] temp2 = Board_PvE.stack.pop();
					int num2 = temp2[0] * 15 + temp2[1];
					Board_PvE.drawStoneDefault(num2);
					Board_PvE.matrix[temp2[0]][temp2[1]] = 0;
					Board_PvE.isBlack = !isBlack;

					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Undo >> [" + temp2[0] + ", " + temp2[1] + "]" + "\n");
					} /// printer

				} else
					return;
			}
		});

		// 새로운 게임 실행버튼
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0. stack안에 값이 있다면,
				// 1. stack을 비워주고,
				// 2. 보드판 싹 다 지우기.
				// 3. matrix 0으로 싹 초기화해주고
				// 4. turn 흑돌로(true로) 배치해주기.
				// 5. 매트릭스 버튼들 모두 활성화
				// 6. undo, showProcess 버튼 활성화
				if (Board_PvE.stack.isEmpty() == false)
					Board_PvE.stack.removeAllElements();
				for (int i = 0; i < 225; i++) {
					Board_PvE.drawStoneDefault(i);
					Board_PvE.matrix[i / 15][i % 15] = 0;
				}
				Board_PvE.isBlack = true;
				Board_PvE.computerTurn = false;

				for (int i = 0; i < Board_PvE.button.length; i++) {
					Board_PvE.button[i].setEnabled(true);
				}

				Board_PvE.undo.setEnabled(true);
				Board_PvE.showProcess.setEnabled(true);

				// 첫 시작 세팅.(turn = white일때만 실행)
				if (MainMenu.seq[1].equalsIgnoreCase("white")) {
					// ([7,7]일 때, num = 112)
					int num = 112;
					// matrix 중간에 ([7,7]에) 검은 돌 놓기.
					Board_PvE.matrix[num / 15][num % 15] += 2;

					// GUI상에서도 흑돌 배치하기
					Board_PvE.drawStoneBlack(num);

					// stack에 놓은 곳 저장하기. (저장 안해도 되지 않나요?)
					// int[] position = new int[2];
					// position[0] = num / 15;
					// position[1] = num % 15;
					// Board_PvE.stack.push(position);

					// change turn (stone color)
					Board_PvE.isBlack = false;

					// change computer turn
					Board_PvE.computerTurn = false;

				}

				// check
				System.out.println("Start new Game.");

				// print on showProcess textArea.
				if (Board_PvE.show != null) {
					Board_PvE.show.textArea.append("==================New Game=================" + "\n");
				} /// printer

			}
		});

		// 프로세스 보여주는 버튼
		showProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				show = new ShowProcess(); // 새 창 실행
			}
		});

		// make rest of panels & coloring
		JPanel north = new JPanel();
		north.setBackground(Color.WHITE);
		JPanel east = new JPanel();
		east.setBackground(Color.WHITE);
		JPanel west = new JPanel();
		west.setBackground(Color.WHITE);
		middlePane.setBackground(Color.WHITE);
		bottomPane.setBackground(Color.WHITE);

		// insert into frame
		MainMenu.getFrame().add(north, BorderLayout.NORTH);
		MainMenu.getFrame().add(east, BorderLayout.EAST);
		MainMenu.getFrame().add(west, BorderLayout.WEST);
		MainMenu.getFrame().add(middlePane, BorderLayout.CENTER);
		MainMenu.getFrame().add(bottomPane, BorderLayout.SOUTH);

		// AI부분으로, 추가된 부분입니다. (new game button에도 추가되었습니다.)
		// computer turn이라면, (첫 시작)
		if (Board_PvE.computerTurn == true) {
			// ([7,7]일 때, num = 112)
			int num = 112;
			// matrix 중간에 ([7,7]에) 검은 돌 놓기.
			Board_PvE.matrix[num / 15][num % 15] += 2;

			// GUI상에서도 흑돌 배치하기
			Board_PvE.drawStoneBlack(num);

			// stack에 놓은 곳 저장하기. (저장 안해도 되지 않나요?)
			// int[] position = new int[2];
			// position[0] = num / 15;
			// position[1] = num % 15;
			// Board_PvE.stack.push(position);

			// change turn (stone color)
			Board_PvE.isBlack = false;

			// change computer turn
			Board_PvE.computerTurn = false;
		}

	}

	// 놓인 돌 없애주는 함수
	public static void drawStoneDefault(int num) {
		if (num == 0) {// 왼쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftUp.png"));
		} else if (num == 14) {// 오른쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightUp.png"));
		} else if (num == 210) {// 왼쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftDown.png"));
		} else if (num == 224) {// 오른쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // 위 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // 아래 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // 왼쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftEdge.png"));
		} else if (num % 15 == 14) { // 오른쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightEdge.png"));
		} else { // 가운데
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/middle.png"));
		}
	}

	// 흰색 돌 놓아주는 함수
	public static void drawStoneWhite(int num) {
		// if()활용하여 알맞은 돌로 바꿔주기(돌 모양)
		if (num == 0) {// 왼쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftUp.png"));
		} else if (num == 14) {// 오른쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightUp.png"));
		} else if (num == 210) {// 왼쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftDown.png"));
		} else if (num == 224) {// 오른쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // 위 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // 아래 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // 왼쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftEdge.png"));
		} else if (num % 15 == 14) { // 오른쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightEdge.png"));
		} else { // 가운데
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/middle.png"));
		}
	}

	// 검은색 돌 놓아주는 함수
	public static void drawStoneBlack(int num) {
		// if()활용하여 알맞은 돌로 바꿔주기(돌 모양)
		if (num == 0) {// 왼쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftUp.png"));
		} else if (num == 14) {// 오른쪽 위 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightUp.png"));
		} else if (num == 210) {// 왼쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftDown.png"));
		} else if (num == 224) {// 오른쪽 아래 가장자리
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // 위 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // 아래 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // 왼쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftEdge.png"));
		} else if (num % 15 == 14) { // 오른쪽 edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightEdge.png"));
		} else { // 가운데
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/middle.png"));
		}
	}
}

class Listener_PvE implements ActionListener, Runnable {

	private int num;
	public int aa;

	public Listener_PvE(int num) {
		this.num = num;
	}

	public synchronized void run() {

		// press check
		int count = 0;
		if (Thread.currentThread().getName() == "a")
			System.out.println("aaa" + num / 15 + ", " + num % 15);
		if (Thread.currentThread().getName() == "b")
			System.out.println("bbb" + num / 15 + ", " + num % 15);
		// computer turn이 아니라면, (당연히 아니어야합니다. 안돌아가면 여기에 문제있는겨..)
		if (Board_PvE.computerTurn == false) {
			// 먼저 사람이 놓는다.
			// matrix change
			if (Thread.currentThread().getName() == "a") {
				if (Board_PvE.isBlack == true && Board_PvE.matrix[num / 15][num % 15] == 0) { // 검정돌 차례고 아무것도
					// 놓여있지
					// 않다면,

					Board_PvE.matrix[num / 15][num % 15] += 2; // 검은색이면 +2

					// 흑돌 배치하기.
					Board_PvE.drawStoneBlack(num);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Black Stone at [" + num / 15 + ", " + num % 15 + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, num / 15, num % 15, count) == true) { // 게임이
						// 끝났으면,
						// 매트릭스 버튼들 모두 비활성화시켜야하고
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process 비활성화. new game만 눌릴 수 있는 상태로 만들어주어야 한다.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// 흑돌을 마지막에 두었으므로 흑이 이겼다고 출력해야 함. ( 콘솔상으로 & 화면상으로 )
						System.out.println("Black win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("Black Win!" + "\n");
						} /// printer

						JOptionPane.showMessageDialog(MainMenu.getFrame(), "Black Win!");
						count = 1;
						return;

					}

					// stack에 놓은 곳 저장하기.
					int[] position = new int[2];
					position[0] = num / 15;
					position[1] = num % 15;
					Board_PvE.stack.push(position);
					// check
					System.out
							.println("pushed: [" + Board_PvE.stack.peek()[0] + ", " + Board_PvE.stack.peek()[1] + "]");

					// change turn
					Board_PvE.isBlack = false;

					// change computer turn
					Board_PvE.computerTurn = true;
				}

				else if (Board_PvE.isBlack == false && Board_PvE.matrix[num / 15][num % 15] == 0) { // 흰돌 차례고
					// 아무것도
					// 놓여있지
					// 않다면,

					Board_PvE.matrix[num / 15][num % 15] += 1; // 흰색이면 +1

					// 백돌 배치하기.

					Board_PvE.drawStoneWhite(num);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("White Stone at [" + num / 15 + ", " + num % 15 + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, num / 15, num % 15, count) == true) { // 게임이
						// 끝났으면,
						// 매트릭스 버튼들 모두 비활성화시켜야하고
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process 비활성화. new game만 눌릴 수 있는 상태로 만들어주어야 한다.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// 흑돌을 마지막에 두었으므로 흑이 이겼다고 출력해야 함. ( 콘솔상으로 & 화면상으로 )
						System.out.println("White win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("White Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "White Win!");
						count = 1;
						return;
					}

					// stack에 놓은 곳 저장하기.
					int[] position = new int[2];
					position[0] = num / 15;
					position[1] = num % 15;
					Board_PvE.stack.push(position);
					// check
					System.out
							.println("pushed: [" + Board_PvE.stack.peek()[0] + ", " + Board_PvE.stack.peek()[1] + "]");

					// change turn
					Board_PvE.isBlack = true;

					// change computer turn
					Board_PvE.computerTurn = true;
				} // 돌이 놓여있다면, do nothing
			}
			aa = 1;
			System.out.println(aa);
		} else {
			if (Thread.currentThread().getName() == "b") {

				// 다음 AI가 놓는다.
				// AI의 생각 추출.

				int[] move = Board_PvE.AI.think(Board_PvE.matrix);

				// check
				// System.out.println("AI's thinking : [" + move[0] + ", " + move[1] + "]");
				/// ?

				// matrix change

				if (Board_PvE.isBlack == true && Board_PvE.matrix[move[0]][move[1]] == 0) { // 검정돌 차례고 아무것도
					// 놓여있지
					// 않다면,

					Board_PvE.matrix[move[0]][move[1]] += 2; // 검은색이면 +2

					// 흑돌 배치하기.

					Board_PvE.drawStoneBlack(move[0] * 15 + move[1]);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Black Stone at [" + move[0] + ", " + move[1] + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, move[0], move[1], count) == true) { // 게임이
						// 끝났으면,
						// 매트릭스 버튼들 모두 비활성화시켜야하고
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process 비활성화. new game만 눌릴 수 있는 상태로 만들어주어야 한다.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// 흑돌을 마지막에 두었으므로 흑이 이겼다고 출력해야 함. ( 콘솔상으로 & 화면상으로 )
						System.out.println("Black win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("Black Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "Black Win!");
						count = 1;
						return;
					}

					// stack에 놓은 곳 저장하기.
					int[] position = new int[2];
					position[0] = move[0];
					position[1] = move[1];
					Board_PvE.stack.push(position);
					// check
					System.out
							.println("pushed: [" + Board_PvE.stack.peek()[0] + ", " + Board_PvE.stack.peek()[1] + "]");

					// change turn
					Board_PvE.isBlack = false;

					// change computer turn
					Board_PvE.computerTurn = false;
				}

				else if (Board_PvE.isBlack == false && Board_PvE.matrix[move[0]][move[1]] == 0) { // 흰돌 차례고

					// 아무것도
					// 놓여있지
					// 않다면,

					Board_PvE.matrix[move[0]][move[1]] += 1; // 흰색이면 +1
					System.out.println("2");
					// 백돌 배치하기.
					Board_PvE.drawStoneWhite(move[0] * 15 + move[1]);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("White Stone at [" + move[0] + ", " + move[1] + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, move[0], move[1], count) == true) { // 게임이

						// 끝났으면,
						// 매트릭스 버튼들 모두 비활성화시켜야하고
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process 비활성화. new game만 눌릴 수 있는 상태로 만들어주어야 한다.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// 흑돌을 마지막에 두었으므로 흑이 이겼다고 출력해야 함. ( 콘솔상으로 & 화면상으로 )
						System.out.println("White win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("White Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "White Win!");
						count = 1;
						return;
					}

					// stack에 놓은 곳 저장하기.
					int[] position = new int[2];
					position[0] = move[0];
					position[1] = move[1];
					Board_PvE.stack.push(position);
					// check
					System.out
							.println("pushed: [" + Board_PvE.stack.peek()[0] + ", " + Board_PvE.stack.peek()[1] + "]");

					// change turn
					Board_PvE.isBlack = true;

					// change computer turn
					Board_PvE.computerTurn = false;
				}
				// check matrix status
				// for (int i = 0; i < 15; i++) {
				// for (int j = 0; j < 15; j++) {
				// System.out.print(Board_PvE.matrix[i][j] + " ");
				// }
				// System.out.println();
				// }

			}
		}
	}

	public void actionPerformed(ActionEvent e) {

		Thread person = new Thread(this, "a");

		person.start();
		try {
			person.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Thread computer = new Thread(this, "b");
		computer.start();

	}
}