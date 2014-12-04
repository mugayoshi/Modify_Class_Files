import java.util.*;
public class Objeto {
	String className;
	String methodName;
	ArrayList<String> list;
	String statement;
	String checkedMethod;
	public Objeto(String c, String m){
		this.className = c;
		this.methodName = m;
		this.list = new ArrayList<String>();
		this.statement = new String();
	}
	public Objeto(String c, String m, String checked){
		this.className = c;
		this.methodName = m;
		this.list = new ArrayList<String>();
		this.statement = new String();
		this.checkedMethod = checked;
	}
	public void makeStatement(){
		this.statement = "System.out.println(\"BEFORE " + this.methodName + "\");";
		for(int i = 0; i < this.list.size(); i++){
			this.statement += "System.out.println(\"\t" + this.list.get(i) + " = \" + $0." + this.list.get(i) + ");";
		}
		System.out.println("make statement: " + this.statement);
	}
	public void makeStatementAndroid(){
		this.statement = "android.util.Log.d(\"ModifyClassFiles.insertCodes(String)\", "
				+ "$0.toString() + \"is Executing \"" + this.methodName + " in " + this.checkedMethod + " of " + this.className + ");";
	}
}
