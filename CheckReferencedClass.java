import javassist.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
public class CheckReferencedClass {
	public static void main(String[] args){
		ClassPool cp = ClassPool.getDefault();
		//String classname = "EjemploDos";
		try {
			System.out.print("Please enter class name: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String classname = br.readLine();
			
			CtClass cc = cp.get(classname);
			Collection<String> refClasses = cc.getRefClasses();
			for(Iterator<String> i = refClasses.iterator(); i.hasNext();){
				String str = i.next();
				System.out.println(str);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
