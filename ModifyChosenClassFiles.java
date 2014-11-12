import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class ModifyChosenClassFiles extends ModifyClassFiles {
	String[] modifiedClass;
	public ModifyChosenClassFiles(String[] args){
		super();
		//this.input = new File(".").getAbsolutePath();
		this.modifiedClass = new String[args.length];
		for(int i = 0; i < args.length; i++){
			this.modifiedClass[i] = args[i];
		}

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ModifyChosenClassFiles mccf = new ModifyChosenClassFiles(args);
		if(args.length < 1){
			System.out.println("Usage: You Need to Input Class Files that You're Looking for");
			return ;
		}
		/*for(int i = 0; i < mccf.modifiedClass.length; i++)
			System.out.println(mccf.modifiedClass[i]);*/
		
		
		try{
			mccf.searchClassFile(mccf.directoryPath);
			mccf.showClasses();
			System.out.println("Do you wanna insert codes ?");
			System.out.print("yes or no: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String answer = br.readLine();
			if(answer.toLowerCase().contains("y") || answer.toLowerCase().equals("yes")){
				int classNum = mccf.classFiles.size();
				for(int i = 0; i < classNum; i++){
					String classPackageName = mccf.clsPckgNames.get(i);
					int j = i + 1;
					System.out.println(j + "/" + classNum+ " " +  classPackageName);
					
					mccf.insertCodes(classPackageName);
				}
				System.out.println("Insert Codes has Done");
			}else{
				System.out.println("Done !");
			}
		}catch(IOException e){
			
		}
		
	}
	public void searchClassFile(String dirPath){
		
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(checkFile(files[i]) == false){
				searchClassFile(files[i].getAbsolutePath());
			}
			
		}
	}
	public boolean checkFile(File file){
		String fileName = file.getName();
		if(file.isDirectory()){
//			System.out.println("Directory: " + file.getName());
			return false;
		}
		//System.out.println("file name = " + fileName);
		for(int i = 0; i < this.modifiedClass.length; i++){
			if(fileName.indexOf(this.modifiedClass[i]) != -1 && fileName.contains(".class")){
				addClassFile(file, this.noSuperClass);
			}
		}
		return true;
	}
}
