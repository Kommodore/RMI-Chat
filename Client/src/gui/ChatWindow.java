package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ChatWindow{

	private JFrame window;
	
	private JTextArea chat;
	private JTextField msg;
	
	public ChatWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		//Add Components here
		placeComponents();
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
	}

	private void placeComponents() {
		GridBagConstraints c = new GridBagConstraints();
		window.setLayout(new GridBagLayout());
		
		chat = new JTextArea();
		chat.setPreferredSize(new Dimension(600, 600));
		chat.setEditable(false);
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 0, 5);
		window.add(chat, c);
		
		msg = new JTextField();
		msg.setPreferredSize(new Dimension(600, 25));
		msg.setActionCommand("SETMSG");
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		window.add(msg, c);
	}

	public void addChatAction(ActionListener al) {
		msg.addActionListener(al);
	}
	
	public String getChatMsg() {
		String m = msg.getText();
		msg.setText("");
		return m;
	}

	public void addChatMsg(String username, String m) {
		chat.setText(chat.getText() + "<" + username + ">: " + m + "\n");
	}
}
