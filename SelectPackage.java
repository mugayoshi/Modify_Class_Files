import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SelectPackage extends ModifyClassFiles {
	public static void main(String[] args){
		SelectPackage sp = new SelectPackage();
		sp.searchDirectory(sp.directoryPath);
		int classNum = sp.clsPckgNames.size();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input_class;
		try {
			System.out.print("please input package name: ");
			input_class = br.readLine();
			for(int i = 0; i < classNum; i++){
				String classname = sp.clsPckgNames.get(i);
				if(classname.contains(input_class) == false){
					continue;
				}
				if(sp.avoidItself(classname)){
					sp.insertCodesIntoAllMethods(classname);
				}
				
			}
			System.out.println("Writing Codes to Class Files has done !!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
}
