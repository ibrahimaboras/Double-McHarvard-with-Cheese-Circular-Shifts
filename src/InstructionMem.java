import java.util.HashMap;

public class InstructionMem {
    
    int[] data; //2^16 - 1 = 65535

    public InstructionMem(){
        data = new int[1024];
    }

    public int[] getData() {
        return data;
    }

}
