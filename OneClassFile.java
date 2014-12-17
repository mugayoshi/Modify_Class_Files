import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javassist.*;

public class OneClassFile extends SearchClassFiles {

	public OneClassFile(){
		super();
	}
	public static void main(String[] args){
		System.out.println("Modification for One Class File");
		System.out.println("Enter Package Names (e.g. com.example.a)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		OneClassFile ocf = new OneClassFile();
		
		try {
			String c = br.readLine();
			String[] cs = c.split(" ");
			for(int i = 0; i < cs.length; i++){
				ocf.insertCodesIntoAllMethods(cs[i]);
			}
			System.out.println("DONE !!");
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
