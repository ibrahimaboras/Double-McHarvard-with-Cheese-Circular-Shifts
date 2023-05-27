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
        if(index < 0) index = index * -1;
        data[index] = newData;
    }

    // @Override
    // public String toString() {
    //     String string = "[" + data[0] + ", ";
    //     for(int i = 1; i < data.length - 1; i++)
    //         string += i + "(" + data[i] + ")" + ", ";

    //     string += "2047" + "(" + data[2047] + ")" + "]\n";
    //     return string;
    // }
    

    @Override
    public String toString() {
        String string = "";
        for(int i = 0; i < data.length - 1; i++)
            string += "\t Data At Memory " + i + ": " + data[i] + "\n" + "------------------------------------------\n";

        //string += "2047" + "(" + data[2047] + ")" + "]\n";
        return string;
    }
}
