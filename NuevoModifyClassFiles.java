import javassist.*;

import java.io.*;

public class NuevoModifyClassFiles extends ModifyClassFiles {
	String[] inputWords;
	String[] modifiedClass;
	int matchMethodCount;
	int matchClassCount;
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
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length < 1){
			System.out.println("Usage: You need to input search words of methods you wanna insert into");
			return ;
		}
		NuevoModifyClassFiles nuevo = new NuevoModifyClassFiles(args);
		nuevo.searchDirectory(nuevo.directoryPath);
		
		int classNum = nuevo.classFiles.size();
		for(int i = 0; i < classNum; i++){
			String classPackageName = nuevo.clsPckgNames.get(i);
			int j = i + 1;
			System.out.println(j + "/" + classNum+ " " +  classPackageName);
			nuevo.insertCodes(classPackageName, nuevo.inputWords);
		}
		System.out.println("Count of Match Method: " + nuevo.matchMethodCount);
	
	}
	public void insertCodes(String className, String[] input){
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
							System.out.println(methodname + " matches with " + this.inputWords[j] + "!!!");
							break;
						}
					}
					if(!match){
						//System.out.println("Not Match");
						continue ;
					}
					
					String src1 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"class: " + className + " \");";
					String src2 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"method: " + methodname + " \");";
					
					/*String parameterInfo = makeParametersInfo(methods[i], cc);
					String src = "{" + src1 + src2 + "}";
					methods[i].insertBefore(parameterInfo);
					methods[i].insertBefore(src);*/
				}
				
			}
			/*cc.writeFile();
			cc.defrost();*/
			System.out.println("Write File Succeeded in " + className);
		} catch (NotFoundException e) {
			System.out.println("Not Found Exception in insertCodes " + className);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}/* catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			System.out.println("---- Cannpt Compile Exception in " + className + " ----");
			e.printStackTrace();
		}*//* catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
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
		for(int i = 0; i < this.modifiedClass.length; i++){
			if(fileName.indexOf(this.modifiedClass[i]) != -1 && fileName.contains(".class")){
				addClassFile(file, this.noSuperClass);
			}
		}
		return true;
	}

}
