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

public class Board_PvP {

	public static boolean isBlack = true; // turn
	private static int boardSize = 15; // �ӽ�����.
	public static JButton[] button = new JButton[boardSize * boardSize]; // matrix buttons
	public static int[][] matrix = new int[boardSize][boardSize]; // matrix
	public static Stack<int[]> stack = new Stack<>(); // �ڷΰ��⸦ ���� stack
	public static JButton undo; // �ڷΰ��� ��ư
	public static JButton showProcess; // ���μ��� �����ִ� ��ư
	public static ShowProcess show; // process â ���.
	// constructor

	public Board_PvP() {
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
			button[a].addActionListener(new Listener_PvP(a));
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
					if (Board_PvP.show != null) {
						Board_PvP.show.textArea.append("==============================================" + "\n");
					} /// printer

					// �ʱⰪ���� �����ϴ�.

					// 0. stack�ȿ� ���� �ִٸ�,
					// 1. stack�� ����ְ�,
					// 2. matrix 0���� �� �ʱ�ȭ���ְ�
					// 3. turn �浹��(true��) ��ġ���ֱ�.
					// 4. undo, showProcess ��ư Ȱ��ȭ
					// 5. ȭ�� �����
					if (Board_PvP.stack.isEmpty() == false)
						Board_PvP.stack.removeAllElements();
					for (int i = 0; i < 225; i++) {
						Board_PvP.matrix[i / 15][i % 15] = 0;
					}
					Board_PvP.isBlack = true;

					Board_PvP.undo.setEnabled(true);
					Board_PvP.showProcess.setEnabled(true);

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

		// �ڷΰ��� �����ư
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 0-1. stack�ȿ� ���� �ִٸ�,
				// 1. stack���� �� �� ���� ���´�.
				// 2. ���� ���� ���� ��ǥ�� ��ư�̹����� default�� �ٲ۴�.
				// 3. matrix�� ���� 0���� �ٲپ� �ش�.
				// 4. turn�� �ٲ��ش�.

				// 0-2. stack�ȿ� ���� ���ٸ�,
				// return; (nothing to do)

				if (Board_PvP.stack.isEmpty() == false) {
					int[] temp = Board_PvP.stack.pop();
					int num = temp[0] * 15 + temp[1];
					Board_PvP.drawStoneDefault(num);
					Board_PvP.matrix[temp[0]][temp[1]] = 0;
					Board_PvP.isBlack = !isBlack;
					
					// print on showProcess textArea.
					if (Board_PvP.show != null) {
						Board_PvP.show.textArea.append("Undo >> [" + temp[0] + ", " + temp[1] + "]" + "\n");
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

				if (Board_PvP.stack.isEmpty() == false)
					Board_PvP.stack.removeAllElements();
				for (int i = 0; i < 225; i++) {
					Board_PvP.drawStoneDefault(i);
					Board_PvP.matrix[i / 15][i % 15] = 0;
				}
				Board_PvP.isBlack = true;

				for (int i = 0; i < Board_PvP.button.length; i++) {
					Board_PvP.button[i].setEnabled(true);
				}

				Board_PvP.undo.setEnabled(true);
				Board_PvP.showProcess.setEnabled(true);

				// check
				System.out.println("Start new Game.");
				
				// print on showProcess textArea.
				if (Board_PvP.show != null) {
					Board_PvP.show.textArea.append("==================New Game=================" + "\n");
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

		// design menu buttons

		// insert into frame
		MainMenu.getFrame().add(north, BorderLayout.NORTH);
		MainMenu.getFrame().add(east, BorderLayout.EAST);
		MainMenu.getFrame().add(west, BorderLayout.WEST);
		MainMenu.getFrame().add(middlePane, BorderLayout.CENTER);
		MainMenu.getFrame().add(bottomPane, BorderLayout.SOUTH);

	}

	// ���� �� �����ִ� �Լ�
	public static void drawStoneDefault(int num) {
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/rightEdge.png"));
		} else { // ���
			Board_PvP.button[num].setIcon(new ImageIcon("./img/board/middle.png"));
		}
	}

	// ��� �� �����ִ� �Լ�
	public static void drawStoneWhite(int num) {
		// if()Ȱ���Ͽ� �˸��� ���� �ٲ��ֱ�(�� ���)
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/rightEdge.png"));
		} else { // ���
			Board_PvP.button[num].setIcon(new ImageIcon("./img/whiteStone/middle.png"));
		}
	}

	// ������ �� �����ִ� �Լ�
	public static void drawStoneBlack(int num) {
		// if()Ȱ���Ͽ� �˸��� ���� �ٲ��ֱ�(�� ���)
		if (num == 0) {// ���� �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/leftUp.png"));
		} else if (num == 14) {// ������ �� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/rightUp.png"));
		} else if (num == 210) {// ���� �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/leftDown.png"));
		} else if (num == 224) {// ������ �Ʒ� �����ڸ�
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/rightDown.png"));
		} else if (num >= 1 && num <= 13) { // �� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/topEdge.png"));
		} else if (num >= 211 && num <= 224) { // �Ʒ� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/bottomEdge.png"));
		} else if (num % 15 == 0 && num >= 1) { // ���� edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/leftEdge.png"));
		} else if (num % 15 == 14) { // ������ edge
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/rightEdge.png"));
		} else { // ���
			Board_PvP.button[num].setIcon(new ImageIcon("./img/blackStone/middle.png"));
		}
	}

}

class Listener_PvP implements ActionListener {
	private int num;

	public Listener_PvP(int num) {
		this.num = num;
	}

	public void actionPerformed(ActionEvent e) {
		// press check
		System.out.println(num / 15 + ", " + num % 15);

		// matrix change
		if (Board_PvP.isBlack == true && Board_PvP.matrix[num / 15][num % 15] == 0) { // ������ ���ʰ� �ƹ��͵� �������� �ʴٸ�,

			Board_PvP.matrix[num / 15][num % 15] += 2; // �������̸� +2

			// �浹 ��ġ�ϱ�.
			Board_PvP.drawStoneBlack(num);

			// print on showProcess textArea.
			if (Board_PvP.show != null) {
				Board_PvP.show.textArea.append("Black Stone at [" + num / 15 + ", " + num % 15 + "]\n");
			} /// printer

			// check game over
			if (CheckGameOver_PvP.checkGameOver(Board_PvP.matrix, num / 15, num % 15) == true) { // ������ ��������,
				// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
				for (int i = 0; i < Board_PvP.button.length; i++) {
					Board_PvP.button[i].setEnabled(false);
				}
				// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
				Board_PvP.undo.setEnabled(false);
				Board_PvP.showProcess.setEnabled(false);
				// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
				System.out.println("Black win");

				// print on showProcess textArea.
				if (Board_PvP.show != null) {
					Board_PvP.show.textArea.append("Black Win!" + "\n");
				} /// printer

				JOptionPane.showMessageDialog(MainMenu.getFrame(), "Black Win!");

			}

			// stack�� ���� �� �����ϱ�.
			int[] position = new int[2];
			position[0] = num / 15;
			position[1] = num % 15;
			Board_PvP.stack.push(position);
			// check
			System.out
					.println("Stack >> pushed: [" + Board_PvP.stack.peek()[0] + ", " + Board_PvP.stack.peek()[1] + "]");

			// change turn
			Board_PvP.isBlack = false;
		}

		else if (Board_PvP.isBlack == false && Board_PvP.matrix[num / 15][num % 15] == 0) { // �� ���ʰ� �ƹ��͵� �������� �ʴٸ�,

			Board_PvP.matrix[num / 15][num % 15] += 1; // ����̸� +1

			// �鵹 ��ġ�ϱ�.
			Board_PvP.drawStoneWhite(num);

			// print on showProcess textArea.
			if (Board_PvP.show != null) {
				Board_PvP.show.textArea.append("White Stone at [" + num / 15 + ", " + num % 15 + "]\n");
			} /// printer

			// check game over
			if (CheckGameOver_PvP.checkGameOver(Board_PvP.matrix, num / 15, num % 15) == true) { // ������ ��������,
				// ��Ʈ���� ��ư�� ��� ��Ȱ��ȭ���Ѿ��ϰ�
				for (int i = 0; i < Board_PvP.button.length; i++) {
					Board_PvP.button[i].setEnabled(false);
				}
				// undo, show process ��Ȱ��ȭ. new game�� ���� �� �ִ� ���·� ������־�� �Ѵ�.
				Board_PvP.undo.setEnabled(false);
				Board_PvP.showProcess.setEnabled(false);
				// �浹�� �������� �ξ����Ƿ� ���� �̰�ٰ� ����ؾ� ��. ( �ֻܼ����� & ȭ������� )
				System.out.println("White win");

				// print on showProcess textArea.
				if (Board_PvP.show != null) {
					Board_PvP.show.textArea.append("White Win!" + "\n");
				} /// printer

				JOptionPane.showMessageDialog(MainMenu.getFrame(), "White Win!");
			}

			// stack�� ���� �� �����ϱ�.
			int[] position = new int[2];
			position[0] = num / 15;
			position[1] = num % 15;
			Board_PvP.stack.push(position);
			// check
			System.out
					.println("Stack >> pushed: [" + Board_PvP.stack.peek()[0] + ", " + Board_PvP.stack.peek()[1] + "]");

			Board_PvP.isBlack = true;
		} else
			return; // ���� �����ִٸ�, do nothing

		// check matrix status
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(Board_PvP.matrix[i][j] + " ");
			}
			System.out.println();
		}

	}

}
