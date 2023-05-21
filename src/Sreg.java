public class Sreg {
    private byte status; // a5erna 0 -> 31     10100010
    
    public Sreg() {
        this.status = 0;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
    
    public int getCarry(){
        return (0b00010000 & status) >> 4;
    }

    public int getOverFlow(){
        return (0b00001000 & status) >> 3;
    }

    public int getNegative(){
        return (0b00000100 & status) >> 2;
    }

    public int getSign(){
        return (0b00000010 & status) >> 1;
    }

    public int getZero(){
        return (0b00000001 & status);
    }
    
}
