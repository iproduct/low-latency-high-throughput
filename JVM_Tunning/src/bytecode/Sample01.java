package bytecode;

public class Sample01 {

	public static void main(String[] args) {
		conversion();

	}
	
    static void conversion() {
        byte byteVal = 0;
        int intVal = 124;
        while (true) {
            ++intVal;
            byteVal = (byte) intVal;
            intVal *= -1;
            byteVal = (byte) intVal;
            intVal *= -1;
        }
    }
}
