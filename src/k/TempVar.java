package k;

/**
 * Created by KevAdmin on 4/28/2015.
 */
public class TempVar {

    String temp,variable,addr;

    TempVar(int num,String variable){
        this.variable = variable;
        temp = "T"+num;
        addr="00";
    }//..

    public void finalize(String p1,String p2){
        temp=p1;addr=p2;
    }//..

    public String[] getAddress(){
        String [] fullAddress = new String[2];
        fullAddress[0]=temp;
        fullAddress[1]=addr;
        return fullAddress;
    }//..

}// TempVar
