
public class InstructionMem {
    
    private short[] data; //2^16 - 1 = 65535
    private int size = 0;

    public InstructionMem(){
        data = new short[1024];
    }

    public short[] getData() {
        return data;
    }

    public String toString(){
        String string = "\t [" + data[0];
        for (int i = 1; i < size; i++) {
            string += "," + data[i];
        }
        return string + "]";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void insertIntoInstructionMem(String line){
        String[] instruction = line.split(" ");
        short insertedData = 0;

        switch (instruction[0]){
            case "ADD":
                insertedData = (short)((0b0000 << 12) | insertedData);
                break;

            case "SUB":
                insertedData = (short)((0b0001 << 12) | insertedData);
                break;

            case "MUL":
                insertedData = (short)((0b0010 << 12) | insertedData);
                break;

            case "LDI":
                insertedData = (short)((0b0011 << 12) | insertedData);
                break;

            case "BEQZ":
                insertedData = (short)((0b0100 << 12) | insertedData);
                break;

            case "AND":
                insertedData = (short)((0b0101 << 12) | insertedData);
                break;

            case "OR":
                insertedData = (short)((0b0110 << 12) | insertedData);
                break;

            case "JR":
                insertedData = (short)((0b0111 << 12) | insertedData);
                break;

            case "SLC":
                insertedData = (short)((0b1000 << 12) | insertedData);
                break;

            case "SRC":
                insertedData = (short)((0b1001 << 12) | insertedData);
                break;

            case "LB":
                insertedData = (short)((0b1010 << 12) | insertedData);
                break;

            case "SB":
                insertedData = (short)((0b1011 << 12) | insertedData);
                break;
        }

        int r1 = Integer.parseInt(instruction[1].substring(1));
        insertedData = (short)((r1 << 6) | insertedData);

        if(instruction[2].charAt(0) == 'R'){
            int r2 = Integer.parseInt(instruction[2].substring(1));
            insertedData = (short)((r2) | insertedData);
        }
        else{
            int immediate = Integer.parseInt(instruction[2]);
            insertedData = (short)((immediate) | insertedData);
        }

        data[size] = insertedData;

        size++;
    }

}
