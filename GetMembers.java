import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javassist.*;
public class GetMembers extends ModifyClassFiles {

	public GetMembers(){
		super();
	}
	public static void main(String[] args){
		GetMembers gm = new GetMembers();
		gm.searchDirectory(gm.directoryPath);
		int classNum = gm.clsPckgNames.size();
		
		String inputPackage = new String();
		if(args.length == 1){
			inputPackage = args[0]; 
			System.out.println("Package Name : " + inputPackage + "\n");
			System.out.println("Caution com.XXX.YYY -> you can input just XXX or YYY");
		}
		ClassPool cp = ClassPool.getDefault();
		try {
			if(args.length < 1){
				for(int i = 0; i < classNum; i++){
					String classname = gm.clsPckgNames.get(i);
					if(gm.avoidItself(classname)){
						CtClass cc = cp.get(classname);
						gm.getFieldInfo(cc);
					}
				} 
			}else{
				for(int i = 0; i < classNum; i++){
					String classname = gm.clsPckgNames.get(i);
					if(classname.toLowerCase().contains(inputPackage) == false){
						continue;
					}
					if(gm.avoidItself(classname)){
						CtClass cc = cp.get(classname);
						gm.getFieldInfo(cc);
					}
					
				}
			}
			System.out.println("DONE !!");

		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	public void getParameter(CtClass cc){
		System.out.println("---- Begin getParameter of " + cc.getName() + "----");
		
		CtMethod[] methods = cc.getMethods();
		for(int i = 0 ; i < methods.length; i++){
			String methodname = methods[i].getName();
			if(this.checkMethod(methodname) == false){
				continue;
			}
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
			String[] par = this.getMethodParameterNames(methods[i], cc);
			if(par != null){
				this.showParameter(par);
			}

		}
		System.out.println("---- End of " + cc.getName() + "in getParameter ----");
	}
	public void showParameter(String[] p){
		for(int i = 0; i < p.length; i++){
			if(p[i] == null)
				p[i] = "NoName";
			System.out.print(p[i] + " ");
			if(i / 10 == 0){
				System.out.println("");
			}
		}
	}
	public ArrayList<String> getFieldInfo(CtClass cc){
		//ClassPool cp = ClassPool.getDefault();
		try {
			//CtClass cc = cp.get(className);
			String className = cc.getName();
			CtField[] fields = cc.getFields();
			ArrayList<String> list = new ArrayList<String>();
			System.out.println("\n--- Begin getFieldInfo of " + className + " ---");
			for(int i = 0; i < fields.length; i++){
				System.out.println("\t" + fields[i].getType().getName() + " " + fields[i].getName() + " ");
				/*if(this.checkObject(cls) && cls != null){
					list.add(fields[i].getName());
					
				}*/
			}
			System.out.println("--- End getFieldInfo of " + className +  "---");
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
		if(packageName.contains("java.lang") && cc != null)
			return true;
		return false;
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
			//this.refClasses.add(str);
		}
		
	}
}
