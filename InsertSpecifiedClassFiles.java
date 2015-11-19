import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class InsertSpecifiedClassFiles extends SearchClassFiles {

	public InsertSpecifiedClassFiles(){
		super();
	}
	public static void main(String[] args){
		//System.out.println("Modification for One Class File");
		//System.out.println("Enter Package Names (e.g. com.example.a)");
		InsertSpecifiedClassFiles ocf = new InsertSpecifiedClassFiles();
		ClassPool cp = ClassPool.getDefault();
		for(int i = 0; i < args.length; i++){
			if(args[i].contains("/")){
				args[i] = splitInput(args[i]);
			}
			try {
				CtClass cc = cp.get(args[i]);
				ocf.insertCodesIntoAllMethods(args[i], cc);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			//System.out.println("args" + i + " " + args[i]);
		}
		System.out.println("DONE ");
	
	}
	public static String splitInput(String str){
		String[] strs = str.split("/");
		String input = new String();
		for(int i = 0;i < strs.length; i++){
			if(strs[i].equals("")){
				continue;
			}else if(strs[i].equals(".")){
				continue;
			}
			int index = strs[i].indexOf(".class");
			if(index != -1){
				strs[i] = strs[i].substring(0, index);
			}
			
			input += strs[i];
			if(i != strs.length - 1)
				input += ".";
		}
		System.out.println("input = " + input);
		return input;
	}
	
}
