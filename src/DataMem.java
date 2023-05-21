import java.util.HashMap;

public class DataMem {

    private byte[] data; // 2048 keys,   2^8 - 1 = 255


    public DataMem(){
        data = new byte[2048];
    }

    public byte[] getData() {
        return data;
    }

    public void setData(int index, byte newData) {
        data[index] = newData;
    }
    
}
