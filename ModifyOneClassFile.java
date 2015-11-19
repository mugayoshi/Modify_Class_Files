import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javassist.*;

public class ModifyOneClassFile extends SearchClassFiles {

	public ModifyOneClassFile(){
		super();
	}
	public static void main(String[] args){
		System.out.println("Modification for One Class File");
		System.out.println("Enter Package Names (e.g. com.example.a)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ModifyOneClassFile ocf = new ModifyOneClassFile();
		
		try {
			String c = br.readLine();
			String[] cs = c.split(" ");
			for(int i = 0; i < cs.length; i++){
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.getCtClass(cs[i]);
				ocf.insertCodesIntoAllMethods(cs[i], cc);
				
			}
			System.out.println("DONE !!");
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
