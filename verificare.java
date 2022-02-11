final class verificare {
    static String[] datatype = { "boolean", "char", "short", "int", "double", "String" };
    static String[] specialDatatype = { "tabref", "list" };// ELE VIN CU PARANTEZE

    public static boolean validDatatype(String dt) {
        for (String element : datatype)
            if (dt == element)
                return true;

        for (String element1 : specialDatatype)
            if (dt.substring(0, dt.indexOf("(")) == element1)
                return true;
        // aici am obs ca da eroare asa ca nu prea am idee , cred ca ar trebuii sa refaci for loop
        return false;
    }
    public static boolean identicStucturaNouaVeche(){
        return true;
    }
}
