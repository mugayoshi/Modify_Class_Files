import javassist.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchMethods extends ModifyClassFiles {
	String[] inputWords;
	String[] inputClasses;
	int matchMethodCount;
	int matchClassCount;
	ArrayList<String> insertedClass;
	ArrayList<String> insertedMethods;
	
	public SearchMethods(String[] args) {
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
			System.out.print("Enter Methods Names: ");
			String inputMethods = br.readLine();
			String answer;
			String c = new String();
			SearchMethods search;
			if(inputMethods.length() < 1){
				System.out.println("You have to enter method names");
				return;
			}
			if(inputMethods.toLowerCase().equals("all")){
				search = new SearchMethods(inputMethods.split(" "));
				search.searchDirectory(search.directoryPath);
				search.showAllMethods();
				return;
			}
			
			search = new SearchMethods(inputMethods.split(" "));
			System.out.println("Do you specify the classes ?");
			System.out.print("Yes or No: ");
			answer = br.readLine();
			if(answer.toLowerCase().contains("y")){
				System.out.println("Please tell me classes below" );
				c = br.readLine();
				String[] classes = c.split(" ");
				search.inputClasses = new String[classes.length];
				for(int i = 0; i < classes.length; i ++){
					search.inputClasses[i]  = classes[i];
				}
				search.searchClassFile(search.directoryPath);
				search.showClasses();
			}else{
				System.out.println("This is going to search all of the classes");
				search.searchDirectory(search.directoryPath);
			}
			
			int classNum = search.classFiles.size();
			System.out.println("Search Done Class Number is " + classNum);
			System.out.print("Do you wanna insert codes(1) or just look methods (2) ? --> ");
			answer = br.readLine();
			if(answer.equals("1")){
				for(int i = 0; i < classNum; i++){
					String classPackageName = search.clsPckgNames.get(i);
					if(search.avoidItself(classPackageName))
						search.insertCodesIntoSomeMethods(classPackageName);
				}
			}else if(answer.equals("2")){
				for(int i = 0; i < classNum; i++){
					String classPackageName = search.clsPckgNames.get(i);
					search.searchMethods(classPackageName);
				}
				
				
			}else{
				System.out.println("this number is wrong !!");
				return ;
			}
			
			System.out.println("Do you write this result to a text file ?");
			System.out.print("Yes or No: ");
			String write = br.readLine();
			if(write.toLowerCase().contains("y")){
				search.writeToFile(inputMethods.split(" "));
				return ;
			}
			search.showInsertedClass();
			search.showInsertedMethods();
			return;
		}catch (IOException e){
			System.out.println("IOException Occurred");
		}
		
		
		
	
	}
	public void showAllMethods(){
		System.out.println("Show All Methods in this current directory ");
		try{
			for(int i = 0; i < this.clsPckgNames.size(); i++){
				ClassPool cp = ClassPool.getDefault();
				CtClass cc = cp.get(this.clsPckgNames.get(i));
				if(cc.isInterface()){
					return ;
				}else if(cc.isFrozen()){
					return ;
				}
				CtMethod[] methods = cc.getMethods();
				for(int j = 0; j < methods.length; j++){
					String methodname = methods[j].getName();
					if(this.checkMethod(methodname)){
						this.matchMethodCount++;
						String info = methods[j].getName() + " " + this.clsPckgNames.get(i);
						this.insertedMethods.add(info);
						System.out.println(info);
					}
				}
			}
		}catch(NotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void showInsertedClass(){
		if(this.insertedClass.size() < 1){
			System.out.println("No InsertedClass\n");
			return ;
		}
		System.out.println("Number of Inserted Class is " + this.insertedMethods.size());
		
		try{
			System.out.println("Do you wanna see all of them ?");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String ans = br.readLine();
			if(ans.toLowerCase().contains("y")){
				for(int i = 0; i < this.insertedClass.size(); i++){
					System.out.println("- " + this.insertedClass.get(i));
				}

			}else{
				return ;
			}
		}catch (IOException e){
			System.out.println("IO Exception Occurred");
		}

	}
	public void showInsertedMethods(){
		if(this.insertedMethods.size() < 1){
			System.out.println("No InsertedMethods\n");
			return ;
		}
		System.out.println("Number of Inserted Methods is " + this.insertedMethods.size());
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Do you wanna see all of them ?");
			String ans = br.readLine();
			if(ans.toLowerCase().contains("y")){
				for(int i = 0; i < this.insertedMethods.size(); i++){
					System.out.println("- " + this.insertedMethods.get(i));
					
				}
			}else{
				return ;
			}
		}catch (IOException e){
			System.out.println("IO Exception Occurred");
		}
		
		
		
	}
	
	public void searchMethods(String className){
		ClassPool cp = ClassPool.getDefault();
		CtClass cc;
		boolean flag = false;
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
							if(flag == false){
								flag = true;
								this.insertedClass.add(className);
							}
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
		}catch (RuntimeException e){
			System.out.println("Runtime Exception Occurred in " + className);
		}
		
	}
			
	public void insertCodesIntoSomeMethods(String className){
		this.successInsert = 0;
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass	cc = cp.get(className);
			if(cc.isInterface()){
				return ;
			}else if(cc.isFrozen()){
				return ;
			}
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				String methodname = methods[i].getName();
				String longName = methods[i].getLongName();
				//remove methods that Thread class has except run
				boolean run = false;
				if(methodname.contains("run")){
					run = true;
				}
				if(longName.contains("Thread") && !run){
					//System.out.println("This Methods is from Thread Class");
					continue;
				}
				if(this.checkMethod(methodname)){
					String lowerMethodName = methodname.toLowerCase();//to make it small characters
					boolean match = false;
					for(int j = 0; j < this.inputWords.length; j++){
						if(lowerMethodName.contains(this.inputWords[j])){
							match = true;
							this.matchMethodCount++;
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
					this.successInsert++;
				}
				
			}
			this.insertedClass.add(className);
			cc.writeFile();
			cc.defrost();
			System.out.println("Write File Succeeded in " + className + " " +  this.successInsert + "/" + methods.length);
		} catch (NotFoundException e) {
			System.out.println("Not Found Exception in insertCodes " + className);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot Compile Exception in " + className);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/*catch (RuntimeException e){
			System.out.println("Runtime Exception Occurred in " + className);
		}*/
	}

	public void searchClassFile(String dirPath){

		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(checkAndAddFile(files[i]) == false){
				searchClassFile(files[i].getAbsolutePath());
			}

		}
	}
	public boolean checkAndAddFile(File file){
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
	public void writeToFile(String[] input){
		FileWriter writer;
		String s = new String();
		for(int i = 0; i < input.length; i++){
			 s += input[i] + "_";
		}
		SimpleDateFormat simpleDate =  new SimpleDateFormat("dd_MM_HH_mm");
		Date date = new Date();
		String date_str = simpleDate.format(date);
		String fileName = s + date_str + "_searchMethods.txt";
		File file = new File(fileName);
		try {
			
			writer = new FileWriter(file, true);
			String strStart = "---- Start Classes ----\n";
			writer.write(strStart);
			for(int i = 0; i < this.insertedClass.size(); i++){
				String str = "- " + this.insertedClass.get(i) + "\n";
				writer.write(str);
			}
			String strEnd = "---- End Classes ----\n";
			writer.write(strEnd);

			strStart = "---- Start Methods ----\n";
			writer.write(strStart);
			for(int i = 0; i < this.insertedMethods.size(); i++){
				String str = this.insertedMethods.get(i) + "\n";
				writer.write(str);
			}
			strEnd = "---- End Methods ----\n";
			writer.write(strEnd);
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return;
	}

}
