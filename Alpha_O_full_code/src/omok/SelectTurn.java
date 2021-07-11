package omok;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class SelectTurn {
	// constructor
	public SelectTurn() {
		//JButton button_1 = new JButton("Black");
		//JButton button_2 = new JButton("White");
		JButton button_1 = new JButton(new ImageIcon("./img/select/SelectTurnBlack.png"));
		JButton button_2 = new JButton(new ImageIcon("./img/select/SelectTurnWhite.png"));

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
				// black ����
				MainMenu.seq[1] = "Black";
				//check
				System.out.println("Black selected.");

				// ȭ�� �����
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();

				// select level ����.
				new SelectLevel();
			}
		});

		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// white ����
				MainMenu.seq[1] = "White";
				//check
				System.out.println("White selected.");

				// ȭ�� �����
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();

				// select level ����.
				new SelectLevel();
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
