import java.util.*;
public class Objeto {
	String className;
	String methodName;
	ArrayList<String> fieldList;
	String statement;
	String checkedMethod;
	ArrayList<String> logList;
	public Objeto(String c, String m){
		this.className = c;
		this.methodName = m;
		this.fieldList = new ArrayList<String>();
		this.statement = new String();
		this.logList = new ArrayList<String>();

	}
	public Objeto(String c, String m, String checked){
		this.className = c;
		this.methodName = m;
		this.fieldList = new ArrayList<String>();
		this.statement = new String();
		this.checkedMethod = checked;
		this.logList = new ArrayList<String>();

	}
	public void makeStatement(String methodCall){
		Random r = new Random();
		int random = r.nextInt(1000);
		
		this.statement = "System.out.println(\"BEFORE " + this.methodName + " " + random  + "\");";
		for(int i = 0; i < this.fieldList.size(); i++){
			this.statement += "System.out.println(\"\t" + this.fieldList.get(i) + " = \" + $0." + this.fieldList.get(i) + ");";
		}
		System.out.println("make statement: " + this.statement);
	}
	public String makeStatementAndroid(){
		Random r = new Random();
		int random = r.nextInt(1000);
		String log_before = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " + random + " Called from " + this.checkedMethod + " of " + this.className + "\");";
		
		String log_after = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " +  random + " Backed to  " + this.checkedMethod + " of " + this.className + "\");";
		String statement = "{" + log_before + "$_ = $proceed($$);" + log_after + "}";
		return statement;
	}
}
