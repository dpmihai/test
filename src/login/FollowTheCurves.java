package login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FollowTheCurves extends JFrame implements ActionListener {
	private CurvesPanel contentPane;

	public FollowTheCurves() {
		contentPane = new CurvesPanel();
		getContentPane().add(BorderLayout.CENTER, contentPane);

		JLabel welcome = new JLabel(UIHelper.readImageIcon("welcome.png"));
		contentPane.setLayout(new BorderLayout());
		contentPane.add(BorderLayout.CENTER, welcome);

		Timer animation = new Timer(50, this);
		animation.start();

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setSize(new Dimension(400, 300));
		setLocationRelativeTo(null);
	}
	
	public void actionPerformed(ActionEvent e) {
		contentPane.repaint();
	}

	public String getTitle() {
		return "Follow The Curves";
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				FollowTheCurves frame = new FollowTheCurves();
				frame.setVisible(true);				
			}
		});
	}
}
