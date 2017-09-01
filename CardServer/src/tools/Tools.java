package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

public class Tools {
	private static Segment seg=HanLP.newSegment();
	private static String MailReg=".*?([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}.*?";
	private static String WebAddrReg=".*?([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+.*?";
	private static String PhoneReg=".*?((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}.*?";
	
	public static void saveImg(byte[] data, String ImgAddr){
		File file=new File(ImgAddr);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(data, 0, data.length);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int GetType(byte[] result){
		int res=(int)((result[0]&0xff)|((result[1]<<8)&0xff)|((result[2]<<16)&0xff)|((result[3]<<24)&0xff));
		return res;
	}
	
	public static byte[] GetInfo(byte[] result){
		int len=result.length-4;
		byte[] res=new byte[len];
		for(int i=0;i<len;i++){
			res[i]=result[i+4];
		}
		return res;
	}
	
	public static String Seg(String[] info){
		HashMap<String,String> map=new HashMap<>();
		for(int i=0;i<info.length;i++){
			info[i]=info[i].replaceAll(" ", "");
			System.out.println(info[i]);
			List<Term> list=seg.seg(info[i]);
			System.out.println(list);
			if(list.size()==0)
				continue;
			String naturename=list.get(list.size()-1).nature.name();
			if(info[i].contains("公司")||naturename.contains("ni")||naturename.contains("nt")){
				map.put("company", info[i].substring(info[i].indexOf(":")+1));
			}
			else if(info[i].contains("地址")){
				map.put("address", info[i].substring(info[i].indexOf(":")+1));
			}
			else if(info[i].contains("邮箱")||info[i].matches(MailReg)){
				map.put("mail", info[i].substring(info[i].indexOf(":")+1));
			}
			else if(info[i].contains("网址")||info[i].matches(WebAddrReg)){
				map.put("note", info[i].substring(info[i].indexOf(":")+1));
			}
			else if(info[i].matches(PhoneReg)){
				map.put("mobilephone", info[i]);
			}
			else if(naturename.contains("nr")){
				map.put("name", info[i]);
			}
			else if(info[i].contains("传真")){
				map.put("fax", info[i].substring(info[i].indexOf(":")+1));
			}
		}
		String res="";
		Set<String> key=map.keySet();
		for(String k:key){
			if(map.get(k)!=null){
				String temp=map.get(k);
				temp=temp.replaceAll("\\s*", "");
				res+=k+":"+temp+" ";
			}
		}
		System.out.println(res);
		return res;
	}
}
