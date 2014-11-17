import javassist.*;

import java.io.*;
public class ShowMethods extends SearchClassFiles {

	public ShowMethods(String[] args){
		super(args);
	}
	
	public static void main(String[] args){
		System.out.print("Input Class Name you're looking for: ");
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			String[] cs = c.split(" ");
			ShowMethods sm = new ShowMethods(cs);
			sm.searchClassFile(sm.directoryPath);
			sm.showClasses();
			
			for(int i = 0; i < sm.classFiles.size(); i++){
				String classname = sm.clsPckgNames.get(i);
				sm.showMethods(classname);
			}
			System.out.println("Done !!");
		}catch(IOException e){
			System.out.println("IO Exception Occurred");
		}
	}
	
	public void showMethods(String classname){
		System.out.println("----Begin Methods of " + classname + " -----");
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass cc = cp.get(classname);
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				String methodname = methods[i].getName();
				if(this.checkMethod(methodname))
					System.out.println(methodname);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found Exception Occurred");
			e.printStackTrace();
		}finally{
			System.out.println("---- End of Methods of " + classname + " ----");
		}
		
	}
}
