import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class creareFisiere {
    public String returneazaPathDatabase(String path){// imi da pathul bun 
        if(!path.equals("")||!path.equals(" ")){
            File f = new File(path);
            if(!f.exists())
                path=System.getProperty("user.dir")+"/db";
        }
        else{
            path=System.getProperty("user.dir")+"/db";
        }
        return path;
    }
    public void salveazaDatabase(bussGlobal buss,Database db){
        String path=returneazaPathDatabase(db.path);
        db.path=new String(path);
        String name=db.nume;
        File f=new File(path+"/"+name);
        // if(f.exists()){
        //     buss.eroare(8);
        //     return;
        // }
        path+="/"+name;
            
        try {//pentru crearea db
            f.mkdir();
            f=new File(path+"/"+"tabele");
            f.mkdir();
            f=new File(path+"/"+"configurare.txt");
            f.createNewFile();
            String data="";
            data+="nume:"+db.nume+"\n";
            data+="nrtabele:"+db.listatabele.size()+"\n";
            data+="contorTabele:"+db.contorTabele+"\n";
            BufferedWriter buffer = new BufferedWriter(new FileWriter(path+"/configurare.txt"));
            buffer.write(data);
            buffer.close();

            for(int i=0;i<db.listatabele.size();i++){
                String p=path+"/tabele/";
                Tabel t=db.listatabele.get(i);
                String n=i+"_"+t.numeTabel;
                f=new File(p+n);
                f.mkdir();
                f=new File(p+n+"/configuraref.txt");
                f.createNewFile();
                BufferedWriter b=new BufferedWriter(new FileWriter(p+n+"/configuraref.txt"));
                data="";
                data+="numeTabel:"+t.numeTabel+"\n";
                data+="id:"+t.id+"\n";
                data+="idelemente:"+t.idelemente+"\n";
                data+="subtabeleCounter:"+t.subtabeleCounter+"\n";
                b.write(data);
                b.close();
                f=new File(p+n+"/structura.txt");
                f.createNewFile();
                b=new BufferedWriter(new FileWriter(p+n+"/structura.txt"));
                data="";
                for(int index=0;index<t.structura.coloane.size();index++)
                    data+=t.structura.numeColoana.get(index)+":"+t.structura.coloane.get(index)+"\n";
                b.write(data);
                b.close();
                //tot ce o fost mai sus o fost pentru a memora parametrii fiecarui tabel 
                f=new File(p+n+"/subtabele");
                f.mkdir();
                p=p+n+"/subtabele/";
                //mai jos scriu in fiecare subtabel fisier
                for(int index=0;index<t.subtabel.size();index++){
                    if(t.subtabel.get(index).rand.size()==0)
                        continue;
                    f=new File(p+index+".txt");
                    f.createNewFile(); 
                    data="";
                    //asta ii un rand
                    for(int j=0;j<t.subtabel.get(index).rand.size();j++){
                        data+=t.subtabel.get(index).rand.get(j).id+" ";
                        Casuta[] c=t.subtabel.get(index).rand.get(j).casuta;
                        for(int k=0;k<c.length;k++){//fiecare element de pe rand
                            data+="#"+c[k];
                        }
                        data+="\n";//pentru urmatorul rand din subtabel
                    }
                    b=new BufferedWriter(new FileWriter(p+index+".txt"));
                    b.write(data);
                    b.close();
                }

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            buss.eroare(3);
            e.printStackTrace();
        }

    }
    public boolean existaDatabase(String path,String nume){
        File f=new File(path+"/"+nume);
        if(!f.exists()){
            path=System.getProperty("user.dir")+"/db";
            f=new File(path+"/"+nume);
            if(!f.exists())
                return false;
        }
        return true;
    }
    public void incarcaDatabase(bussGlobal buss,Database db){
        //presupun ca pathul vine cum trebuie

        String p=db.path+"/"+db.nume+"/",text;
        File f=new File(p);
        try (BufferedReader fisier = new BufferedReader(new FileReader(p+"configurare.txt"))) {
            text = fisier.readLine();
            db.nume=text.substring(text.indexOf(":")+1);
            text = fisier.readLine();
            db.nrtabele=Integer.parseInt(text.substring(text.indexOf(":")+1));
            text = fisier.readLine();
            db.contorTabele=Integer.parseInt(text.substring(text.indexOf(":")+1));

        } catch (IOException e) {
            buss.eroare(5);
            return;
        }
        //pana acum am facut configurarea mare , mai jos o sa fie pentru tabele 
        //sa nu iti la final sa scazi toate alea 
        p+="tabele/";
        f=new File(p);
        String []numetabel=f.list();
        for(int index=0;index<numetabel.length;index++){
            String aa=numetabel[index].substring(numetabel[index].indexOf("_")+1);
            db.addTabel(aa);
            Tabel tab=db.listatabele.get(index);
            String[] s,dt,nm;String aux;
            BufferedReader fisier;
            int k=0;
            //db.listatabele.get(index).id=Integer.parseInt(numetabel[index].substring(0,numetabel[index].indexOf("_")));
            try {
                fisier = new BufferedReader(new FileReader(p+numetabel[index]+"/structura.txt"));
                while ((aux = fisier.readLine()) != null){
                    k++;
                }
                s=new String[k];
                dt=new String[k];
                nm=new String[k];
                fisier.close();
                fisier = new BufferedReader(new FileReader(p+numetabel[index]+"/structura.txt"));
                for(int i=0;i<k;i++){
                    s[i]=fisier.readLine();
                    nm[i]=s[i].substring(0,s[i].indexOf(":"));
                    dt[i]=s[i].substring(s[i].indexOf(":"));
                }
                tab.structura= new Structura(k);
                for(int i=0;i<k;i++){
                    tab.structura.numeColoana.add(nm[i]);
                    Casuta c=new Casuta();
                    c.set(dt[i]);
                    tab.structura.coloane.add(c);
                }
            } catch (Exception e) {
                buss.eroare(5);
                return;
            }
            try{
                f=new File(p+numetabel[index]+"/subtabele/");
                s=f.list();
                for(int i=0;i<s.length;i++){// aici citeste din subtabele
                    fisier.close();
                    fisier=new BufferedReader(new FileReader(p+numetabel[index]+"/subtabele/"+s[i]));
                    //verific cate elemente am in fisier
                    k=0;
                    while ((aux = fisier.readLine()) != null){
                        k++;
                    }
                    String[] rand=new String[k];//rand
                    fisier.close();
                    fisier=new BufferedReader(new FileReader(p+numetabel[index]+"/subtabele/"+s[i]));
                    for(int j=0;j<k;j++){// asta citeste randurile
                        rand[j]=fisier.readLine();
                        //int id=Integer.parseInt(rand[j].substring(0, rand[j].indexOf("#")));
                        rand[j]=rand[j].substring(rand[j].indexOf("#")+1);
                        Casuta[] cas=new Casuta[tab.structura.numeColoana.size()];
                        int a;
                        for(a=0;a<tab.structura.numeColoana.size()-1;a++){//coloanele
                            cas[a]=new Casuta();
                            cas[a].set(rand[j].substring(0,rand[j].indexOf("#")));
                           rand[j]=rand[j].substring(rand[j].indexOf("#")+1);
                        }
                        cas[a]=new Casuta();
                        cas[a].set(rand[j]);
                        db.adaugaDateTabel(index, cas);  
                    }
                    db.nrtabele/=2;
                }

                fisier.close();
            } catch (Exception e) {
                e.printStackTrace();
                buss.eroare(5);
                return;
            }
        }
    }
    
}
