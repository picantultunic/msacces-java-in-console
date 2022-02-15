final class verificare {
    static String[] datatype = { "Boolean", "Character","Short", "Integer", "Double", "String" , "Byte" };
    static String[] specialDatatype = { "tabref", "list" };// ELE VIN CU PARANTEZE
    // pentru specialDatatype ar trebuii sa

    public static boolean validDatatype(String dt) {
        // mentine pentru exemplu in viitor
        for (String element : datatype)
            if (dt == element)
                return true;

        for (String element1 : specialDatatype)
            if (dt.substring(0, dt.indexOf("(")) == element1)
                return true;
        // aici am obs ca da eroare asa ca nu prea am idee , cred ca ar trebuii sa
        // refaci for loop
        return false;
    }

    public static boolean identicStucturaNouaVeche() {
        return true;
    }

    public static String returnDatatype(Casuta c) {
        // cand o sa adaug sdt atunci o sa trebuiasca sa adaug mai multe chestii
        if(c.slot.data==null)
        return "Null";
        String dt = ((Object) c.slot.data).getClass().getSimpleName().toString();
        return dt;
    }
    public static boolean aprobRand(Structura struct, Casuta[] c) {
        if (struct.coloane.size() != c.length) {
            bussGlobal.eroare(20);
            return false;
        }
        for (int i = 0; i < c.length; i++)// cand o sa fie sdt atunci o sa trebuiasca sa mai fie si un else
            if (!struct.coloane.get(i).equals(returnDatatype(c[i])))
                return false;
        return true;
    }
    public static void transformareDate(Structura struct, Casuta[] c){
        if (struct.coloane.size() != c.length) {
            bussGlobal.eroare(20);
            return ;
        }
        for(int i=0;i<c.length;i++){
            String dt=struct.coloane.get(i).toString();
            if(!dt.equals(returnDatatype(c[i]))&&c[i]!=null){
                      if(dt.equals("String")){
                    c[i].set(c[i].toString());
                }else if(dt.equals("Boolean")){
                    boolean a;
                    try {//string->boolean
                        a=Boolean.parseBoolean(c[i].slot.data.toString());
                    } catch (Exception e) {
                        bussGlobal.eroare(21);
                        return;
                    }
                    c[i].set(a);
                }else if(dt.equals("Integer")){
                    int a;
                    try {//string->boolean
                        a=Integer.parseInt(c[i].slot.data.toString());
                    } catch (Exception e) {
                        bussGlobal.eroare(21);
                        return;
                    }
                    c[i].set(a);
                }else if(dt.equals("Double")){
                    double a;
                    try {//string->boolean
                        a=Double.parseDouble(c[i].slot.data.toString());
                    } catch (Exception e) {
                        bussGlobal.eroare(21);
                        return;
                    }
                    c[i].set(a);
                }else if(dt.equals("Character")){
                    c[i].set(c[i].toString().charAt(0));
                }
                
            }
        }
            
    }
}
