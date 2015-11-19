import java.util.*;

import javassist.expr.MethodCall;
public class InsertedLog {
	String className;
	String methodName;
	ArrayList<String> fieldList;
	String statement;
	String checkedMethod;
//	ArrayList<String> methodCallList;
	String methodCallLog;
	public InsertedLog(String c, String m, String checked){
		this.className = c;
		this.methodName = m;
		this.statement = new String();
		this.checkedMethod = checked;
//		this.methodCallList = new ArrayList<String>();
//		this.methodCallList.add("---- CLASS: " + className + " METHOD: " + this.checkedMethod + " ----");
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
	public String makeStatementAndroid(MethodCall m){
		String methodCallName = m.getMethodName();
		String methodCallClass = m.getClassName();
		String signature = m.getSignature();
		Random r = new Random();
		int random = r.nextInt(1000);
//		this.methodCallList.add(methodCallName);
		
		String log_before = "if($0 != null) android.util.Log.d(\"ModifyClassFiles\", "
				+ "\"ID: " + random + " Before " + methodCallName + " [" + signature + "] of " + methodCallClass + " Called From " + this.checkedMethod + " In " + this.className + "\");";
		
		String log_after = "if($0 != null) android.util.Log.d(\"ModifyClassFiles\", "
				+ "\"ID: " +  random + " After " + methodCallName + " Backed To  " + this.checkedMethod + " In " + this.className + "\");";
		String statement = "{" + log_before + "$_ = $proceed($$);" + log_after + "}";
		return statement;
	}
	public void register(String className, String methodName, int line){	
		String str = line  + " " + className + " " + methodName; 
		//this.methodCallList.add(str);
		this.methodCallLog += str + "\n";
 	}
}
