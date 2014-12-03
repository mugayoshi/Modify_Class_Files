import java.util.*;
public class Objeto {
	String className;
	String methodName;
	ArrayList<String> list;
	String statement;
	public Objeto(String c, String m){
		this.className = c;
		this.methodName = m;
		this.list = new ArrayList<String>();
		this.statement = new String();
	}
	public void makeStatement(){
		for(int i = 0; i < this.list.size(); i++){
			this.statement += "System.out.println(\"" + this.list.get(i) + " = \" + $0." + this.list.get(i) + ");";
		}
		System.out.println("make statement: " + this.statement);
	}
}
