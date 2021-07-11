package omok;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShowProcess {
	public TextArea textArea = new TextArea();

	// constructor
	public ShowProcess() {
		JFrame frame = new JFrame("Process");
		JPanel panel = new JPanel();
		panel.add(textArea);
		
		// 한 칸 꽉차게 만들기.
		panel.setLayout(new GridLayout(1, 1));
		frame.add(panel);
		//수정 불가로 변경.
		textArea.setEditable(false);
		//font 변경
		textArea.setFont(new Font("GOTHIC",Font.BOLD, 15));
		
		// print on basic text.
		textArea.append("\t\t  " + "<< Version 1.0 >>" + "\n");
		textArea.append("\t" + "Made into an algorithm team project" + "\n");
		textArea.append("      " + "Made By HyunSik / JoonHyuk / HyunMin" + "\n");
		textArea.append("==============================================" + "\n");
		textArea.append("\t\t     " + "[ Game Info ]" + "\n");
		textArea.append("   Game Mode      >> " + MainMenu.seq[0] + "\n");
		textArea.append("   User's turn       >> " + MainMenu.seq[1] + "\n");
		textArea.append("   Difficulty           >> " + MainMenu.seq[2] + "\n");
		textArea.append("==============================================" + "\n");
		
		frame.setSize(400, 500);
		frame.setResizable(false);
		frame.setVisible(true);

	}

}
