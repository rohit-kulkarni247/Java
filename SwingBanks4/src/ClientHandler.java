import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import java.util.*;
import java.sql.*;

public class ClientHandler implements Runnable {

	Socket client;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Integer ch;
	String choice, y, send, recieve;
	Statement st, st1, st2, st3;
	ResultSet rs, rs1;
	PreparedStatement ps;

	public ClientHandler(Socket clientSocket) throws Exception {
		this.client = clientSocket;
		ois = new ObjectInputStream(this.client.getInputStream());
		oos = new ObjectOutputStream(this.client.getOutputStream());
		st = Server1.con.createStatement();
		st1 = Server1.con.createStatement();
		st2 = Server1.con.createStatement();
		st3 = Server1.con.createStatement();

	}

	public void run() {
		try {
			String s = (String) ois.readObject();

			if (s.equals("Login")) {
				String id = (String) ois.readObject();
				String email = (String) ois.readObject();
				String password1 = (String) ois.readObject();

				String query = "select password from users where user_id=" + id + " and email='" + email + "'";

				try {
					rs = st.executeQuery(query);

					boolean f = false;

					if (rs.next()) {

						if (rs.getString("password").equals(password1)) {
							oos.writeObject("1");

							f = true;
						}
					}
					if (!f) {
						oos.writeObject("0");

					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else if (s.equals("Register")) {
				String name = (String) ois.readObject();
				String email = (String) ois.readObject();
				String pass = (String) ois.readObject();

				String query = "insert into users(username,email,password) values(?,?,?)";
				try {
					ps = Server1.con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
					ps.setString(1, name);
					ps.setString(2, email);
					ps.setString(3, pass);

					try {
						ps.execute();

						rs = ps.getGeneratedKeys();
						if (rs.next()) {

							oos.writeObject("" + rs.getInt(1));
						} else {
							oos.writeObject("-1");
						}
					} catch (Exception e) {
						oos.writeObject("-1");
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					oos.writeObject("-1");
					// e.printStackTrace();
				}

			} else if (s.equals("Admin")) {

				s = (String) ois.readObject();

				if (s.equals("delete")) {
					String id = (String) ois.readObject();

					String query = "delete from customer where cust_id=" + id;

					try {
						st.execute(query);
						oos.writeObject("1");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						oos.writeObject("-1");
					}

				} else if (s.equals("user details")) {

					String query="select * from users";
    				String userdata="";
    				//ArrayList<String> str=new ArrayList<String>();
    				String ans="";
    				ans+="user-id     | username    |   email    |       balance\n\n";
    				rs=st3.executeQuery(query);
    				while(rs.next()) {
							userdata=rs.getInt(1)+"            |        "+rs.getString(2)+ "          |       "+rs.getString(3)+"      |       "+rs.getInt(5);
							//System.out.println(userdata);
							ans+=userdata+"\n\n\n";
							//str.add(userdata);
    				}
    				oos.writeObject(ans);

			

				}
				else if (s.equals("cashback")) {

					String query="select * from cashback";
    				String userdata="";
    				Integer col1,col2;
    				//ArrayList<String> str=new ArrayList<String>();
    				String ans="";
    				ans+="user-id     | amount earned\n\n";
    				rs=st3.executeQuery(query);
    				while(rs.next()) {
    						col1=rs.getInt(1);
    						col2=rs.getInt(2);
							userdata=rs.getInt(1)+"             |        "+rs.getInt(2);
							Integer cashbackdata[]= {col1,col2};
							//DefaultTableModel table1=(DefaultTableModel)jTable1.getModel();
							//System.out.println(userdata);
							ans+=userdata+"\n\n\n";
							//str.add(userdata);
    				}
    				oos.writeObject(ans);

				}
				else if (s.equals("whnpby")) {

					String query = "select cust_id , bill , paybilldate  from bill where flag=0  order by  bill desc";
					String q1 = "select name , mobile_no , emailID , type_ from customer where cust_id=";

					ArrayList<String> arr = new ArrayList<String>();

					rs = st.executeQuery(query);

					while (rs.next()) {

						rs1 = st1.executeQuery(q1 + rs.getInt("cust_id"));
						rs1.next();
						s = "Cust_ID = " + rs.getString("cust_id") + "\n" + "Name = " + rs1.getString("name") + "\n"
								+ "MobileNo = " + rs1.getString("mobile_no") + "\n" + "EmailID = "
								+ rs1.getString("emailID") + "\n" + "Bill Amount = " + rs.getString("bill") + "\n"
								+ "Type = " + rs1.getString("type_") + "\n" + "Date = " + rs.getString("paybilldate")
								+ "\n";

						// System.out.println(s);

						arr.add(s);

					}
					// System.out.println(arr.size());
					oos.writeObject(arr);

				} else {

					String what = (String) ois.readObject();
					if (what.equals("ID")) {
						String id = (String) ois.readObject();

						String q1 = "select * from customer where cust_id=" + id;

						rs = st.executeQuery(q1);

						if (rs.next()) {
							s = "Cust_ID = " + rs.getString("cust_id") + "\n" + "Name = " + rs.getString("name") + "\n"
									+ "MobileNo = " + rs.getString("mobile_no") + "\n" + "EmailID = "
									+ rs.getString("emailID") + "\n" + "Type = " + rs.getString("type_") + "\n"
									+ "Address = " + rs.getString("address") + "\n";

							oos.writeObject(s);

						} else {
							oos.writeObject("-1");
						}

					} else {
						String id = (String) ois.readObject();
						String q2 = "select * from customer where emailID='" + id + "'";

						rs = st.executeQuery(q2);

						if (rs.next()) {
							s = "Cust_ID = " + rs.getString("cust_id") + "\n" + "Name = " + rs.getString("name") + "\n"
									+ "MobileNo = " + rs.getString("mobile_no") + "\n" + "EmailID = "
									+ rs.getString("emailID") + "\n" + "Type = " + rs.getString("type_") + "\n"
									+ "Address = " + rs.getString("address") + "\n";

							oos.writeObject(s);

						} else {
							oos.writeObject("-1");
						}
					}
				}

			} else if (s.equals("customer")) {

				String id = (String) ois.readObject();
				s = (String) ois.readObject();

				if (s.equals("show")) {

					String q1 = "select * from users where user_id=" + id;

					rs = st.executeQuery(q1);

					if (rs.next()) {
						s = "user_ID = " + rs.getString("user_id") + "\n" + "username = " + rs.getString("username")
								+ "\n" + "EmailID = " + rs.getString("email") + "\n";

						oos.writeObject(s);

					} else {
						oos.writeObject("-1");
					}

				}

				else if (s.equals("add money")) {

					String retu = "";

					String name = (String) ois.readObject();
					Integer addTemp = Integer.parseInt(name);

					String q1 = "select balance from users where user_id=" + id;

					rs = st.executeQuery(q1);
					rs.next();
					Integer temp = rs.getInt("balance");
					temp += addTemp;
					String query = "update users set balance=" + temp + " where user_id=" + id;
					st3.executeUpdate(query);
					String msg = "Successfully added rps. " + addTemp;
					msg += "\n\ncurrent balance:  " + temp;
					oos.writeObject(msg);
				} else if (s.equals("withdraw money")) {

					String name = (String) ois.readObject();
					Integer addTemp = Integer.parseInt(name);

					String q1 = "select balance from users where user_id=" + id;

					rs = st.executeQuery(q1);
					rs.next();
					Integer temp = rs.getInt("balance");
					temp -= addTemp;
					String query = "update users set balance=" + temp + " where user_id=" + id;
					st3.executeUpdate(query);
					String msg = "Successfully withdrawn rps. " + addTemp;
					msg += "\n\ncurrent balance:  " + temp;
					oos.writeObject(msg);
				} else if (s.equals("transfer money")) {

					Integer conid = (Integer) ois.readObject();
					Integer amt = (Integer) ois.readObject(); // amount to be transferred

					String query = "select balance from users where user_id=" + conid;
					rs = st1.executeQuery(query);

					if (rs.next()) {
						// oos.writeObject(flag1); //send flag=1 receiver id exists 16
						Integer temp4 = rs.getInt("balance"); // receivers bank balance
						//
						System.out.println(temp4);

						// sendBal=(Integer)ois.readObject(); //amount to be transferred 17
						query = "select balance from users where user_id=" + id;
						rs = st2.executeQuery(query);
						rs.next();
						Integer temp3 = rs.getInt("balance"); // sender's bank balance
						if (amt < temp3) {
							// flag=1;
							// oos.writeObject(flag);
							temp3 -= amt;
							query = "update users set balance=" + temp3 + " where user_id=" + id;
							st3.executeUpdate(query);
							temp4 += amt;
							query = "update users set balance=" + temp4 + " where user_id=" + conid;
							st3.executeUpdate(query);
							query = "select username from users where user_id=" + conid;
							rs = st3.executeQuery(query);
							rs.next();
							String msg = "Money transferred successfully to " + rs.getString("username")
									+ " with User-id " + conid;
							// oos.writeObject(msg); //acknowledgement of transfer 18

							if (amt > 500) {
								// flag=1;
								// oos.writeObject(flag); //send flag=1 for scratch card 19
								// msg+="\nYou have earned a scratch card";
								// oos.writeObject(msg); //scratch card msg 20
								Integer amount = ((int) (Math.random() * 10) + 1);
								msg += "\nCongratulations! You have won Rs. " + amount
										+ " and have been successfully added to your account";
								query = "select balance from users where user_id=" + id;
								rs = st2.executeQuery(query);
								rs.next();
								Integer temp5 = rs.getInt("balance");
								temp5 += amount;
								query = "update users set balance=" + temp5 + " where user_id=" + id;
								st3.executeUpdate(query);

								query = "insert into cashback values(" + id + "," + amount + ")";
								st3.executeUpdate(query);

								oos.writeObject(msg); // cashback amount 21
							} else {
								// flag=0;
								// oos.writeObject(flag);
								msg = "";
								msg += "Money transferred successfully to " + rs.getString("username")
										+ " with User-id " + conid;
								oos.writeObject(msg);
							}

						} else { // sendbal>temp3 cannot transfer amount
									// amount=0;
									// flag=0;
									// oos.writeObject(flag); //22
							String msg = "Amount cannot be greater than bank balance";
							oos.writeObject(msg);

						}
					}

					else {
						// flag=0;
						// oos.writeObject(flag);
						String msg = "Reciever id doesn't exist!";
						oos.writeObject(msg); // 23

					}
				}

				else if (s.equals("pay")) {

					String q = "select * from bill where cust_id=" + id + " and flag=0";
					String r = "";
					rs = st.executeQuery(q);
					rs1 = st1.executeQuery("select type_ from customer where cust_id=" + id);
					rs1.next();
					while (rs.next()) {
						r += "Bill_id = " + rs.getInt("bill_id") + "\n" + "Units = " + rs.getInt("unit") + "\n"
								+ "Bill = " + rs.getInt("bill") + "\n" + "Date = " + rs.getString("paybilldate") + "\n"
								+ "Type = " + rs1.getString("type_") + "\n" + "\n";
					}

					oos.writeObject(r);
					s = (String) ois.readObject();
					if (s.equals("pay1")) {

						System.out.println("i am in pay1");

						String billID = (String) ois.readObject();
						String bill = (String) ois.readObject();

						q = "select * from bill where bill_id=" + billID;

						String reply;

						ResultSet rs2 = st2.executeQuery(q);

						if (rs2.next()) {
							if (rs2.getInt("bill") == Integer.parseInt(bill)) {
								q = "update bill set flag=1 where bill_id=" + billID;
								st.execute(q);
								reply = rs2.getString("paybilldate");

							} else {
								reply = "0";
							}
						} else {
							reply = "-1";
						}

						oos.writeObject(reply);

					}

				}

			}

		} catch (ClassNotFoundException | IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
