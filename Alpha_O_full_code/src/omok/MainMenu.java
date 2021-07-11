package omok;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainMenu {
	// 선택한 사항들 저장.
	public static String[] seq = new String[] { "null", "null", "null" }; // 각각 user, turn, level
	public static JFrame frame; // frame
	private static Container container; // container

	// constructor
	public MainMenu() {
		// make frame
		frame = new JFrame("O_Mok");

		// JButton button = new JButton("Main_Menu(Press Enter to start)");
		JButton button = new JButton(new ImageIcon("./img/select/MainMenu.png"));

		// space bar 비활성화
		button.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button listener
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check
				System.out.println("Enter Pressed(Clicked).");

				// 화면 지우기
				container.removeAll();
				container.revalidate();
				container.repaint();

				// select user창 시작.
				new SelectUser();
			}
		});

		// enter를 위한 JRootPane 생성. enter 시, button이 눌리도록 설정.
		JRootPane rootPane = frame.getRootPane();
		rootPane.setDefaultButton(button);

		// button의 윤곽선 없애기, 선택되었을 때 윤곽선 없애기
		button.setBorderPainted(false);
		button.setFocusPainted(false);

		// container 생성. button 가운데에 추가.
		container = frame.getContentPane();
		container.add(button, BorderLayout.CENTER);

		// 창 크기 600,600으로 고정. (창 크기 변경 불가)
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setVisible(true);

		// 종료 시 프로세스도 종료
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

		// space bar 비활성화
		button.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "none");

		// button listener
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check
				System.out.println("Enter Pressed(Clicked).");
				// 화면 지우기
				container.removeAll();
				container.revalidate();
				container.repaint();

				// select user창 시작.
				new SelectUser();
			}
		});

		// enter를 위한 JRootPane 생성. enter 시, button이 눌리도록 설정.
		JRootPane rootPane = frame.getRootPane();
		rootPane.setDefaultButton(button);

		// button의 윤곽선 없애기, 선택되었을 때 윤곽선 없애기
		button.setBorderPainted(false);
		button.setFocusPainted(false);

		// container 생성. button 가운데에 추가.
		container = frame.getContentPane();
		container.add(button, BorderLayout.CENTER);

		// 창 크기 600,600으로 고정. (창 크기 변경 불가)
		frame.setSize(600, 600);
		frame.setResizable(false);
		frame.setVisible(true);

		// 종료 시 프로세스도 종료
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

}
