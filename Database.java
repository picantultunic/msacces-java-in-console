import java.util.ArrayList;

/**
 * in acest fisier o sa trebuiasca sa fie si o clasa/functie ce o sa bage
 * automat pathul default ca poate il schimba dar pentru acum este bine
 */

public class Database {
	// lista de tabele
	bussGlobal buss;
	private boolean activa = false;
	String nume, path;
	ArrayList<Tabel> listatabele = new ArrayList<Tabel>();
	creareFisiere cf = new creareFisiere();// ii ca sa poti sa faci fisiere
	int nrtabele = 0;
	int contorTabele = 0;

	public boolean isActive() {
		return this.activa;
	}

	public int pozitieTabel(String n) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(n)) {
				return i;
			}
		return -1;
	}

	public void addTabel(String n) {
		// ce ii mai jos ii pentru a crea fisierul de tabel
		if (listatabele == null) {
			buss.eroare(1);
			return;
		}
		for (int i = 0; i < listatabele.size(); i++)// cauta sa vada daca exista deja
			if (listatabele.get(i).numeTabel.equals(n)) {
				buss.eroare(7);
				return;
			}
		listatabele.add(new Tabel(n, this.contorTabele));// aici trebuie transmis numele si id dar id o sa fie bazat pe
															// contor
		this.contorTabele++;
		this.nrtabele++;
	}

	public void removeTabel(String nume) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				listatabele.remove(i);
				this.nrtabele--;
				return;
			}
		buss.eroare(2);
	}

	public void removeTabel(int i) {
		try {
			listatabele.remove(i);
			this.nrtabele--;
		} catch (Exception e) {
			buss.eroare(2);
		}
	}

	public void conecteazaTabele(String nume1, String nume2) {
		int a = -1, b = -1;
		for (int i = 0; i < listatabele.size() && (a == -1 || b == -1); i++) {
			if (listatabele.get(i).numeTabel.equals(nume1)) {
				a = i;
			}
			if (listatabele.get(i).numeTabel.equals(nume2)) {
				b = i;
			}
		}
		if (a == -1 || b == -1 || a == b) {
			buss.eroare(2);
			return;
		}
		// true <=> se conecteaza
		listatabele.get(a).conexitateaTabelelor(listatabele.get(b), true);
		listatabele.get(b).conexitateaTabelelor(listatabele.get(a), true);
	}

	public void setareConexiuneTabel(String nume1, String nume2) {
		// int a=-1,b=-1;
		// for(int i=0;i<nrtabele&&(a==-1||b==-1);i++){
		// f(listatabele.get(i).numeTabel.equals(nume1)){
		// a=i;
		// }
		// if(listatabele.get(i).numeTabel.equals(nume2)){
		// b=i;
		// }
		// }
		// if(a==-1||b==-1){
		// buss.eroare(2);
		// return;
		// }
		// //true <=> se conecteaza
		// listatabele.get(a).conexitateaTabelelor(listatabele.get(b), true);
		// listatabele.get(b).conexitateaTabelelor(listatabele.get(a), true);
	}

	public void deconecteazaTabele() {
		// i simplu ...
	}

	public void adaugaDateTabel(String nume, Casuta[] v) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				listatabele.get(i).addRand(v);
			}
	}

	public void adaugaDateTabel(int i, Casuta[] v) {
		if (i >= listatabele.size()||i<0) {
			buss.eroare(2);
			return;
		}
		listatabele.get(i).addRand(v);
	}

	public void stergeDateTabel() {
		/**
		 * cel mai probabil o sa fie pe rand dar o sa trebuiasca sa fie doua
		 * prima sa fie hard delete si cea de adoua ii sa fie de curatare
		 */
	}

	public void modificaDateTabel(String nume, int k, Casuta[] c) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				listatabele.get(i).modificaRand(k, c);
			}
	}

	public void modificaDateTabel(int i, int k, Casuta[] c) {
		if (i >= listatabele.size()||i<0) {
			buss.eroare(2);
			return;
		}
		listatabele.get(i).modificaRand(k, c);

	}

	public String returneazaDateTabelString(String nume, int k) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				return listatabele.get(i).returnRandString(k);
			}
		/**
		 * in parametru o sa trebuiasca sa fie specificat modul de transmitere a
		 * informatiei
		 * si poate o sa fie mai multe functii in functie de formatul cerut de returnare
		 */

		return "null tabel";
	}

	public Casuta[] returneazaDateTabel(String nume, int k) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				return listatabele.get(i).returnRand(k);
			}
		/**
		 * in parametru o sa trebuiasca sa fie specificat modul de transmitere a
		 * informatiei
		 * si poate o sa fie mai multe functii in functie de formatul cerut de returnare
		 */
		return null;
	}
	public Casuta[] returneazaDateTabel(int i, int k) {
		if (i >= listatabele.size()||i<0) {
			buss.eroare(2);
			return null;
		}
		return listatabele.get(i).returnRand(k);
	}
	public void seteazaStructuraTabel(String nume, String[] struct, String[] numeColoana) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				if (listatabele.get(i).structura == null) {
					listatabele.get(i).initializareStructura(struct, numeColoana);
				} else
					// adauga datatype de la structura veche
					listatabele.get(i).nouaStructura(struct, numeColoana);
			}
	}

	public void seteazaStructuraTabel(int index, String[] struct, String[] numeColoana) {
		if (index >= listatabele.size() && index >= 0) {
			buss.eroare(2);
			return;
		}
		if (listatabele.get(index).structura == null) {
			listatabele.get(index).initializareStructura(struct, numeColoana);
		} else{// adauga datatype de la structura veche
			for(int i=0;i<numeColoana.length;i++)
				struct[i]=listatabele.get(index).structura.coloane.get(i).toString();
			listatabele.get(index).nouaStructura(struct, numeColoana);}
	}

	public void seteazaStructuraTabelFortat(String nume, String[] struct, String[] numeColoana) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				if (listatabele.get(i).structura == null) {
					listatabele.get(i).initializareStructura(struct, numeColoana);
				} else
					// poate o sa ai o eroare cand apelezi comanda si atunci o sa fie al drecu
					listatabele.get(i).nouaStructura(struct, numeColoana, true);
				break;
			}
	}

	public void redenumesteStructuraTabel(String nume, String[] numeColoana) {
		for (int i = 0; i < listatabele.size(); i++)
			if (listatabele.get(i).numeTabel.equals(nume)) {
				if (listatabele.get(i).structura != null) {
					listatabele.get(i).redenumireStructura(numeColoana);
				} else
					buss.eroare(10);
				break;
			}
	}

	public void redenumesteStructuraTabel(int i, String[] numeColoana) {
		if (i >= listatabele.size() && i >= 0) {
			buss.eroare(2);
			return;
		}
		if (listatabele.get(i).structura != null) {
			listatabele.get(i).redenumireStructura(numeColoana);
		} else
			buss.eroare(10);

	}

	public void salveazaDatabase() {
		cf.salveazaDatabase(buss, this);
	}

	Database(bussGlobal buss, String path, String nume, boolean secreaza) {
		this.buss = buss;
		if (secreaza) {
			/**
			 * executia: 
			 * se verifica pathul si se corecteaza daca e cazul
			 * 
			 * se verifica daca se poate sa 
			 */

			// partea asta ar trebuii sa fie pusa la save
			// String data=new String();
			String[] data = new String[1];
			data[0] = path;
			// fa ce ai facut pentru path si pentru nume ca poate ai duplicat

			// o sa trebuiasca sa scoti din comentariu pentru ca asta creaza fisiere
			///cf.fisierDatabase(buss, data, nume);

			// este pentru crearea fisiereor si trebuie inlocuit cu unu care ferifica pathul
			if (cf.existaDatabase(path, nume)){
				buss.eroare(8);
				return;}
			this.path = cf.returneazaPathDatabase(path);
			this.nume = nume;

			if (!buss.hasError() && this.path != null) {
				this.activa = true;
			} // else {eroare(nr eroare)}
		} else {
			if (!cf.existaDatabase(path, nume)){
				buss.eroare(6);
				return;}
			this.path=cf.returneazaPathDatabase(path);
			this.nume=nume;
			cf.incarcaDatabase(buss,this);
		}
	}

	public void debugg() {
		// buss.eroare(5);
		System.out.println("nume:" + this.nume);
		System.out.println("path:" + this.path);
		System.out.println("lista tabele:");
		for (int i = 0; i < listatabele.size(); i++) {
			System.out.println(" numetabel:" + listatabele.get(i).numeTabel);
			System.out.println("  structura:" + listatabele.get(i).structura);
		}
	}
}
