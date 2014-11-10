import java.io.File;


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
			System.out.println("Usage: You Need to Input Class Files that You Wanna modify");
			return ;
		}
		/*for(int i = 0; i < mccf.modifiedClass.length; i++)
			System.out.println(mccf.modifiedClass[i]);*/
		mccf.searchClassFile(mccf.directoryPath);
		
		mccf.showClasses();
		
		
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
