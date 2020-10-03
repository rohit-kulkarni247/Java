
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.sql.*;

public class Server {
	
	public static Connection con;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(7041);
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdatabase", "root", "qwerty");
		
		 ExecutorService pool = Executors.newFixedThreadPool(5);
	        while (true) {
	            System.out.println("Server Waiting");
	            Socket socket = ss.accept();
	            System.out.println("Server is connected to client");
	            HandleClient clientThread = new HandleClient(socket);
	            //clients.add(clientThread);
	            pool.execute(clientThread);
	        }
	}

}
