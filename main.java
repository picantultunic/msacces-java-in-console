

import java.util.Scanner;

public class main {
	String pestele="pesteleeeee";
	public static void main(String[] args){
		System.out.println("***Acces in terminal . Va rog sa introduceti numarul pentru urmatoarele decizi:");
		System.out.println("***o sa trebuiasca sa adaugati informatii despre baza de date care vreti sa o accesati");
		System.out.println("***daca ea nu exista in calculator atunci ea o sa fie creata");
		
		bussGlobal buss=new bussGlobal();
		Scanner input= new Scanner(System.in);
		String numeBazaDate,path,decizie;
		boolean seCreaza=false;
		Database db;
		
		System.out.println("   doriti sa creati o baza de date?(Y/N) \n   (caz contrar creati una) introduceti X pentru iesire");
		decizie=input.nextLine();
		
		switch(decizie){
			default:
				throw new IllegalArgumentException("trebuie sa alegi y sau n");
			case"x":case"X":
				throw new IllegalArgumentException("ai iesit din program");
			case"y":case"Y":
				seCreaza=true;break;
			case"n":case"N":
				seCreaza=false;break;
		}
		System.out.println("introduceti numele bazei de date");
		numeBazaDate=input.nextLine();
		System.out.println("scrieti pathul unde vreti sa salvati baza de date(doar apasati Enter pentru default)");
		path=input.nextLine();
		//path=path+"\\"+numeBazaDate;
		
		//System.out.println(path);
		db=new Database(buss,path,numeBazaDate,seCreaza);
		//
		if(seCreaza){
			db.addTabel("cont");
			String[] str={"Integer","String","Boolean"},nmstr={"cnp","serie","Sex"};
			db.seteazaStructuraTabel("cont", str,nmstr);
			db.addTabel("persoane");
			String[] strr={"String","String"},nmstrr={"nume","prenume"};
			db.seteazaStructuraTabel("persoane", strr,nmstrr);
			//db.debugg();
			int cnp=215674345,serie=563824;

			db.conecteazaTabele("cont", "persoane");
			db.debugg();
			for(int i=0;i<12;i++){
				Casuta[] casuta=new Casuta[3];
				casuta[0]=new Casuta();
				casuta[0].set(cnp);
				cnp=cnp*cnp%100000000;
				casuta[1]=new Casuta();
				String s="ar"+serie;
				serie=serie*serie%1000000;
				casuta[1].set(s);
				casuta[2]=new Casuta();
				casuta[2].set(false);
				db.adaugaDateTabel("cont",casuta);
			}
		}
		// n
		new Display(buss,db);
		//display
	}

}
