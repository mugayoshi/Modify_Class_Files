import javassist.*;

import java.io.*;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
public class ShowMethods extends SearchClassFiles {
	boolean write;
	File file;
	public ShowMethods(String[] args){
		super(args);
		this.write = false;
	}
	
	public static void main(String[] args){
		System.out.print("Enter Class Name: ");
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			String[] cs = c.split(" ");
			ShowMethods sm = new ShowMethods(cs);
			sm.searchClassFile(sm.directoryPath);
			sm.showClasses();
			System.out.print("Do you wanna write this result to a text file ?: ");
			String ans = br.readLine();
			if(ans.toLowerCase().contains("y")){
				sm.write = true;
				SimpleDateFormat simpleDate =  new SimpleDateFormat("dd:MM:HH:mm");
				Date date = new Date();
				String date_str = simpleDate.format(date);
				String input = new String();
				for(int i = 0; i < cs.length; i++){
					input += cs[i] + "_";
				}
				sm.file = new File(input + "_" + date_str + "_showMethods.txt");
			}
			for(int i = 0; i < sm.classFiles.size(); i++){
				String classname = sm.clsPckgNames.get(i);
				if(sm.write){
					sm.writeMethods(classname);
				}else{
					sm.showMethods(classname);
				}
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
					System.out.println(methodname + " <- " +  methods[i].getLongName());
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found Exception Occurred");
			e.printStackTrace();
		}finally{
			System.out.println("---- End of Methods of " + classname + " ----");
		}
		
	}
	public void writeMethods(String classname){
		ClassPool cp = ClassPool.getDefault();
		try {
			FileWriter writer = new FileWriter(this.file, true);
			//System.out.println("----Begin Methods of " + classname + " -----");
			String begin = "---- Begin Methods of " + classname + " -----\n";
			writer.write(begin);
			CtClass cc = cp.get(classname);
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				String methodname = methods[i].getName();
				if(this.checkMethod(methodname)){
					//System.out.println(methodname + " <- " +  methods[i].getLongName());
					String str = methodname + " <- " +  methods[i].getLongName() + "\n";
					writer.write(str);
				}
			}
			String end = "---- End Methods of " + classname + " -----\n";
			writer.write(end);
			writer.close();
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found Exception Occurred");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*finally{
			System.out.println("---- End of Methods of " + classname + " ----");
		}*/
	}
}
