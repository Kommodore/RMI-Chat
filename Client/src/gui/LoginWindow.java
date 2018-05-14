package vs_chat.me.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginWindow {

	private JFrame window;

	private JTextField ipAddr, name, password;
	private JLabel l_ipAddr, l_name, l_password;
	private JButton login;

	public LoginWindow() {
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		// Add Components here
		placeComponents();

		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);

	}

	private void placeComponents() {
		GridBagConstraints c = new GridBagConstraints();
		window.setLayout(new GridBagLayout());

		l_ipAddr = new JLabel("IP ADDRESS:");
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(50, 0, 0, 0);
		window.add(l_ipAddr, c);

		ipAddr = new JTextField();
		ipAddr.setPreferredSize(new Dimension(150, 25));
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 30, 0, 30);
		window.add(ipAddr, c);

		l_name = new JLabel("NAME:");
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(50, 0, 0, 0);
		window.add(l_name, c);

		name = new JTextField();
		name.setPreferredSize(new Dimension(150, 25));
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(0, 30, 0, 30);
		window.add(name, c);

		l_password = new JLabel("PASSWORD:");
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(50, 0, 0, 0);
		window.add(l_password, c);

		password = new JTextField();
		password.setPreferredSize(new Dimension(150, 25));
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(0, 30, 0, 30);
		window.add(password, c);

		login = new JButton("Login");
		login.setPreferredSize(new Dimension(75, 25));
		login.setActionCommand("LOGIN");
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(50, 30, 50, 30);
		window.add(login, c);
	}

	public void addLoginAction(ActionListener al) {
		login.addActionListener(al);
	}
	
	public void killWindow() {
		window.dispose();
	}
	
	public String getIpAddr() {
		return ipAddr.getText();
	}
	
	public String getName() {
		return name.getText();
	}
	
	public String getPassword() {
		return password.getText();
	}
	
}
