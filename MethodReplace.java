import java.io.BufferedReader;
import java.io.File;
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
	public MethodReplace(){
		super();
	}
	public static void main(String[] args){
		ClassPool cp = ClassPool.getDefault();
		MethodReplace rep = new MethodReplace();
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Enter class names: ");
			String c = br.readLine();
			ArrayList<String> clsPckgs;
			
			if(c.toLowerCase().contains("all")){
				rep.searchDirectory(rep.directoryPath);
				clsPckgs = rep.clsPckgNames;
				System.out.println("Class Num: " + clsPckgs.size());
			}else{
				String[] cs = c.split(" ");
				SearchClassFiles scf = new SearchClassFiles(cs);
				scf.searchClassFile(scf.directoryPath);
				scf.showClasses();
				clsPckgs = scf.clsPckgNames;
			}
			System.out.println("Do you wanna insert codes ?");
			System.out.print("yes or no: ");
			String answer = br.readLine();
			if(answer.toLowerCase().contains("y") || answer.toLowerCase().equals("yes")){
				//int classNum = classFiles.size();
				for(int i = 0; i < clsPckgs.size(); i++){
					String classPackageName = clsPckgs.get(i);
					int j = i + 1;
					System.out.println(j + "/" + clsPckgs.size() + " " +  classPackageName);
					CtClass cc = cp.get(classPackageName);
					rep.iterateOneClass(cc, classPackageName);
					clsPckgs.set(i, null);
				}
				System.out.println("Replacement has Done !!");
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
		String longName, checkedMethod;
		for(int i = 0; i < methods.length; i++){
			longName = methods[i].getLongName();
			// String[] methodPackages = longName.split("\\.");
			checkedMethod = methods[i].getName();
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
		methods = null;
		return;
	}
	public void replaceCodes(String className,String checkedMethodName, CtClass cc){
		try {
			
			CtMethod cm = cc.getDeclaredMethod(checkedMethodName);
			final Objeto obj = new Objeto(className, null, checkedMethodName);
			cm.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException{
					//if(!m.getMethodName().contains("getString"))
					//make statement
					String statement = obj.makeStatementAndroid(m);
					m.replace(statement);
				}
			});
			cc.writeFile();
			cc.defrost();
			return;
		} catch (NotFoundException e) {
			System.out.println("Not Found Exception " + checkedMethodName + " in " + className);
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}

 	
}
