package omok;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainMenu {
	// ������ ���׵� ����.
	public static String[] seq = new String[] { "null", "null", "null" }; // ���� user, turn, level
	public static JFrame frame; // frame
	private static Container container; // container

	// constructor
	public MainMenu() {
		// make frame
		frame = new JFrame("O_Mok");

		// JButton button = new JButton("Main_Menu(Press Enter to start)");
		JButton button = new JButton(new ImageIcon("./img/select/MainMenu.png"));

		// space bar ��Ȱ��ȭ
		button.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button listener
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check
				System.out.println("Enter Pressed(Clicked).");

				// ȭ�� �����
				container.removeAll();
				container.revalidate();
				container.repaint();

				// select userâ ����.
				new SelectUser();
			}
		});

		// enter�� ���� JRootPane ����. enter ��, button�� �������� ����.
		JRootPane rootPane = frame.getRootPane();
		rootPane.setDefaultButton(button);

		// button�� ������ ���ֱ�, ���õǾ��� �� ������ ���ֱ�
		button.setBorderPainted(false);
		button.setFocusPainted(false);

		// container ����. button ����� �߰�.
		container = frame.getContentPane();
		container.add(button, BorderLayout.CENTER);

		// â ũ�� 600,600���� ����. (â ũ�� ���� �Ұ�)
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setVisible(true);

		// ���� �� ���μ����� ����
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	// get frame
	public static JFrame getFrame() {
		return frame;
	}

	// get container
	public static Container getContainer() {
		return container;
	}

	public static void MakeNewMain() {
		// seq init
		seq[0] = "null";
		seq[1] = "null";
		seq[2] = "null";

		// JButton button = new JButton("Main_Menu(Press Enter to start)");
		JButton button = new JButton(new ImageIcon("./img/select/MainMenu.png"));

		// space bar ��Ȱ��ȭ
		button.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button listener
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check
				System.out.println("Enter Pressed(Clicked).");
				// ȭ�� �����
				container.removeAll();
				container.revalidate();
				container.repaint();

				// select userâ ����.
				new SelectUser();
			}
		});

		// enter�� ���� JRootPane ����. enter ��, button�� �������� ����.
		JRootPane rootPane = frame.getRootPane();
		rootPane.setDefaultButton(button);

		// button�� ������ ���ֱ�, ���õǾ��� �� ������ ���ֱ�
		button.setBorderPainted(false);
		button.setFocusPainted(false);

		// container ����. button ����� �߰�.
		container = frame.getContentPane();
		container.add(button, BorderLayout.CENTER);

		// â ũ�� 600,600���� ����. (â ũ�� ���� �Ұ�)
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setVisible(true);

		// ���� �� ���μ����� ����
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

}
