import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.UnknownHostException;


//调用该模块时，需要判断线程是否在运行，运行完毕后才可读取返回结果！

public class testclient implements Runnable{
	private static Socket socket;
	private static String ip="localhost";
	private static Object Message;
	private static Object Result;
	
	/*
	 *errorcode对应的错误 
	 *-1		无错误
	 *0			错误的ip地址
	 *1			无法连接服务器
	 *2			连接超时
	 *3			与服务器连接断开
	 */
	private static int errorcode=-1;
	
	public testclient(Serializable Message){
		this.Message=Message;
		System.out.println(this.Message.toString());
		Result=null;
		try {
			socket=new Socket(ip,8000);
		///	System.out.println(socket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
			errorcode=0;
	///		System.out.println(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			errorcode=1;
	///		System.out.println(socket);
		}
	}
	
	private static void send(Object Message){
		try {
		///	System.out.println(Message);
			ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(Message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			errorcode=3;
		}
	}
	
	private static void get(){
		try {
			ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
			Result=in.readObject();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Result=null;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(errorcode==-1){
			send(Message);
			long starttime=System.currentTimeMillis();
			while(true){
				get();
				if(Result!=null){
					//deal with the Result received here
					
					System.out.println(Result.toString());
					break;
				}
				
				if(System.currentTimeMillis()-starttime>=5000){
					//overtime and exit
					errorcode=2;
					break;
				}
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get the result
	public Object getResult() {
		return Result;
	}
	
	//get the errorcode
	public int getErrorCode(){
		return errorcode;
	}
	
	//test main
	public static void main(String args[]) {
		String str = "12345";
		String str1 = "123456";
		testclient sm = new testclient(str);
		sm.run();
		testclient sm1 = new testclient(str1);
		sm1.run();
		
	}
}
