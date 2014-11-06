import java.io.IOException;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
public class Segundo {
	String name;
	public Segundo(String n){
		this.name = n;
	}
	public static void main(String[] args){
		ClassPool cp = ClassPool.getDefault();
		String classname = "Tercero";
		Segundo s = new Segundo("Muga");
		try {
			CtClass cc = cp.get(classname);
			CtMethod[] methods = cc.getMethods();
			System.out.println("Before For Loop");
			for(int i = 0; i < methods.length; i++){
				String methodName = methods[i].getName();
				if(s.checkMethod(methodName)){
					String str1 = "System.out.println(\"Method Name is " + methodName + " \" + $0.toString());";
					String parameterInfo = makeParametersString(methods[i]);
					System.out.println(parameterInfo);
					methods[i].insertBefore(parameterInfo);
					methods[i].insertBefore(str1);
				}
			}
			
			Class<?> c = cc.toClass();
			
			Tercero tres = (Tercero)c.newInstance();
			int i = tres.ex2(9);
			tres.ex3("Muga", "Yoshikawa");
			tres.ex1();
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/ catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public static CtClass[] getParameterTypes(CtMethod cm){
		CtClass[] cc = null;
		try {
			 cc = cm.getParameterTypes();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return cc;
		}
	}
	public static String[] getMethodParameters(CtMethod cm){
		MethodInfo methodInfo = cm.getMethodInfo();
		CodeAttribute codeAttr = methodInfo.getCodeAttribute();
		LocalVariableAttribute attr = (LocalVariableAttribute) codeAttr.getAttribute(javassist.bytecode.LocalVariableAttribute.tag);
		String[] paramNames = null;
		try {
			paramNames = new String[cm.getParameterTypes().length];
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
		for(int i = 0; i < paramNames.length; i++){
			paramNames[i] = attr.variableName(i + pos);
		}
		return paramNames;
				
	}
	public static String makeParametersString(CtMethod cm){
		String[] parameters = getMethodParameters(cm);
		CtClass[] types = getParameterTypes(cm);
		String parameterInfo = "{";
		if(parameters.length > 0){
			
			String str = new String();
			for(int i = 0; i < parameters.length; i++){
						str = "args[" + i + "]:"  + types[i].getName() + " " + parameters[i] + "= ";
						parameterInfo += "System.out.println(\"" + str + "\" + $args[" + i + "]);";
			}
			parameterInfo += "}";
		}else{
			return "System.out.println(\"No Parameter in This Method \");";
		}
				return parameterInfo;
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
	public void showName(){
		System.out.println(this.name);
		
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
	public static String analyzeParameterType(CtClass[] cc){
		String type = new String();
		
		return type;
	}
	
}
