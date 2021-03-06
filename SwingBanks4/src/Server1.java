	import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

public class Server1 {
	public static Connection con;

	public static void main(String[] arg) throws Exception {
		ServerSocket ss = new ServerSocket(11111);
		JOptionPane.showMessageDialog(null, "Server Waiting");
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdatabase", "root", "qwerty");

		ExecutorService pool = Executors.newFixedThreadPool(5);
		while (true) {
			Socket client = ss.accept();
			// JOptionPane.showMessageDialog(null, "Server is connected to client");
			System.out.println("Server is connected to client");
			ClientHandler clientThread = new ClientHandler(client);

			pool.execute(clientThread);
		}

	}
}
