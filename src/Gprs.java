public class Gprs {  // 8 bits (255)
    private byte[] registers; // may change to hashmap

    public Gprs(){
        registers = new byte[64];
    }

    public byte[] getRegisters() {
        return registers;
    }

}
