package au.edu.anu.rscs.aot.archetype;

public class Test {

	public static void thing(String s,Object...args) {
		for (int i =0;i<args.length;i++) {
			System.out.println(args[i].getClass());
		}
		
	}
	public static void main(String[] args) {
		Integer i = 4;
		Double d = 3.0;
		thing(" ",i,d);

	}

}
