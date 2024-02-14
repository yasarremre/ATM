/** @author: EMRE YASAR
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;

public class ATM_20210808602 {
    public static void main(String[] args) {
        String ATM_4 = args[0];
        String accountInfo = ATM_4 + "_AccountInfo.txt";
        String acoountInfoOut = ATM_4 + "_AccountInfoOut.txt";
        String transferInfo = ATM_4 + "_TransferInfo.txt";
        int arrLeng = countAccounts(accountInfo);
        int[] acctNums = new int[arrLeng];
        String[] names = new String[arrLeng];
        String[] surnames = new String[arrLeng];
        double [] balances = new double[arrLeng];


        readAccountInfo(acctNums, names, surnames, balances, accountInfo);
        transferlog(acctNums, transferInfo,ATM_4, balances);
        writeAccountInfo(acctNums, names, surnames, balances, acoountInfoOut);

    }

    public static int countAccounts(String filename) {
        File file = new File(filename);
        int counter = 0;
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                counter++;
                input.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return counter;
    }

    public static void readAccountInfo(int[] acctNums, String[] names, String[] surnames,
                                       double[] balances, String filename) {
        File file = new File(filename);
        int selector = 0;
        try {
            Scanner input = new Scanner(file);
            while(input.hasNextLine()) {
                String[] infoArr = input.nextLine().split(" ");
                acctNums[selector] = Integer.parseInt(infoArr[0]);
                names[selector] = infoArr[1];
                surnames[selector] = infoArr[2];
                balances[selector] = Double.parseDouble(infoArr[3]);
                selector++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean deposit(double[] balance,int index , double amount){
        if(isDepositValid(amount)){
            balance[index] += amount;
            return true;
        }
        else return false;
    }

    public static boolean withdrawal(double[] balance,int index , double amount){
        if(isWithdrawalValid(balance[index],amount) ){
            balance[index] -= amount;
            return true;
        }
        return false;
    }

    public static int transfer(int[] acctNums,double[] balances,int acctNumFrom,int acctNumTo,double amount) {
        int accToIndex = findAcct(acctNums, acctNumTo);
        int accFromIndex = findAcct(acctNums, acctNumFrom);
        if(accToIndex == -1)
            return 1;
        else if(accFromIndex == -1)
            return 2;
        else if(deposit(balances, accToIndex, amount) && withdrawal(balances, accFromIndex, amount)) {
            return 0;
        }
        return 3;

    }

    public static void writeAccountInfo(int[] acctNums,String[] names,String[] surnames,
                                        double[] balances,String filename) {
        try {
            PrintWriter writer = new PrintWriter(filename);
            for (int i = 0; i <names.length; i++) {
                String accOutInfo = acctNums[i] + " " + names[i] + " " + surnames[i] + " " + balances[i];
                writer.println(accOutInfo);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void transferlog(int[] acctNums ,String transferInfo ,String filename,double[] balances){
        String[] items = {"STX - Transfer Successful" , "TNF - To Account not found" ,
                "FNF - From Account not found" , "NSF - Insufficient Funds"};
        File newfile = new File(transferInfo);
        try {
            Scanner input = new Scanner(newfile);
            PrintWriter writer = new PrintWriter(filename + ".log");
            while(input.hasNextLine()){
                String[] selector = input.nextLine().split(" ");
                int transferResult = transfer(acctNums, balances, Integer.parseInt(selector[1]), Integer.parseInt(selector[2]),
                        Double.parseDouble(selector[3]));

                String write = "Transfer " + selector[0] + " resulted in code " + transferResult +": " + items[transferResult];
                writer.println(write);
            }
            writer.close();
            input.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public static int findAcct(int[] acctNums, int acctNum) {
        for(int i = 0 ;i < acctNums.length ;i++) {
            if (acctNum == acctNums[i])
                return i;
        }
        return -1;
    }

    public static boolean isDepositValid(double depoAmo) {
        if (depoAmo > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isWithdrawalValid(double balance, double withAmo) {
        if (balance - withAmo >= 0  && withAmo > 0){
            return true;
        }
        else{
            return false;
        }

    }
}
