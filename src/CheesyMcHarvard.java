import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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

    private int counter = 1;


    
    public static void main(String[] args) throws Exception {

        CheesyMcHarvard cheesy = new CheesyMcHarvard();
        // cheesy.getGprs().setRegisters(1, (byte)130);
        // cheesy.getGprs().setRegisters(2, (byte)130);
        cheesy.run();

    }

    public CheesyMcHarvard(){
        iMems = new InstructionMem();
        dMems = new DataMem();

        gprs = new Gprs();
        sreg = new Sreg();
        pc = new PC();
    }


    public void run() throws Exception{

           File file = new File("src\\resources\\AssemblyCode.txt");
           BufferedReader br = new BufferedReader(new FileReader(file));

           String st;

            while ((st = br.readLine()) != null){
                if(!st.equals(""))
                    iMems.insertIntoInstructionMem(st);
            }


        // Integer.parseInt(String, 2);

        int tempPC = 0;

        int clockCycles = 1;
        int maxClockCycles = 3 + ((iMems.getSize() - 1) * 1);

        short instruction = 0;

        for(int i = clockCycles; i <= maxClockCycles; i++, clockCycles++, counter++){

            System.out.println("Clock Cycle " + clockCycles + " : ");

            System.out.println("\t New Value of Register " + r1 + " : " + valueR1 + "\n"); //Case Store not handled

            if(clockCycles == 1 || counter == 1){
                System.out.println("\t Instruction " + pc.getInstToBeExec() + " is being fetched");

                System.out.println("\n\t\t Inputs: ");
                System.out.println("\t\t\t PC: " + pc.getInstToBeExec());

                instruction = fetch();

                System.out.println("\n\t\t Output: ");
                System.out.println("\t\t\t Instruction: " + Integer.toBinaryString(instruction & 0xFFFF));
            }
            else if(clockCycles == 2 || counter == 2){
                System.out.println("\t Instruction " + (pc.getInstToBeExec() - 1) + " is being decoded");

                System.out.println("\n\t\t Inputs: ");
                System.out.println("\t\t\t Instruction: " + Integer.toBinaryString(instruction & 0xFFFF));

                decode(instruction);

                System.out.println("\n\t\t Output: ");
                System.out.println("\t\t\t Opcode: " + opcode);
                System.out.println("\t\t\t R1: " + r1);
                System.out.println("\t\t\t R2: " + r2);
                System.out.println("\t\t\t Immediate: " + immediate);
                System.out.println("\t\t\t Value of R1: " + valueR1);
                System.out.println("\t\t\t Value of R2: " + valueR2);

                if(clockCycles < maxClockCycles - 1){
                    tempPC = pc.getInstToBeExec();

                    System.out.println("\t Instruction " + pc.getInstToBeExec() + " is being fetched");

                    System.out.println("\n\t\t Inputs: ");
                    System.out.println("\t\t\t PC: " + pc.getInstToBeExec());
    
                    instruction = fetch();

                    System.out.println("\n\t\t Output: ");
                    System.out.println("\t\t\t Instruction: " + Integer.toBinaryString(instruction & 0xFFFF));
                }
            }
            else{
                if(clockCycles == maxClockCycles)
                    System.out.println("\t Instruction " + (pc.getInstToBeExec() - 1) + " is being executed\n");
                else 
                    System.out.println("\t Instruction " + (pc.getInstToBeExec() - 2) + " is being executed\n");

                execute();
                
                System.out.println("\t Output: ");

                System.out.println("\t\t Final Value: " + valueR1);

                // Print SREG

                System.out.println("\t Status Register : " + sreg.getStatus());
                System.out.println("\t\t Carry Flag : " + sreg.getCarry());
                System.out.println("\t\t Overflow Flag : " + sreg.getOverFlow());
                System.out.println("\t\t Negative Flag : " + sreg.getNegative());
                System.out.println("\t\t Sign Flag : " + sreg.getSign());
                System.out.println("\t\t Zero Flag : " + sreg.getZero());
                System.out.println();

                sreg.setStatus((byte)0);

                if(counter != 0){

                if(clockCycles < maxClockCycles){
                    System.out.println("\t Instruction " + (pc.getInstToBeExec() - 1) + " is being decoded");

                    System.out.println("\n\t\t Inputs: ");
                    System.out.println("\t\t\t Instruction: " + Integer.toBinaryString(instruction & 0xFFFF));
    
                    decode(instruction);
 
                    System.out.println("\n\t\t Output: ");
                    System.out.println("\t\t\t Opcode: " + opcode);
                    System.out.println("\t\t\t R1: " + r1);
                    System.out.println("\t\t\t R2: " + r2);
                    System.out.println("\t\t\t Immediate: " + immediate);
                    System.out.println("\t\t\t Value of R1: " + valueR1);
                    System.out.println("\t\t\t Value of R2: " + valueR2);
                }
                if(clockCycles < maxClockCycles - 1){
                    tempPC = pc.getInstToBeExec();
                    
                    System.out.println("\t Instruction " + pc.getInstToBeExec() + " is being fetched");

                    System.out.println("\n\t\t Inputs: ");
                    System.out.println("\t\t\t PC: " + pc.getInstToBeExec());

                    instruction = fetch();
    
                    System.out.println("\n\t\t Output: ");
                    System.out.println("\t\t\t Instruction: " + Integer.toBinaryString(instruction & 0xFFFF));
                }
            }

            else maxClockCycles++;

            }
        }

        // Final Printing

        System.out.println("\nData and Memories after execution: \n");

        // Print PC

        System.out.println("\nPC Register : " + pc.getInstToBeExec() + "\n");

        // Print SREG

        System.out.println("Status Register : " + sreg.getStatus());
        System.out.println("\t Carry Flag : " + sreg.getCarry());
        System.out.println("\t Overflow Flag : " + sreg.getOverFlow());
        System.out.println("\t Negative Flag : " + sreg.getNegative());
        System.out.println("\t Sign Flag : " + sreg.getSign());
        System.out.println("\t Zero Flag : " + sreg.getZero());
        System.out.println();

        // Print GPRS

        System.out.println("General Purpose Register: ");

        for (int i = 0; i < gprs.getRegisters().length; i++) {
            System.out.println("\t Register " + i + " : " + gprs.getRegisters()[i]);
        }

        // Print Memories

        System.out.println("\nData Memory : ");
        System.out.println(dMems);

        System.out.println("Instruction Memory : ");
        System.out.println(iMems);

    }


    public short fetch() {
        
        short[] data = iMems.getData();
        short instruction = data[pc.getInstToBeExec()];
        
        pc.setInstToBeExec((short)(pc.getInstToBeExec() + 1));

        return instruction;
    }

    public void decode(int instruction) {

        opcode = (instruction & 0b1111000000000000) >>> 12;
        r1 = (instruction & 0b0000111111000000) >>> 6;
        r2 = (instruction & 0b0000000000111111);
        int immediateSign = (instruction & 0b0000000000100000) >>> 5;
        if(immediateSign == 1) immediate = (instruction | 0b11111111111111111111111111000000);
        else immediate = (instruction & 0b0000000000111111);

        valueR1 = gprs.getRegisters()[r1];
        valueR2 = gprs.getRegisters()[r2];
    }

    public void execute() { // 0b00010000 or sreg.get
        
        int signOfR1 = (Byte.toUnsignedInt(valueR1) & 0b10000000) >> 7;
        int signOfR2 = (Byte.toUnsignedInt(valueR2) & 0b10000000) >> 7;

        switch(opcode){
            case 0:
                byte add = (byte)(valueR1 + valueR2);

                // Carry Flag:

                int carryCheck = Byte.toUnsignedInt(valueR1) + Byte.toUnsignedInt(valueR2);
                carryCheck = (carryCheck & 0b00000000000000000000000100000000) >> 8;
                if(carryCheck == 1)  // 000 1 0000
                    sreg.setStatus((byte)(0b00010000 | sreg.getStatus()));
                   
                // Overflow Flag:

                int addSign = (Byte.toUnsignedInt(add) & 0b10000000) >> 7;

                if(signOfR1 == signOfR2 && signOfR1 != addSign)
                    sreg.setStatus((byte)(0b00001000 | sreg.getStatus()));
                

                // Negative Flag:

                if(addSign == 1)
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Sign Flag:

                int addXOR = sreg.getNegative() ^ sreg.getOverFlow();
                if(addXOR == 1)
                    sreg.setStatus((byte)((0b00000010) | sreg.getStatus()));

                // Zero Flag:

                if(add == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));


                valueR1 = (byte)(valueR1 + valueR2);
                gprs.setRegisters(r1, valueR1);
                break;
            case 1:
                byte sub = (byte)(valueR1 - valueR2);

                // Overflow Flag:

                int subSign = (Byte.toUnsignedInt(sub) & 0b10000000) >> 7;

                if(signOfR1 != signOfR2 && signOfR2 == subSign)
                    sreg.setStatus((byte)(0b00001000 | sreg.getStatus()));

                // Negative Flag:
                
                if(subSign == 1)   
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Sign Flag:

                int subXOR = sreg.getNegative() ^ sreg.getOverFlow();
                if(subXOR == 1)
                    sreg.setStatus((byte)((0b00000010) | sreg.getStatus()));

                // Zero Flag:

                if(sub == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));


                valueR1 = (byte)(valueR1 - valueR2);
                gprs.setRegisters(r1, valueR1);
                break;
            case 2:
                byte mult = (byte)(valueR1 * valueR2);

                int multSign = (Byte.toUnsignedInt(mult) & 0b10000000) >> 7;

                // Negative Flag:

                if(multSign == 1)  
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                //Zero Flag

                if(mult == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));


                valueR1 = (byte)(valueR1 * valueR2);
                gprs.setRegisters(r1, valueR1);
                break;
            case 3: 
                valueR1 = (byte)(immediate);
                gprs.setRegisters(r1, valueR1);
                break;
            case 4:
                if(valueR1 == 0){
                    pc.setInstToBeExec((short)(pc.getInstToBeExec() - 1 + immediate));
                    //System.out.println(pc.getInstToBeExec());
                    counter = 0;
                }
                gprs.setRegisters(r1, valueR1);
                break;
            case 5:
                byte and = (byte)(valueR1 & valueR2);

                int andSign = (Byte.toUnsignedInt(and) & 0b10000000) >> 7;

                // Negative Flag:
                
                if(andSign == 1)  
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Zero Flag:

                if(and == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));


                valueR1 = (byte)(valueR1 & valueR2);
                gprs.setRegisters(r1, valueR1);
                break;
            case 6: 
                byte or = (byte)(valueR1 | valueR2);

                int orSign = (Byte.toUnsignedInt(or) & 0b10000000) >> 7;

                // Negative Flag:
                
                if(orSign == 1)  
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Zero Flag:

                if(or == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));

                valueR1 = (byte)(valueR1 | valueR2);
                gprs.setRegisters(r1, valueR1);
                break;
            case 7: 
                // int ans = (x << 2) | y; (concatination) 
                pc.setInstToBeExec((short)((valueR1 << 6) | valueR2));
                counter = 1;
                gprs.setRegisters(r1, valueR1);
                break;
            case 8:
                byte slc = (byte)(valueR1 << immediate | valueR1 >>> (8-immediate));

                int slcSign = (Byte.toUnsignedInt(slc) & 0b10000000) >> 7;

                // Negative Flag:
                
                if(slcSign == 1)  
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Zero Flag:

                if(slc == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));

            
                valueR1 = (byte)(valueR1 << immediate | valueR1 >>> (8-immediate));
                gprs.setRegisters(r1, valueR1);
                break;
            case 9:
                byte src = (byte)(valueR1 >>> immediate | valueR1 << (8-immediate));

                int srcSign = (Byte.toUnsignedInt(src) & 0b10000000) >> 7;

                // Negative Flag:
                
                if(srcSign == 1)  
                    sreg.setStatus((byte)(0b000000100 | sreg.getStatus()));

                // Zero Flag:

                if(src == 0)
                    sreg.setStatus((byte)(0b00000001 | sreg.getStatus()));



                valueR1 = (byte)(valueR1 >>> immediate | valueR1 << (8-immediate));
                gprs.setRegisters(r1, valueR1);
                break;
            case 10:
                valueR1 = dMems.getData()[immediate];
                gprs.setRegisters(r1, valueR1);
                break;
            case 11:
                dMems.setData(immediate, valueR1);
                System.out.println("\t New Stored Vlaue at Address " + immediate + " : " + valueR1 + "\n");
                break;

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


    public InstructionMem getiMems() {
        return iMems;
    }


    public DataMem getdMems() {
        return dMems;
    }


    public Gprs getGprs() {
        return gprs;
    }


    public Sreg getSreg() {
        return sreg;
    }


    public PC getPc() {
        return pc;
    }


    public byte getValueR1() {
        return valueR1;
    }


    public byte getValueR2() {
        return valueR2;
    }

    

}