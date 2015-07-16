package bytecode;

public class Sample02 {

	public static void main(String[] args) {
		System.out.println(multiply(5, 7));

	}
	
    static int multiply(int a, int b) throws NullPointerException{
    	int result = a * b;
    	return result;   
    }
}
