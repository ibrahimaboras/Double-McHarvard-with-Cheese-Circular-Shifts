
// int ans = (x << 2) | y; (concatination)

public class CheesyMcHarvard {

    private InstructionMem iMems;
    private DataMem dMems;

    private Gprs gprs;
    private Sreg sreg;
    private PC pc;

    private int opcode = 0;
    private int r1 = 0;
    private int r2 = 0;
    private int immediate = 0;

    private byte valueR1 = 0;
    private byte valueR2 = 0;


    public CheesyMcHarvard(){
        iMems = new InstructionMem();
        dMems = new DataMem();

        gprs = new Gprs();
        sreg = new Sreg();
        pc = new PC();
    }

    
    public void start(){

    }



    public int fetch() {
        
        short[] data = iMems.getData();
        int instruction = data[pc.getInstToBeExec()];
        
        pc.setInstToBeExec((byte)(pc.getInstToBeExec() + 1));

        return instruction;
    }

    public void decode(int instruction) {

        opcode = (instruction & 0b1111000000000000) >>> 12;
        r1 = (instruction & 0b0000111111000000) >>> 6;
        r2 = (instruction & 0b0000000000111111);
        immediate = (instruction & 0b0000000000111111);

        valueR1 = gprs.getRegisters()[r1];
        valueR2 = gprs.getRegisters()[r2];
    }

    public void execute() {
        
        switch(opcode){
            case 0:
                valueR1 = (byte)(valueR1 + valueR2);
                break;
            case 1:
                valueR1 = (byte)(valueR1 - valueR2);
                break;
            case 2:
                valueR1 = (byte)(valueR1 * valueR2);
                break;
            case 3: 
                valueR1 = (byte)(immediate);
                break;
            case 4:
                if(valueR1 == 0) pc.setInstToBeExec((byte)(pc.getInstToBeExec() + 1 + immediate));
                break;
            case 5:
                valueR1 = (byte)(valueR1 & valueR2);
                break;
            case 6: 
                valueR1 = (byte)(valueR1 | valueR2);
                break;
            case 7: 
                // int ans = (x << 2) | y; (concatination) 
                pc.setInstToBeExec((byte)((valueR1 << 6) | valueR2));
                break;
            case 8:
                valueR1 = (byte)(valueR1 << immediate | valueR1 >>> (8-immediate));
                break;
            case 9:
                valueR1 = (byte)(valueR1 >>> immediate | valueR1 << (8-immediate));
                break;
            case 10:
                valueR1 = dMems.getData()[immediate];
                break;
            case 11:
                dMems.getData()[immediate] = valueR1;

        }

    }
    


    public int getImmediate() {
        return immediate;
    }
    
    public int getOpcode() {
        return opcode;
    }

    public int getR1() {
        return r1;
    }

    public int getR2() {
        return r2;
    }

}