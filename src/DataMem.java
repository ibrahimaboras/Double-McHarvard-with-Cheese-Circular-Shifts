import java.util.HashMap;

public class DataMem {

    HashMap<Integer, Integer> data; // 2048 keys,   2^8 - 1 = 255


    public DataMem(){
        data = new HashMap<>();
    }

    public HashMap<Integer, Integer> getData() {
        return data;
    }
    
}
