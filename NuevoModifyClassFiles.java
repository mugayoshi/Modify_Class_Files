import javassist.*;

import java.io.*;
import java.util.ArrayList;

public class NuevoModifyClassFiles extends ModifyClassFiles {
	String[] inputWords;
	String[] inputClasses;
	int matchMethodCount;
	int matchClassCount;
	ArrayList<String> insertedClass;
	ArrayList<String> insertedMethods;
	
	public NuevoModifyClassFiles(String[] args) {
		// TODO Auto-generated constructor stub
		super();
		this.inputWords = new String[args.length];
		for(int i = 0; i < args.length; i++){
			this.inputWords[i] = args[i].toLowerCase();
			//it make all input search word small
		}
		matchClassCount = 0;
		matchMethodCount = 0;
		insertedClass = new ArrayList<String>();
		insertedMethods = new ArrayList<String>();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter methods names you wanna insert: ");
			String m = br.readLine();
			String answer;
			String c = new String();
			
			if(m.length() < 1){
				System.out.println("You have to enter method names");
				return;
			}
			NuevoModifyClassFiles nuevo = new NuevoModifyClassFiles(m.split(" "));
			System.out.println("Do you wanna choose class this inserts codes ?");
			System.out.print("Yes or No: ");
			answer = br.readLine();
			if(answer.toLowerCase().equals("yes") || answer.toLowerCase().contains("y")){
				System.out.println("Please tell me classes below" );
				c = br.readLine();
				String[] classes = c.split(" ");
				nuevo.inputClasses = new String[classes.length];
				for(int i = 0; i < classes.length; i ++){
					nuevo.inputClasses[i]  = classes[i];
				}
				nuevo.searchClassFile(nuevo.directoryPath);
			}else{
				nuevo.searchDirectory(nuevo.directoryPath);
			}
			System.out.println("Searching Done !");
			int classNum = nuevo.classFiles.size();
			System.out.print("Do you wanna insert codes(1) or just look methods (2) ? --> ");
			answer = br.readLine();
			if(answer.equals("1")){
				for(int i = 0; i < classNum; i++){
					String classPackageName = nuevo.clsPckgNames.get(i);
					nuevo.insertCodes(classPackageName);
				}
			}else if(answer.equals("2")){
				for(int i = 0; i < classNum; i++){
					String classPackageName = nuevo.clsPckgNames.get(i);
					nuevo.searchAndShowMethods(classPackageName);
				}
			}else{
				System.out.println("this number is wrong !!");
				return ;
			}
			
			System.out.println("Count of Matches of Methods: for "+ m + nuevo.matchMethodCount);
			System.out.println("Count of Matches of Classes: " + c + nuevo.matchClassCount);
			nuevo.showInsertedClass();
			nuevo.showInsertedMethods();
		}catch (IOException e){
			System.out.println("IOException Occurred");
		}
		
		
		
	
	}
	public void showInsertedClass(){
		if(this.insertedClass.size() < 1){
			System.out.println("No InsertedClass\n");
			return ;
		}
		System.out.println("InsertedClass(ArrayList<String>), which this program inserted codes ");
		for(int i = 0; i < this.insertedClass.size(); i++){
			System.out.println("- " + this.insertedClass.get(i));
			
		}
	}
	public void showInsertedMethods(){
		if(this.insertedMethods.size() < 1){
			System.out.println("No InsertedMethods\n");
			return ;
		}
		System.out.println("InsertedMethods(ArrayList<String>), which this program inserted codes ");
		for(int i = 0; i < this.insertedMethods.size(); i++){
			System.out.println("- " + this.insertedMethods.get(i));
			
		}
	}
	
	public void searchAndShowMethods(String className){
		ClassPool cp = ClassPool.getDefault();
		CtClass cc;
		try {
			cc = cp.get(className);
			if(cc.isInterface()){
				return ;
			}else if(cc.isFrozen()){
				return ;
			}
			CtMethod[] methods = cc.getMethods();
			
			for(int i = 0; i < methods.length; i++){
				String methodname = methods[i].getName();
				if(this.checkMethod(methodname)){
					String lowerMethodName = methodname.toLowerCase();//to make it small characters
					boolean match = false;
					for(int j = 0; j < this.inputWords.length; j++){
						if(lowerMethodName.contains(this.inputWords[j])){
							match = true;
							this.matchMethodCount++;
							//System.out.println(methodname + " matches with " + this.inputWords[j] + "!!!");
							String info = methods[i].getName() + " in " + className;
							this.insertedMethods.add(info);
							break;
						}
					}
					if(!match){
						//System.out.println("Not Match");
						continue ;
					}
				}
			}
			cc.defrost();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
			
	public void insertCodes(String className){
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass	cc = cp.get(className);
			if(cc.isInterface()){
				return ;
			}else if(cc.isFrozen()){
				return ;
			}
			CtMethod[] methods = cc.getMethods();
			System.out.println("Insert Codes for " + className + "<- class");
			//System.out.println("Number of its Methods is " + methods.length);
			
			for(int i = 0; i < methods.length; i++){
				String methodname = methods[i].getName();
				if(this.checkMethod(methodname)){
					String lowerMethodName = methodname.toLowerCase();//to make it small characters
					boolean match = false;
					for(int j = 0; j < this.inputWords.length; j++){
					//	System.out.println(methodname + " compares with " + this.inputWords[j] + " in " + className);
						if(lowerMethodName.contains(this.inputWords[j])){
							match = true;
							this.matchMethodCount++;
							//System.out.println(methodname + " matches with " + this.inputWords[j] + "!!!");
							String info = methods[i].getName() + " " + className;
							this.insertedMethods.add(info);
							break;
						}
					}
					if(!match){
						//System.out.println("Not Match");
						continue ;
					}
					
					String src1 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"class: " + className + " \");";
					String src2 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"method: " + methodname + " \");";
					
					String parameterInfo = makeParametersInfo(methods[i], cc);
					String src = "{" + src1 + src2 + "}";
					methods[i].insertBefore(parameterInfo);
					methods[i].insertBefore(src);
				}
				
			}
			this.insertedClass.add(className);
			cc.writeFile();
			cc.defrost();
			System.out.println("Write File Succeeded in " + className);
		} catch (NotFoundException e) {
			System.out.println("Not Found Exception in insertCodes " + className);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			System.out.println("---- Cannpt Compile Exception in " + className + " ----");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void searchClassFile(String dirPath){

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(checkFile(files[i]) == false){
				searchClassFile(files[i].getAbsolutePath());
			}

		}
	}
	public boolean checkFile(File file){
		String fileName = file.getName();
		if(file.isDirectory()){
			return false;
		}
		//System.out.println("file name = " + fileName);
		for(int i = 0; i < this.inputClasses.length; i++){
			if(fileName.indexOf(this.inputClasses[i]) != -1 && fileName.contains(".class")){
				addClassFile(file, this.noSuperClass);
				this.matchClassCount++;
			}
		}
		return true;
	}

}
