import java.io.IOException;

import javassist.*;
import javassist.bytecode.AccessFlag;
public class ChangeAccessFlag extends ModifyClassFiles {

	public static void main(String[] args){
		String targetClass, targetMethod;
		if(args.length < 2){
			System.out.println("Usage: args[0] = target class, args[1] = target method,,,");
			return;
		}
		
		ClassPool cp = ClassPool.getDefault();
		
		try {
			if(args[0].equals("-r")){
				targetClass = args[1];
				CtClass cc = cp.get(targetClass);
				for(int i = 2; i < args.length; i++){
					targetMethod = args[i];
					changeBackToPrivate(targetMethod, cc);
				}
				return;
			}
			targetClass = args[0];
			CtClass cc = cp.get(targetClass);
			for(int i = 1; i < args.length; i++){
				targetMethod = args[i];
				changeToPublic(targetMethod, cc);
			}
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void changeToPublic(String method, CtClass cc){
		
		try {
			CtMethod cm = cc.getDeclaredMethod(method);
			int mod = cm.getModifiers();
			if(mod == AccessFlag.PRIVATE){
				cm.setModifiers(AccessFlag.PUBLIC);
				System.out.println(cm.getName() + " has changed to public");
				cc.writeFile();
				cc.defrost();
			}
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void changeBackToPrivate(String method, CtClass cc){

		try {
			CtMethod cm = cc.getDeclaredMethod(method);
			int mod = cm.getModifiers();
			if(mod == AccessFlag.PUBLIC){
				cm.setModifiers(AccessFlag.PRIVATE);
				System.out.println(cm.getName() + " has changed back to Private");
				cc.writeFile();
				cc.defrost();
			}
			
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
