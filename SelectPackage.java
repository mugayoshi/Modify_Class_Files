import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;


public class SelectPackage extends ModifyClassFiles {
	File file;
	
	public static void main(String[] args){
		SelectPackage sp = new SelectPackage();
		sp.searchDirectory(sp.directoryPath);
		int classNum = sp.clsPckgNames.size();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input_class;
		try {
			System.out.print("please input package name: ");
			input_class = br.readLine();
			System.out.print("Do you insert codes ?: ");
			String ans = br.readLine();
			if(ans.toLowerCase().contains("y")){
				for(int i = 0; i < classNum; i++){
					String classname = sp.clsPckgNames.get(i);
					if(classname.contains(input_class) == false){
						continue;
					}
					if(sp.avoidItself(classname)){
						sp.insertCodesIntoAllMethods(classname);
					}

				}
				System.out.println("Writing Codes to Class Files has done !!");
			}else{
				System.out.print("Do you write this result to a text file ?: ");
				String ans1 = br.readLine();
				if(ans1.toLowerCase().contains("y")){
					SimpleDateFormat simpleDate =  new SimpleDateFormat("dd:MM:HH:mm");
					Date date = new Date();
					String date_str = simpleDate.format(date);
					sp.file = new File(input_class + "_" + date_str + "_SelectPck.txt");
					for(int i = 0; i < sp.classFiles.size(); i++){
						String classname = sp.clsPckgNames.get(i);
						if(classname.contains(input_class) == false){
							continue;
						}
						sp.writeMethods(classname);
					}
					System.out.println("Writing to " + sp.file.getName()+ "has Done");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
	
	public void writeMethods(String classname){
		ClassPool cp = ClassPool.getDefault();
		try {
			FileWriter writer = new FileWriter(this.file, true);
			//System.out.println("----Begin Methods of " + classname + " -----");
			String begin = "----Begin Methods of " + classname + " -----\n";
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
			String end = "----End Methods of " + classname + " -----\n";
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
