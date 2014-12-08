import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
public class ExtractMethodCalls{
	ArrayList<String> classNames;
	ArrayList<String> methodNames;
	ArrayList<ArrayList<String>> methodCallOfClass;
	public ExtractMethodCalls(){
		classNames = new ArrayList<String>();
		methodNames = new ArrayList<String>();
		methodCallOfClass = new ArrayList<ArrayList<String>>();
	}
	public static void main(String[] args){
		System.out.println("Enter class names you're looking for: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ExtractMethodCalls ex = new ExtractMethodCalls();
		try {
			String c = br.readLine();
			String[] cs = c.split(" ");
			SearchClassFiles scf = new SearchClassFiles(cs);
			scf.searchClassFile(scf.directoryPath);
			scf.showClasses();
			int classNum = scf.classFiles.size();
			ClassPool cp = ClassPool.getDefault();
			for(int i = 0; i < classNum; i++){
				String classPackageName = scf.clsPckgNames.get(i);
				int j = i + 1;
				System.out.println(j + "/" + classNum+ " " +  classPackageName);
				CtClass cc = cp.get(classPackageName);
				ex.iterateOneClass(cc, classPackageName);
				
			}
			ex.showMethodCalls();
			System.out.println("DONE");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void iterateOneClass(CtClass cc, String className){
		CtMethod[] methods = cc.getMethods();
		ArrayList<String> callLogEachMethod = new ArrayList<String>();;
		for(int i = 0; i < methods.length; i++){
			//callLogEachMethod = new ArrayList<String>();
			String longName = methods[i].getLongName();
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
			//this.methodCallOfClass.add(this.Extract(className, checkedMethod, cc));
			String str = this.Extract(className, checkedMethod, cc);
			if(str != null)
				callLogEachMethod.add(str);
		}
		this.methodCallOfClass.add(callLogEachMethod);
		
		
	}
	public String Extract(String className,String checkedMethodName, CtClass cc){
		try {
			CtMethod cm = cc.getDeclaredMethod(checkedMethodName);
			final Objeto obj = new Objeto(className, null, checkedMethodName);
			cm.instrument(new ExprEditor() {
				public void edit(MethodCall m) throws CannotCompileException{
					//if(!m.getMethodName().contains("getString"))
					//obj.register
					obj.register(m.getClassName(), m.getMethodName(), m.getLineNumber());
					m.replace("$_ = $proceed($$);");
				}
			});
			
			cc.writeFile();
			cc.defrost();
			//this.objetos.add(obj);
			//methodCalls.add(obj.methodCallLog);
			return obj.methodCallLog;
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public void showMethodCalls(){
		System.out.println("Show Method Calls");
		for(int i = 0; i < this.methodCallOfClass.size(); i++){
			ArrayList<String> methods = this.methodCallOfClass.get(i);
			System.out.println(methods.get(0));
			for(int j = 1; j < methods.size(); j++){
				System.out.println(methods.get(j));
			}
		}

	}

}
