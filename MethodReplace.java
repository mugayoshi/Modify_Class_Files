import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
public class MethodReplace extends ModifyClassFiles {
	ArrayList<String> refClasses;
	public MethodReplace(){
		super();
		this.refClasses = new ArrayList<String>();
	}
	public static void main(String[] args){
		ClassPool cp = ClassPool.getDefault();
		MethodReplace rep = new MethodReplace();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter class names: ");
			String c = br.readLine();
			String[] cs = c.split(" ");
			SearchClassFiles scf = new SearchClassFiles(cs);
			scf.searchClassFile(scf.directoryPath);
			scf.showClasses();
			System.out.println("Do you wanna insert codes ?");
			System.out.print("yes or no: ");
			
			String answer = br.readLine();
			if(answer.toLowerCase().contains("y") || answer.toLowerCase().equals("yes")){
				int classNum = scf.classFiles.size();
				for(int i = 0; i < classNum; i++){
					String classPackageName = scf.clsPckgNames.get(i);
					int j = i + 1;
					System.out.println(j + "/" + classNum+ " " +  classPackageName);
					CtClass cc = cp.get(classPackageName);
					rep.iterateOneClass(cc, classPackageName);
					//scf.insertCodesIntoAllMethods(classPackageName);//this should be changed
				}
				System.out.println("Replacement has Done");
			}else{
				System.out.println("Done !");
			}
			
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getReferencedClasses(String className, CtClass cc){
		Collection<String> refClasses = cc.getRefClasses();
		for(Iterator<String> i = refClasses.iterator(); i.hasNext();){
			String str = i.next();
			System.out.println(str);
			if(str.contains("android")){
				continue;
			}else if(str.contains("java.lang")){
				continue;
			}
			this.refClasses.add(str);
		}
		
	}
	public void iterateOneClass(CtClass cc, String className){
		CtMethod[] methods = cc.getMethods();
		for(int i = 0; i < methods.length; i++){
			String longName = methods[i].getLongName();
			// String[] methodPackages = longName.split("\\.");
			String checkedMethod = methods[i].getName();
			/*if(checkedLongName.contains("android")){
				continue;
			}*/
			if(longName.contains("java")){
				continue;
			}
			/*if(longName.contains("android") && longName.contains("getString")){
				continue;
			}*/
			if(longName.contains("android") && methods[i].isEmpty()){
				continue;
			}
			this.replaceCodes(className, checkedMethod, cc);
			
		}
	}
	public void replaceCodes(String className,String checkedMethodName, CtClass cc){
		try {
			
			CtMethod cm = cc.getDeclaredMethod(checkedMethodName);
			//final String statement = this.makeStatement(className, checkedMethodName);
			final Objeto obj = new Objeto(className, null, checkedMethodName);
			cm.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException{
					//if(!m.getMethodName().contains("getString"))
					//make statement
					String statement = obj.makeStatementAndroid();
					m.replace(statement);
				}
			});
			cc.writeFile();
			cc.defrost();
		} catch (NotFoundException e) {
			System.out.println("Not Found Exception " + checkedMethodName + " in " + className);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String makeStatement(String className, String checkedMethod){
		Random r = new Random();
		int random = r.nextInt(1000);
		String log_before = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " + random + " Called from " + checkedMethod + " of " + className + "\");";
		
		String log_after = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " +  random + " Backed to  " + checkedMethod + " of " + className + "\");";
		String statement = "{" + log_before + "$_ = $proceed($$);" + log_after + "}";
		return statement;
	}
 	public ArrayList<String> getFieldInfo(String className){
		ClassPool cp = ClassPool.getDefault();
		try {
			CtClass cc = cp.get(className);
			CtField[] fields = cc.getFields();
			ArrayList<String> list = new ArrayList<String>();
			for(int i = 0; i < fields.length; i++){
				CtClass cls = fields[i].getType();
				if(this.checkObject(cls)){
					list.add(fields[i].getName());
				}
			}
			return list;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public boolean checkObject(CtClass cc){
		if(cc.isPrimitive()){
			return true;
		}
		String packageName  = cc.getPackageName();
		if(packageName.contains("java.lang"))
			return true;
		return false;
	}
}
