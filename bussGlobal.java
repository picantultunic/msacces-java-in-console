import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/*
    acesta este folosit pentru a crea o metoda de a comunica cu display
*/
public class bussGlobal {
    private static String data = "lipsa buss lista";
    private static int cod = -1;
    private static boolean eroareDetectata = false;
    static ArrayList<Integer> multipleErrorCode = new ArrayList<>();

    class ErrorType {
        String errorName = "";
        int errorCode = 0;

        public String toString() {
            return "(" + this.errorCode + ":" + this.errorName + ")";
        }

        ErrorType(int a, String b) {
            this.errorCode = a;
            this.errorName = b;
        }
    }

    static ArrayList<ErrorType> errorList;

    public static void eroare(int cod) {
        for (int i = 0; i < errorList.size(); i++)
            if (errorList.get(i).errorCode == cod) {// exista posibilitatea ca sa nu fie in ordine
                bussGlobal.cod = errorList.get(i).errorCode;
                bussGlobal.data = errorList.get(i).errorName;
                eroareDetectata = true;
                multipleErrorCode.add(bussGlobal.cod);
                break;
            }
    }
    public void eroareSatic(int cod) {
        for (int i = 0; i < errorList.size(); i++)
            if (errorList.get(i).errorCode == cod) {// exista posibilitatea ca sa nu fie in ordine
                bussGlobal.cod = errorList.get(i).errorCode;
                bussGlobal.data = errorList.get(i).errorName;
                eroareDetectata = true;
                multipleErrorCode.add(bussGlobal.cod);
                break;
            }
    }

    public static boolean hasError() {
        return bussGlobal.eroareDetectata;
    }
    
    public static void reseteazaEroarea() {
        bussGlobal.eroareDetectata = false;
        bussGlobal.multipleErrorCode.clear();
    }
    public static void flagReset(){
        bussGlobal.eroareDetectata = false;
    }
    public int returnEroare() {
        return cod;
    }
    public static String returnEroareString(int cod) {
        for (int i = 0; i < errorList.size(); i++)
            if (errorList.get(i).errorCode == cod) {// exista posibilitatea ca sa nu fie in ordine
               return errorList.get(i).errorName;
            }
        return "";
    }
    bussGlobal() {
        String dir = System.getProperty("user.dir");
        dir += "/ErrorList.txt";
        try {
            // tot ce se intampla ii sa citeasca lista de erori
            BufferedReader file = new BufferedReader(new FileReader(dir));
            String s = file.readLine();
            int n = Integer.valueOf(s);
            errorList = new ArrayList<ErrorType>(n);

            while ((s = file.readLine()) != null) {
                errorList.add(new ErrorType(
                        Integer.valueOf(s.substring(0, s.indexOf(" "))),
                        s.substring(s.indexOf(" ") + 1)));
            }
            file.close();
        } catch (Exception e) {
            eroare(0);
        }
    }

    public void debugg() {
        for (int i = 0; i < errorList.size(); i++)
            System.out.println(errorList.get(i));
    }
}
