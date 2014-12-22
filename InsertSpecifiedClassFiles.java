import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javassist.*;
public class MethodReplaceSpecified extends MethodReplace {

	public static void main(String[] args){
		System.out.println("Method Replacement for Specified Class Files");
		System.out.println("Enter Exact Package Names (e.g. com.example.a)");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		MethodReplaceSpecified mrs = new MethodReplaceSpecified();
		ClassPool cp = ClassPool.getDefault(); 
			
		try {
			String c = br.readLine();
			String[] cs = c.split(" ");
			for(int i = 0; i < cs.length; i++){
				//String classPackageName = mrs.clsPckgNames.get(i);
				int j = i + 1;
				System.out.println(j + "/" + cs.length + ": " +  cs[i]);
				CtClass cc = cp.get(cs[i]);
				mrs.iterateOneClass(cc, cs[i]);
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
