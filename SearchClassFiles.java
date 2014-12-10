import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;


public class SearchClassFiles extends ModifyClassFiles {
	String[] modifiedClass;
	public SearchClassFiles(String[] args){
		super();
		//this.input = new File(".").getAbsolutePath();
		this.modifiedClass = new String[args.length];
		for(int i = 0; i < args.length; i++){
			this.modifiedClass[i] = args[i].toLowerCase();
		}

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			System.out.println("Enter class names you're looking for: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String c = br.readLine();
			String[] cs = c.split(" ");
			SearchClassFiles mccf = new SearchClassFiles(cs);
			mccf.searchClassFile(mccf.directoryPath);
			mccf.showClasses();
			System.out.println("Do you wanna insert codes ?");
			System.out.print("yes or no: ");
			
			String answer = br.readLine();
			if(answer.toLowerCase().contains("y") || answer.toLowerCase().equals("yes")){
				int classNum = mccf.classFiles.size();
				for(int i = 0; i < classNum; i++){
					String classPackageName = mccf.clsPckgNames.get(i);
					int j = i + 1;
					System.out.println(j + "/" + classNum+ " " +  classPackageName);
					
					mccf.insertCodesIntoAllMethods(classPackageName);
				}
				System.out.println("Insert Codes has Done");
			}else{
				System.out.println("Done !");
			}
		}catch(IOException e){
			
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
		String fileName = file.getName().toLowerCase();
		if(file.isDirectory()){
//			System.out.println("Directory: " + file.getName());
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
	public void insertCodesIntoAllMethods(String className){
		//it insert codes into every method of this class
		this.successInsert = 0;
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass	cc = cp.get(className);
			//cc.defrost();
			if(cc.isInterface()){
				return ;
			}else if(cc.isFrozen()){
				return ;
			}
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				if(methods[i].isEmpty())
					continue;
				String methodname = methods[i].getName();
				String longName = methods[i].getLongName();
				boolean run = false;
				if(methodname.contains("run")){
					run = true;
				}
				if(longName.contains("Thread") && !run){
					continue;
				}
				if(this.checkMethod(methodname)){
					String tag = "android.util.Log.d(\"ModifyClassFiles\",";
					String src1 = "\"CLASS: " + className + " METHOD: " + methodname + "\"";
					//String src2 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"method: " + methodname + " \");";
					String parameterInfo = makeParametersInfo(methods[i], cc);
					String src = tag + src1 + parameterInfo +  ");";
					//System.out.println(src);//for debug
					methods[i].insertBefore(src);
					this.successInsert++;
				}
				
			}
			cc.writeFile();
			cc.defrost();
			System.out.println("Write File Succeeded in " + className + ": " + this.successInsert + "/" + methods.length);
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
	public String makeParametersInfo(CtMethod cm, CtClass cc){//Override !!
		String[] parameterNames = getMethodParameterNames(cm, cc);
		CtClass[] types = getParameterTypes(cm, cc);
		String str = new String();
		
		if(parameterNames == null){
			for(int i = 0; i < types.length; i++){
				str += "+ \" args[" + i + "]:" + types[i].getName() + " = \" + $args[" + i + "] ";
			}
			//System.out.println(str + " in makeParameterInfo");//for debug
			return str;
		}
		//String parameterInfo = new String();
		if(parameterNames.length > 0){
			//parameterInfo = "{";
			for(int i = 0; i < parameterNames.length; i++){
				str += "+ \" args[" + i + "]:"  + types[i].getName() + " " + parameterNames[i] + "= \" + $args[" + i + "]";

			}
			//parameterInfo += "}";
		}else{
			//return "android.util.Log.d(\"ModifyClassFiles.makeParametersString\", \"No Parameter in This Method \");";
			return "+ \" args[]: No Parameter\"";

		}
		return str;
	}
}
