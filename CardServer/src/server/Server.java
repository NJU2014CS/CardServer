package server;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;

import database.UserDatabase;

public class Server extends JFrame{
	private static final long serialVersionUID = 1L;
//	private Object Message;
	private static Object Result;
	private static JTextArea jta = new JTextArea();
//	private List<String> message = new ArrayList<String>();
	
	public Server() {
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		setTitle("Server");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try {
			ServerSocket serverSocket = new ServerSocket(8000);
			jta.append("Server started at " + new Date() + '\n');
			
			int clientNo = 1;
			
			while(true) {
				Socket socket = serverSocket.accept();
				
				jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');
				
				InetAddress inetAddress = socket.getInetAddress();
				jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
				jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + '\n');
				jta.append("Client " + clientNo + "'s socket is " + socket + '\n');
				
				HandleAClient task = new HandleAClient(socket);
				
				new Thread(task).start();
				
				clientNo++;
			}
			
		}
		catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	
	static class HandleAClient implements Runnable {
		private Socket socket;
		String nameOff = "";
		UserDatabase ud = new UserDatabase();
		public HandleAClient(Socket soc) {
			this.socket = soc;
		}
		private void send(Object Message){
			try {
				ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(Message);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	//			errorcode=3;
			}
		}
		
		private void get(){
			try {
			///	System.out.println(Result+ "11");
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
			///	System.out.println(Result+ "22");
				Result=in.readObject();
			///	System.out.println(Result+ " result~~");
			} catch (StreamCorruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Result=null;
			///	System.out.println("1");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Result=null;
				System.out.println("2");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				Result=null;
			///	System.out.println("3");
			}
		}
		public void run() {
			ud.createConnection();

			while(true) {
				get();
			///	System.out.println(Result+ " result");
				//比对样本
				if (Result != null) {
					if(Result.equals("12345"))
						send("ok~~");
					if(Result.equals("123456"))
						send("ok!!");
				//	jta.append("\n");
				}

				
			}
		}
	}
	
	public static void main(String args[]) {
		new Server();
	}
}

