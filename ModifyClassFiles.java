import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
public class ModifyClassFiles {
	
	ArrayList<File> classFiles;
	ArrayList<String> clsPckgNames;
	String directoryPath;
	final String log_tag = "ModifyClassFiles.main";
	final String log_template = "android.util.Log.d(";
	String input;
	final String noSuperClass = "Anything";
	public ModifyClassFiles(){
		this.classFiles = new ArrayList<File>();
		this.clsPckgNames = new ArrayList<String>();
		input = new String();
		this.directoryPath = new File(".").getAbsolutePath();
		String[] s = this.directoryPath.split("/");
		this.input = s[s.length - 2];
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		ModifyClassFiles modClassFile = new ModifyClassFiles();
		modClassFile.searchDirectory(modClassFile.directoryPath);
		int classNum = modClassFile.classFiles.size();
		
		System.out.println("Classes Number: " + modClassFile.classFiles.size());
		for(int i = 0; i < classNum; i++){
			String className = modClassFile.clsPckgNames.get(i);
			int j = i + 1;
			System.out.println(j + "/" + classNum+ " " +  className);
			if(className.equals("ModifyClassFiles")){
				System.out.println("Skip because this filename is itself");
				break;
			}
			modClassFile.insertCodes(className);
			
		}
		System.out.println("Writing Codes to Class Files has done !!");

	}
	
	
	public void showClasses(){
		System.out.println("----- Begin of Show Classes  ----");
		System.out.println("Number of Classes is  " + this.clsPckgNames.size());
		for(int i = 0; i < this.classFiles.size(); i++){
			String packageName = this.clsPckgNames.get(i);
			/*if(packageName.contains("android.")){
				continue;
			}*/
			System.out.println(packageName); 
			
		}
		System.out.println("----- End of Show Classes ----");

	}
	public String getSuperClass(String classname){
		
		String superclassname = new String();
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass cc = cp.get(classname);
			CtClass superclass = cc.getSuperclass();
			superclassname = superclass.getName();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found Exception Occurred");
			e.printStackTrace();
			superclassname = "NOT FOUND EXCEPTION";
		}finally{
			return superclassname;
		}
			
		
	}
	public void showMethods(String classname){
		System.out.println("----Begin Methods of " + classname + " -----");
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass cc = cp.get(classname);
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				System.out.println(methods[i].getName());
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found Exception Occurred");
			e.printStackTrace();
		}finally{
			System.out.println("---- End of Methods of " + classname + " ----");
		}
		
	}
	public void insertCodes(String className){
		//it insert codes into every method of this class
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
			System.out.println("Insert Codes for " + className + "<- class");
			System.out.println("Number of its Methods is " + methods.length);
			
			for(int i = 0; i < methods.length; i++){
				/*if(methods[i].isEmpty())
					continue;*/
				String methodname = methods[i].getName();
				if(this.checkMethod(methodname)){
					//String src1 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"ths class is \" + $0.toString() );";
					String src1 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"class: " + className + " \");";
					String src2 = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", \"method: " + methodname + " \");";
					
					String parameterInfo = makeParametersInfo(methods[i], cc);
					String src = "{" + src1 + src2 + "}";
					methods[i].insertBefore(parameterInfo);
					methods[i].insertBefore(src);
				}
				
			}
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
	public void insertCodes(String className, String methodName, String insertSentence){
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass cc = cp.get(className);
			CtMethod cm = cc.getDeclaredMethod(methodName);
			cm.insertBefore(insertSentence);
			cm.insertAfter(insertSentence);
			cc.writeFile();
			System.out.println("Insert Succeeded");
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Not Found");
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("Insert Codes: " + className);

		}
		
	}
	
	public void searchDirectory(String dirPath){
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(check(file, this.noSuperClass) == false){
				searchDirectory(file.getAbsolutePath());
			}
			
		}
	}
	public void searchDirectory(String directoryPath, String superclass){
		File dir = new File(directoryPath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			File file = files[i];
			if(check(file, superclass) == false){
				searchDirectory(file.getAbsolutePath(), superclass);
			}
			
		}
		
		
	}
	
	public boolean check(File file, String superclass){
		if(file.isDirectory()){
			//System.out.println("Directory: " + file.getName());
			return false;
		}
		
		if(file.getName().contains(".class")){
			addClassFile(file, superclass);
		}
		return true;
	}
	
	public void addClassFile(File file, String superclass){
		String[] dirs = file.getPath().split("/");
		int index = 0;
		
		for(int i = 0; i < dirs.length; i++){
			
			if(dirs[i].contains(this.input)){
				index = i;
				break;
			}
		}
		String packageName = new String();
		for(int j = index + 1; j < dirs.length - 1; j++){
			if(dirs[j].equals(".")){
				continue;
			}
			if(dirs[j].contains("android")){
				return ;
			}
			if(dirs[j].indexOf("android") != -1){
				return;
			}
			packageName += dirs[j] + ".";
		}
		packageName += dirs[dirs.length - 1];
		int indexOfSuffix = packageName.indexOf(".class");
		packageName = packageName.substring(0, indexOfSuffix);
		
		if(superclass.equals(this.noSuperClass)){
			this.classFiles.add(file);
			this.clsPckgNames.add(packageName);
			return;
		}
		if(this.getSuperClass(packageName).equals(superclass)){
			if(packageName.contains("android.")){
				//if this class is android.* , it's not added to ArrayList (activity classes)
				return;
			}
			this.classFiles.add(file);
			this.clsPckgNames.add(packageName);
		}
		return;
	}
	public boolean checkMethod(String methodName){
		if(methodName.equals("clone")){
			return false;
		}else if(methodName.equals("equals")){
			return false;
		}else if(methodName.equals("finalize")){
			return false;
		}else if(methodName.equals("getClass")){
			return false;
		}else if (methodName.equals("hashCode")){
			return false;
		}else if(methodName.equals("notify")){
			return false;
		}else if(methodName.equals("notifyAll")){
			return false;
		}else if(methodName.equals("toString")){
			return false;
		}else if(methodName.equals("wait")){
			return false;
		}
		return true;
	}
	public String[] getMethodParameterNames(CtMethod cm, CtClass cc){
		String[] paramNames = null;
		try {
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttr = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttr.getAttribute(javassist.bytecode.LocalVariableAttribute.tag);

			paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			for(int i = 0; i < paramNames.length; i++){
				paramNames[i] = attr.variableName(i + pos);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block 
			System.out.println("---- Get Method Parameter Not Found Exception in Method: " + cm.getName() + " in " + cc.getName() + " ----");
			//e.printStackTrace();
		}catch (NullPointerException e){
			System.out.println("---- Get Method Parameter Null Pointer Exception in Method: " + cm.getName() + " in " + cc.getName() + " ----");
		}
		
		return paramNames;
			
	}
	
	public CtClass[] getParameterTypes(CtMethod cm, CtClass cl){
		CtClass[] cc = null;
		try {
			 cc = cm.getParameterTypes();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("----Get Parameter Type Null Pointer Exception in Method: " + cm.getName() + " in " + cl.getName() + " ----");

		//	e.printStackTrace();
		}finally{
			return cc;
		}
	}
	public String makeParametersInfo(CtMethod cm, CtClass cc){
		String[] parameters = getMethodParameterNames(cm, cc);
		CtClass[] types = getParameterTypes(cm, cc);
		String str = new String();
		
		if(parameters == null){
			//log_template = "android.util.Log.d(";
			//return "android.util.Log.d(\"ModifyClassFiles.makeParametersString\", \"Can't Get This Method's Info, Sorry\");";
			return log_template + "\"ModifyClassFiles.makeParametersString\", \"Can't Get This Method's Info, Sorry\");";
		}
		String parameterInfo = new String();
		if(parameters.length > 0){
			parameterInfo = "{";
			for(int i = 0; i < parameters.length; i++){
				str = "args[" + i + "]:"  + types[i].getName() + " " + parameters[i] + "= ";
				//parameterInfo += "System.out.println(\"" + str + "\" + $args[" + i + "]);";
				parameterInfo += "android.util.Log.d(\"ModifyClassFiles.makeParametersString\", \"" + str +  "\" + $args[" + i + "]);";

			}
			parameterInfo += "}";
		}else{
			//return "android.util.Log.d(\"ModifyClassFiles.makeParametersString\", \"No Parameter in This Method \");";
			return log_template + "\"ModifyClassFiles.makeParametersString\", \"No Parameter in This Method \");";

		}
		return parameterInfo;
	}

}
