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
			final Objeto obj = new Objeto(className, null, checkedMethodName);
			cm.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException{
					//if(!m.getMethodName().contains("getString"))
					//make statement
					String statement = obj.makeStatementAndroid(m.getMethodName());
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

 	
}
