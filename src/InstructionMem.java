import java.util.HashMap;

public class InstructionMem {
    
    private short[] data; //2^16 - 1 = 65535

    public InstructionMem(){
        data = new short[1024];
    }

    public short[] getData() {
        return data;
    }

    public static void main(String[] args) {

    }

}
