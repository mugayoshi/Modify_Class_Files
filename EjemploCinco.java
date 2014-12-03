import java.io.IOException;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;


public class EjemploCinco {
	public static void main(String[] args){
		EjemploCinco cinco = new EjemploCinco();
		ClassPool cp = ClassPool.getDefault();
		String className = "EjemploDos";
		String checkedMethod = "main";
		try {
			CtClass cc = cp.get(className);
			CtMethod[] methods = cc.getMethods();
			for(int i = 0; i < methods.length; i++){
				String methodName = methods[i].getName();
				if(cinco.checkMethod(methodName)){
					cinco.replaceCodes(className, methodName, checkedMethod);
				}
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
	}
	public void replaceCodes(String className, String methodName, String checkedMethodName){
		ClassPool cp = ClassPool.getDefault();
		CtClass cc;
		try {
			cc = cp.get(className);
			CtMethod cm = cc.getDeclaredMethod(checkedMethodName);
			final Objeto obj = new Objeto(className, methodName);
			obj.list = this.getFieldInfo(className);
			obj.makeStatement();
			cm.instrument(new ExprEditor() {
				String classname = obj.className;
				String methodname = obj.methodName;
				//String statement = "{" + "System.out.println($0.str);" + "$_ = $proceed($$);}";
				String statement = "{" + obj.statement + "$_ = $proceed($$);}";
				public void edit(MethodCall m) throws CannotCompileException{
					if(m.getClassName().equals(classname) && m.getMethodName().equals(methodname)){
						m.replace(statement);
					}
				}
			});
			cc.writeFile();
			cc.defrost();
		} catch (NotFoundException e) {
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
}
