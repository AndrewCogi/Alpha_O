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
	private static int boardSize = 15; // �ӽ�����.
	public static JButton[] button = new JButton[boardSize * boardSize]; // matrix buttons
	public static int[][] matrix = new int[boardSize][boardSize]; // matrix
	public static Stack<int[]> stack = new Stack<>(); // �ڷΰ��⸦ ���� stack
	public static JButton undo; // �ڷΰ��� ��ư
	public static JButton showProcess; // ���μ��� �����ִ� ��ư
	public static ShowProcess show; // process â ���.
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
			if (i == 0) {// ���� �� �����ڸ�
				button[i] = new JButton(new ImageIcon("./img/board/leftUp.png"));
			} else if (i == 14) {// ������ �� �����ڸ�
				button[i] = new JButton(new ImageIcon("./img/board/rightUp.png"));
			} else if (i == 210) {// ���� �Ʒ� �����ڸ�
				button[i] = new JButton(new ImageIcon("./img/board/leftDown.png"));
			} else if (i == 224) {// ������ �Ʒ� �����ڸ�
				button[i] = new JButton(new ImageIcon("./img/board/rightDown.png"));
			} else if (i >= 1 && i <= 13) { // �� edge
				button[i] = new JButton(new ImageIcon("./img/board/topEdge.png"));
			} else if (i >= 211 && i <= 224) { // �Ʒ� edge
				button[i] = new JButton(new ImageIcon("./img/board/bottomEdge.png"));
			} else if (i % 15 == 0 && i >= 1) { // ���� edge
				button[i] = new JButton(new ImageIcon("./img/board/leftEdge.png"));
			} else if (i % 15 == 14) { // ������ edge
				button[i] = new JButton(new ImageIcon("./img/board/rightEdge.png"));
			} else { // ���
				button[i] = new JButton(new ImageIcon("./img/board/middle.png"));
			}

			// button�� ������ ���ֱ�, ���õǾ��� �� ������ ���ֱ�
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
				// yes : ����.
				if (result == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				// no : �����ϰ� �״�� �д�.
				// �׳� â�� ���� ��� : ��������.
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
				// yes : �ϴ��� �� ���ְ� (�ʱⰪ���� �ΰ�) ���θ޴��� ������.
				if (result == JOptionPane.YES_OPTION) {
					// system�� Ȯ��
					System.out.println("GO TO MAINMENU");

					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("==============================================" + "\n");
					} /// printer

					// �ʱⰪ���� �����ϴ�.

					// 0. stack�ȿ� ���� �ִٸ�,
					// 1. stack�� ����ְ�,
					// 2. matrix 0���� �� �ʱ�ȭ���ְ�
					// 3. turn �浹��(true��) ��ġ���ֱ�.
					// 4. undo, showProcess ��ư Ȱ��ȭ
					// 5. ȭ�� �����
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

					// ���ο� ����.
					MainMenu.MakeNewMain();
				}
				// no : �׳� �����ϰ� ��� ����.
				// �׳� â�� ���� ��� : �����ϰ� ��� ����.
				else {
					return;
				}
			}
		});

		// �ڷΰ��� �����ư (PvE�� �ι� ���ݴϴ�.)
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0-1. stack�ȿ� ���� �ִٸ�,
				// 1. stack���� �� �� ���� ���´�.
				// 2. ���� ���� ���� ��ǥ�� ��ư�̹����� default�� �ٲ۴�.
				// 3. matrix�� ���� 0���� �ٲپ� �ش�.
				// 4. turn�� �ٲ��ش�.

				// 0-2. stack�ȿ� ���� ���ٸ�,
				// return; (nothing to do)

				if (Board_PvE.stack.isEmpty() == false) {
					// ����,
					int[] temp1 = Board_PvE.stack.pop();
					int num1 = temp1[0] * 15 + temp1[1];
					Board_PvE.drawStoneDefault(num1);
					Board_PvE.matrix[temp1[0]][temp1[1]] = 0;
					Board_PvE.isBlack = !isBlack;

					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Undo >> [" + temp1[0] + ", " + temp1[1] + "]" + "\n");
					} /// printer

					// 1�� �� ���ϴ�.
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

		// ���ο� ���� �����ư
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0. stack�ȿ� ���� �ִٸ�,
				// 1. stack�� ����ְ�,
				// 2. ������ �� �� �����.
				// 3. matrix 0���� �� �ʱ�ȭ���ְ�
				// 4. turn �浹��(true��) ��ġ���ֱ�.
				// 5. ��Ʈ���� ��ư�� ��� Ȱ��ȭ
				// 6. undo, showProcess ��ư Ȱ��ȭ
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

				// ù ���� ����.(turn = white�϶��� ����)
				if (MainMenu.seq[1].equalsIgnoreCase("white")) {
					// ([7,7]�� ��, num = 112)
					int num = 112;
					// matrix �߰��� ([7,7]��) ���� �� ����.
					Board_PvE.matrix[num / 15][num % 15] += 2;

					// GUI�󿡼��� �浹 ��ġ�ϱ�
					Board_PvE.drawStoneBlack(num);

					// stack�� ���� �� �����ϱ�. (���� ���ص� ���� �ʳ���?)
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

		// ���μ��� �����ִ� ��ư
		showProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				show = new ShowProcess(); // �� â ����
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

		// AI�κ�����, �߰��� �κ��Դϴ�. (new game button���� �߰��Ǿ����ϴ�.)
		// computer turn�̶��, (ù ����)
		if (Board_PvE.computerTurn == true) {
			// ([7,7]�� ��, num = 112)
			int num = 112;
			// matrix �߰��� ([7,7]��) ���� �� ����.
			Board_PvE.matrix[num / 15][num % 15] += 2;

			// GUI�󿡼��� �浹 ��ġ�ϱ�
			Board_PvE.drawStoneBlack(num);

			// stack�� ���� �� �����ϱ�. (���� ���ص� ���� �ʳ���?)
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

	// ���� �� �����ִ� �Լ�
	public static void drawStoneDefault(int num) {
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/rightEdge.png"));
		} else { // ���
			Board_PvE.button[num].setIcon(new ImageIcon("./img/board/middle.png"));
		}
	}

	// ��� �� �����ִ� �Լ�
	public static void drawStoneWhite(int num) {
		// if()Ȱ���Ͽ� �˸��� ���� �ٲ��ֱ�(�� ���)
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/rightEdge.png"));
		} else { // ���
			Board_PvE.button[num].setIcon(new ImageIcon("./img/whiteStone/middle.png"));
		}
	}

	// ������ �� �����ִ� �Լ�
	public static void drawStoneBlack(int num) {
		// if()Ȱ���Ͽ� �˸��� ���� �ٲ��ֱ�(�� ���)
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvE.button[num].setIcon(new ImageIcon("./img/blackStone/rightEdge.png"));
		} else { // ���
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
		// computer turn�� �ƴ϶��, (�翬�� �ƴϾ���մϴ�. �ȵ��ư��� ���⿡ �����ִ°�..)
		if (Board_PvE.computerTurn == false) {
			// ���� ����� ���´�.
			// matrix change
			if (Thread.currentThread().getName() == "a") {
				if (Board_PvE.isBlack == true && Board_PvE.matrix[num / 15][num % 15] == 0) { // ������ ���ʰ� �ƹ��͵�
					// ��������
					// �ʴٸ�,

					Board_PvE.matrix[num / 15][num % 15] += 2; // �������̸� +2

					// �浹 ��ġ�ϱ�.
					Board_PvE.drawStoneBlack(num);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Black Stone at [" + num / 15 + ", " + num % 15 + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, num / 15, num % 15, count) == true) { // ������
						// ��������,
						// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
						System.out.println("Black win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("Black Win!" + "\n");
						} /// printer

						JOptionPane.showMessageDialog(MainMenu.getFrame(), "Black Win!");
						count = 1;
						return;

					}

					// stack�� ���� �� �����ϱ�.
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

				else if (Board_PvE.isBlack == false && Board_PvE.matrix[num / 15][num % 15] == 0) { // �� ���ʰ�
					// �ƹ��͵�
					// ��������
					// �ʴٸ�,

					Board_PvE.matrix[num / 15][num % 15] += 1; // ����̸� +1

					// �鵹 ��ġ�ϱ�.

					Board_PvE.drawStoneWhite(num);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("White Stone at [" + num / 15 + ", " + num % 15 + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, num / 15, num % 15, count) == true) { // ������
						// ��������,
						// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
						System.out.println("White win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("White Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "White Win!");
						count = 1;
						return;
					}

					// stack�� ���� �� �����ϱ�.
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
				} // ���� �����ִٸ�, do nothing
			}
			aa = 1;
			System.out.println(aa);
		} else {
			if (Thread.currentThread().getName() == "b") {

				// ���� AI�� ���´�.
				// AI�� ���� ����.

				int[] move = Board_PvE.AI.think(Board_PvE.matrix);

				// check
				// System.out.println("AI's thinking : [" + move[0] + ", " + move[1] + "]");
				/// ?

				// matrix change

				if (Board_PvE.isBlack == true && Board_PvE.matrix[move[0]][move[1]] == 0) { // ������ ���ʰ� �ƹ��͵�
					// ��������
					// �ʴٸ�,

					Board_PvE.matrix[move[0]][move[1]] += 2; // �������̸� +2

					// �浹 ��ġ�ϱ�.

					Board_PvE.drawStoneBlack(move[0] * 15 + move[1]);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("Black Stone at [" + move[0] + ", " + move[1] + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, move[0], move[1], count) == true) { // ������
						// ��������,
						// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
						System.out.println("Black win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("Black Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "Black Win!");
						count = 1;
						return;
					}

					// stack�� ���� �� �����ϱ�.
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

				else if (Board_PvE.isBlack == false && Board_PvE.matrix[move[0]][move[1]] == 0) { // �� ���ʰ�

					// �ƹ��͵�
					// ��������
					// �ʴٸ�,

					Board_PvE.matrix[move[0]][move[1]] += 1; // ����̸� +1
					System.out.println("2");
					// �鵹 ��ġ�ϱ�.
					Board_PvE.drawStoneWhite(move[0] * 15 + move[1]);
					// print on showProcess textArea.
					if (Board_PvE.show != null) {
						Board_PvE.show.textArea.append("White Stone at [" + move[0] + ", " + move[1] + "]\n");
					} /// printer

					// check game over
					if (CheckGameOver_PvE.checkGameOver(Board_PvE.matrix, move[0], move[1], count) == true) { // ������

						// ��������,
						// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
						for (int i = 0; i < Board_PvE.button.length; i++) {
							Board_PvE.button[i].setEnabled(false);
						}
						// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
						Board_PvE.undo.setEnabled(false);
						Board_PvE.showProcess.setEnabled(false);
						// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
						System.out.println("White win");
						// print on showProcess textArea.
						if (Board_PvE.show != null) {
							Board_PvE.show.textArea.append("White Win!" + "\n");
						} /// printer
						JOptionPane.showMessageDialog(MainMenu.getFrame(), "White Win!");
						count = 1;
						return;
					}

					// stack�� ���� �� �����ϱ�.
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