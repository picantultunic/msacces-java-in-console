import java.util.ArrayList;

public class Structura{
    int lenght;
    boolean eroare=false;
    //Casuta[] coloane;
    ArrayList<Casuta> coloane=new ArrayList<>();
    ArrayList<String> numeColoana=new ArrayList<>();
    public String toString(){	
        String data="";
        if(coloane==null)
            return "Nullll";
        for(int i=0;i<coloane.size();i++)
             data+="("+numeColoana.get(i)+":"+coloane.get(i).slot+")";
        
        return data;
    }
    Structura(int n){
        this.lenght=n;
        //aici ar trebuii sa fie arraylist
        coloane=new ArrayList<>(n);
        numeColoana=new ArrayList<>(n);
    }
    public String[] returnDatatype(){
        String[] dt=new String[coloane.size()];
        for(int i=0;i<dt.length;i++)
            dt[i]=new String(coloane.get(i).toString());
        return dt;
    }
}