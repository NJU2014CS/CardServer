package recognition;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

public class ClassifyInfo {
	private static String Directory="E:/创新项目/646864264/";
	
	public void SendData(String s) throws IOException{
		File fout=new File(Directory+"predict.csv");
		BufferedWriter writer=new BufferedWriter(new FileWriter(fout));
		Segment seg=HanLP.newSegment();
		List<Term> list=seg.seg(s);
		String str="";
		for(Term l:list){
			str+=l.nature.name()+',';
		}
		writer.write(str);
		writer.close();
	}
	
	@SuppressWarnings("null")
	public int GetResult() throws IOException{
		File in=new File(Directory+"result.txt");
		int res=-1;
		if(in.exists()){
			BufferedReader reader=new BufferedReader(new FileReader(in));
			String line=reader.readLine();
			if(line!=null || line.length()!=0){
				res=Integer.valueOf(line);
			}
			reader.close();
			in.delete();
		}
		return res;
	}
}
