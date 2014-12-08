import java.util.*;
public class Objeto {
	String className;
	String methodName;
	ArrayList<String> fieldList;
	String statement;
	String checkedMethod;
	ArrayList<String> methodCallList;
	String methodCallLog;
	public Objeto(String c, String m, String checked){
		this.className = c;
		this.methodName = m;
		this.fieldList = new ArrayList<String>();
		this.statement = new String();
		this.checkedMethod = checked;
		this.methodCallList = new ArrayList<String>();
		this.methodCallList.add("---- CLASS: " + className + " METHOD: " + this.checkedMethod + " ----");
		this.methodCallLog = "---- CLASS: " + className + " METHOD: " + this.checkedMethod + " ----\n";
	}
	public void makeStatement(String methodCall){
		Random r = new Random();
		int random = r.nextInt(1000);
		
		this.statement = "System.out.println(\"BEFORE " + methodCall + " " + random  + "\");";
		this.statement += "System.out.println(\"\tmakeStatement\");";
		/*for(int i = 0; i < this.fieldList.size(); i++){
			//this.statement += "System.out.println(\"\t" + this.fieldList.get(i) + " = \" + $0." + this.fieldList.get(i) + ");";
			this.statement += "System.out.println(\"\tmakeStatement\");";
		}*/
		System.out.println("make statement: " + this.statement);
	}
	public String makeStatementAndroid(String methodCall){
		Random r = new Random();
		int random = r.nextInt(1000);
		this.methodCallList.add(methodCall);
		
		String log_before = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " + random + " before " + methodCall + " Called from " + this.checkedMethod + " of " + this.className + "\");";
		
		String log_after = "if($0 != null) android.util.Log.d(\"ModifyClassFiles.makeStatement(String)\", "
				+ "\"ID: " +  random + " after " + methodCall + " Backed to  " + this.checkedMethod + " of " + this.className + "\");";
		String statement = "{" + log_before + "$_ = $proceed($$);" + log_after + "}";
		return statement;
	}
	public void register(String className, String methodName, int line){
		String str = line  + " " + className + " " + methodName; 
		//this.methodCallList.add(str);
		this.methodCallLog += str + "\n";
 	}
}
