public class Sreg {
    private byte status; // a5erna 0 -> 31

    public Sreg() {
        this.status = 0;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
    
}
