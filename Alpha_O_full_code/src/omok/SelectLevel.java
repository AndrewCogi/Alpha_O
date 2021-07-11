package omok;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class SelectLevel {
	// constructor
	public SelectLevel() {
		//JButton button_1 = new JButton("Easy");
		//JButton button_2 = new JButton("Hard");
		JButton button_1 = new JButton(new ImageIcon("./img/select/SelectLevelEasy.png"));
		JButton button_2 = new JButton(new ImageIcon("./img/select/SelectLevelHard.png"));

		// space bar ��Ȱ��ȭ
		button_1.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		button_2.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button�� ������ ���ֱ�, ���õǾ��� �� ������ ���ֱ�
		button_1.setBorderPainted(false);
		button_1.setFocusPainted(false);
		button_2.setBorderPainted(false);
		button_2.setFocusPainted(false);

		// button event ����.
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// easy ����
				MainMenu.seq[2] = "Easy";
				// check
				System.out.println("Easy selected.");

				// ȭ�� �����
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();

				// game start.
				new Board_PvE();
			}
		});

		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// hard ����
				MainMenu.seq[2] = "Hard";
				// check
				System.out.println("Hard selected.");

				// ȭ�� �����
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();

				// game start.
				new Board_PvE();
			}
		});

		// make panel
		JPanel middlePane = new JPanel();

		// add buttons
		middlePane.setLayout(new GridLayout());
		middlePane.add(button_1);
		middlePane.add(button_2);

		// add into container
		MainMenu.getContainer().add(middlePane, BorderLayout.CENTER);
	}
}
