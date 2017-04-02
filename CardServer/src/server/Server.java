package server;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import recognition.NativeFunction;
import tools.Tools;

public class Server extends JFrame{
	private static final long serialVersionUID = 1L;
//	private Object Message;
	private static Object Result;
	private static JTextArea jta = new JTextArea();
//	private List<String> message = new ArrayList<String>();
	private ServerSocket serverSocket;
	
	public Server() {
		setLayout(new BorderLayout());
		jta.setEditable(false);
		add(new JScrollPane(jta), BorderLayout.CENTER);
		setTitle("Server");
		setSize(500, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//加载动态库
		System.loadLibrary("TestJNI");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//获取当前文件夹
		File directory=new File("");
		NativeFunction.Initial(directory.getAbsolutePath()+"\\logo\\");
		
		try {
			serverSocket = new ServerSocket(8080);
			jta.append("Server started at " + new Date() + '\n');
			
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void ServiceStart(){
		
		int clientNo = 1;
		
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				
				jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');
				InetAddress inetAddress = socket.getInetAddress();
				jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
				jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + '\n');
				jta.append("Client " + clientNo + "'s socket is " + socket + '\n');
				
				HandleAClient task = new HandleAClient(socket);
				new Thread(task).start();
				
				clientNo++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	static class HandleAClient implements Runnable {
		
		private Socket socket;
		private final String SavedImgAddr="temp.bmp";
		//UserDatabase ud = new UserDatabase();
		
		public HandleAClient(Socket socket) {
			this.socket = socket;
		}
		
		private void send(Serializable Message){
			try {
				ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(Message);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void get(){
			try {
				ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
				Result=in.readObject();
			} catch (Exception e) {
				Result=null;
			}
		}
		
		//短连接，服务一结束便关闭socket
		@Override
		public void run() {
			//ud.createConnection();

			get();
			
			if(Result!=null){
				Tools.saveImg((byte[])Result, SavedImgAddr);
				Mat mat=Imgcodecs.imread(SavedImgAddr);
				String res=NativeFunction.RecognitionLogo(mat.getNativeObjAddr());
				send(res);
			}
			else{
				send("Didn't get your infomation!");
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		Server s=new Server();
		s.ServiceStart();
	}
}

