package omok;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class SelectUser {
	public SelectUser() {
		//JButton button_1 = new JButton("Human");
		//JButton button_2 = new JButton("Computer");
		JButton button_1 = new JButton(new ImageIcon("./img/select/SelectUserHuman.png"));
		JButton button_2 = new JButton(new ImageIcon("./img/select/SelectUserComputer.png"));
		
		// space bar 비활성화
		button_1.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");
		button_2.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button의 윤곽선 없애기, 선택되었을 때 윤곽선 없애기
		button_1.setBorderPainted(false);
		button_1.setFocusPainted(false);
		button_2.setBorderPainted(false);
		button_2.setFocusPainted(false);
		
		//button event 제작.
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check
				System.out.println("Human selected.");
				
				//input "user state" into seq[0]
				MainMenu.seq[0] = "Person vs Person";
				
				// 화면 지우기
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();
				
				//game start
				new Board_PvP();
			}
		});
		
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//check
				System.out.println("Computer selected.");
				
				//input "user state" into seq[0]
				MainMenu.seq[0] = "Person vs Computer";
				
				// 화면 지우기
				MainMenu.getContainer().removeAll();
				MainMenu.getContainer().revalidate();
				MainMenu.getContainer().repaint();
				
				//select turn 시작.
				new SelectTurn();
			}
		});
		
		//problem >> back 버튼은 어떻게 할 것인가??
		//make panel
		JPanel middlePane = new JPanel();
		
		//add buttons
		middlePane.setLayout(new GridLayout());
		middlePane.add(button_1);
		middlePane.add(button_2);
		
		//add into container
		MainMenu.getContainer().add(middlePane, BorderLayout.CENTER);
	}
}
