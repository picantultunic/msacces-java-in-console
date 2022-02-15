
import java.util.Scanner;

public class Display {
	Database db;
	int tabelSelectat = 0;
	Scanner input = new Scanner(System.in);
	int afisareDecizie = 1;
	int scroolPozition = 0, scroolCasuta = 0;
	String comanda = "";
	bussGlobal buss;

	private void executaComanda() {
		String aux = "";
		comanda = input.nextLine();
		int latime = 120;

		if (!(comanda.length() > 0))
			return;

		// verifica daca are parametrii comanda
		int parantezaDreapa = comanda.indexOf("("), parantezaStanga = comanda.indexOf(")");

		if (parantezaDreapa != -1 && parantezaStanga != -1) {
			System.out.println("am intrat in paranteze");
			// aici o sa ruleze comenzile ce au paranteze
			aux = comanda.substring(0, parantezaDreapa);
			String parametri = comanda.substring(parantezaDreapa + 1, parantezaStanga);

			if (aux.equals("adaugaTabel")) {
				db.addTabel(parametri);
			} else if (aux.equals("selectTab")) {
				// aux = comanda.substring(parantezaDreapa + 1, parantezaStanga);
				try {
					tabelSelectat = Integer.parseInt(parametri);
				} catch (Exception e) {
					int x = db.pozitieTabel(parametri);
					if (x == -1) {
						buss.eroare(19);
						return;
					} else
						tabelSelectat = x;
				}

			} else if (aux.equals("stergeTabel")) {
				String decizie = "";
				System.out.println("chiar doresti sa il stergi ? (y - da)");
				decizie = input.nextLine();
				if (decizie.equals("Y") || decizie.equals("y")) {
					int buffer;
					try {
						buffer = Integer.parseInt(parametri);
						db.removeTabel(buffer);
					} catch (Exception e) {
						db.removeTabel(parametri);
						return;
					}
				}

			} else if (aux.equals("scroll")) {
				int buffer;
				try {
					buffer = Integer.parseInt(parametri);
				} catch (Exception e) {
					buss.eroare(19);
					return;
				}
				//pentru negativ
				if (buffer + this.scroolPozition < 0)
					this.scroolPozition = 0;
				else
					this.scroolPozition += buffer;

			} else if (aux.equals("scrollCasute")) {
				int buffer;
				try {
					buffer = Integer.parseInt(parametri);
				} catch (Exception e) {
					buss.eroare(19);
					return;
				}
				//la ultima
				if (!(buffer + this.scroolCasuta < db.listatabele.get(tabelSelectat).structura.lenght))
					this.scroolCasuta = db.listatabele.get(tabelSelectat).structura.lenght - 1;
				else if (buffer + this.scroolCasuta < 0)//pentru negativ
					this.scroolCasuta = 0;
				else
					this.scroolCasuta += buffer;
			} else if (aux.equals("modificaRand")) {
				// aux = comanda.substring(parantezaDreapa + 1, parantezaStanga);
				if (db.listatabele.get(tabelSelectat).structura == null)
					return;

				int buffer;
				try {
					buffer = Integer.parseInt(parametri);
				} catch (Exception e) {
					buss.eroare(19);
					return;
				}
				Casuta[] data = new Casuta[db.listatabele.get(tabelSelectat).structura.lenght];
				String[] numeColoana = db.listatabele.get(tabelSelectat).returnareStructura();
				for (int i = 0; i < data.length; i++) {
					String tastatura = "";
					System.out.print(numeColoana[i] + " : ");
					tastatura = input.nextLine();
					data[i] = new Casuta();
					data[i].set(tastatura);
				}
				db.modificaDateTabel(tabelSelectat, buffer, data);
			} else if (aux.equals("zoomCasuta")) {
				int x, y;
				try {
					x = Integer.parseInt(parametri.substring(0, parametri.indexOf(",")));
					y = Integer.parseInt(parametri.substring(parametri.indexOf(",")+1));
				} catch (Exception e) {
					buss.eroare(19);
					return;
				}
				zoomFereastra(x, y);
			}else if(aux.equals("randAprobat")){//proba , numai debug
				int x;
				try {
					x = Integer.parseInt(parametri);
				} catch (Exception e) {
					buss.eroare(19);
					return;
				}
				System.out.println(
					verificare.aprobRand(
						db.listatabele.get(tabelSelectat).structura, 
						db.returneazaDateTabel(tabelSelectat, x)));

				input.nextLine();
			}
		} else if (parantezaDreapa == -1) {
			// nu au nevoie de comanda
			aux = comanda;
			if (aux.equals("iesire")) {
				throw new IllegalArgumentException("ai iesit din program");

			} else if (aux.equals("salvare")) {
				db.salveazaDatabase();

			} else if (aux.equals("adaugaRand")) {
				// o sa trebuiasca o fereastra
				for (int i = 0; i < latime; i++)
					System.out.print("-");
				System.out.println("");
				Casuta[] data = new Casuta[db.listatabele.get(tabelSelectat).structura.lenght];
				String tastatura = "";
				for (int i = 0; i < data.length; i++) {
					tastatura = input.nextLine();
					data[i] = new Casuta();
					//doar le memoreaza , nu le si prelucreaza
					data[i].set(tastatura);
				}

				db.adaugaDateTabel(tabelSelectat, data);
			} else if (aux.equals("modificaStructNume")) {
				String[] numeColoana = db.listatabele.get(tabelSelectat).returnareStructura();
				String[] data = new String[numeColoana.length];
				for (int i = 0; i < numeColoana.length; i++) {
					System.out.print(numeColoana[i] + "=>");
					String tastatura = input.nextLine();
					data[i] = new String(tastatura);
				}
				db.redenumesteStructuraTabel(tabelSelectat, data);
			} else if (aux.equals("seteazaStructura")) {
				String[] numeColoana;
				try {
					numeColoana = db.listatabele.get(tabelSelectat).returnareStructura();
				} catch (Exception e) {
					System.out.print("cate coloane doresti? : ");
					String tastatura = input.nextLine();
					int buffer;
					try {
						buffer = Integer.parseInt(tastatura);
						numeColoana = new String[buffer];
					} catch (Exception b) {
						buss.eroare(19);
						return;
					}
				}
				String[] data = new String[numeColoana.length];
				String[] newdatatype = new String[numeColoana.length];// poate ar trebuii sa fie datatype.lenght

				for (int i = 0; i < numeColoana.length; i++) {
					System.out.print("coloana " + i + " =>");
					String tastatura = input.nextLine();
					data[i] = new String(tastatura);
					System.out.print("datatype=>");
					tastatura = input.nextLine();
					newdatatype[i] = new String(tastatura);
				}
				db.seteazaStructuraTabel(tabelSelectat, newdatatype, data);
			}

		} else {
			buss.eroare(19);
		}
		// zona unde executa comenzile

		/*
		 * // switch(aux){
		 * // case "salvare":
		 * // db.salveazaDatabase();
		 * // break;
		 * // case "redenumire":break;
		 * // case "iesire":
		 * // //salvare
		 * // throw new IllegalArgumentException("ai iesit din program");
		 * // case "modificaRand":
		 * // //verifica daca este nume sau numar
		 * // break;
		 * // case "modificaTabel":break;//nu cred ca ar trebuii sa existe
		 * // case "stergeTabel":break;
		 * // case "stergeRand":break;
		 * // case "zoomRand":break;
		 * // case "zoomCasuta":break;
		 * // case "scrolTabel":break;
		 * // case "scrolCasute":break;
		 * // case "adaugaRand":break;
		 * // case "adaugaTabel":
		 * // //db.addTabel("test");
		 * // break;
		 * // case "modificaStructura":break;
		 * // case "modificaStructuraFortat":break;
		 * // default:
		 * // //eroare , nu e buna comanda
		 * // }
		 */

	}

	private int lungimeData(String tipvar) {
		if (tipvar.equals(new String("boolean")) || tipvar.equals(new String("short")))
			return 5;
		if (tipvar.equals(new String("int")) || tipvar.equals(new String("double")))
			return 12;
		if (tipvar.equals(new String("String")))
			return 20;
		if (tipvar.equals(new String("char")))
			return 1;
		return 10;
	}

	// mai trebuie adaugat ceva ce da zoom la casute si trebuie sa faci comenzile sa
	// fie executate u
	private void zoomFereastra(int x, int y) {
		// x rand
		// y celula
		// cas = db.listatabele.get(tabelSelectat).returnRand(i + scroolPozition);
		Casuta box;
		try {
			Casuta[] a;
			a = db.listatabele.get(tabelSelectat).returnRand(x);
			if (y >= a.length)
				y = a.length - 1;
			box = a[y];
		} catch (Exception e) {
			buss.eroare(17);
			return;
		}
		String str = box.toString(), spatiuStanga = "                  ";
		System.out.print(spatiuStanga);
		for (int i = 0; i < 122; i++)
		System.out.print("-");
		System.out.println("");
		
		for (int i = 0; i < 30; i++) {
			String aux = "";
			try {
				aux = str.substring(i*120, i*120+120);
			} catch (Exception e) {
			}
			System.out.println(spatiuStanga + "|" + String.format("%-120s", aux) + "|");
		}
		System.out.print(spatiuStanga);
		for (int i = 0; i < 122; i++)
		System.out.print("-");
		System.out.println("");
		input.nextLine();	
	}

	private void fereastraEroare() {
		int latime = 122;
		String spatiuStanga = "                  ";
		System.out.println(bussGlobal.multipleErrorCode.size() + " " + bussGlobal.hasError());
		System.out.print("\n\n\n" + spatiuStanga);
		for (int i = 0; i < latime; i++)
			System.out.print("-");
		System.out.println("");

		System.out.println(spatiuStanga + "|" + String.format("%-120s", "   urmatoarele erori au aparut :") + "|");

		for (int i = 0; i < 29; i++) {
			String aux = "";
			try {
				// preia numele erori
				aux = bussGlobal.returnEroareString(bussGlobal.multipleErrorCode.get(i));
				// adauga si numarul erorii
				aux = "  eroare(" + bussGlobal.multipleErrorCode.get(i) + ") " + aux;
			} catch (Exception e) {
			}
			System.out.println(spatiuStanga + "|" + String.format("%-120s", aux) + "|");
		}

		System.out.print(spatiuStanga);
		for (int i = 0; i < latime; i++)
			System.out.print("-");
		System.out.println("");

		for (int i = 0; i < 13; i++)
			System.out.println("");

		input.nextLine();// asteapta pentru enter
		bussGlobal.reseteazaEroarea();
	}

	private void aplicatie() {
		// for(int i=0;i<ecran.length;i++)
		// System.out.println(ecran[i]);

		// tot asta o sa fie void fereastraDB()
		int latime = 160;
		String meniu1 = "  meniubar(numarul comenzii)  nr coloane tab:";
		try {
			meniu1 += db.listatabele.get(tabelSelectat).structura.coloane.size();
		} catch (Exception e) {
		}
		String meniu2 = " 1)Salvare 2)redenumire 3)iesire ";

		// partea de sus
		for (int i = 0; i < latime; i++)
			System.out.print("-");
		System.out.println("");// ii numai pentru a face o linie mare
		System.out.println("|" + String.format("%-158s", meniu1) + "|");// meniu de sus
		System.out.println("|" + String.format("%-158s", meniu2) + "|");
		for (int i = 0; i < latime; i++)
			System.out.print("-");
		System.out.println("");
		/////////

		{// partea asta afiseaza numele coloanelor
			String rand = "", tabel = "", coloane = "";
			// coloana cu tabel si numarul lui
			try {
				tabel = 0 + ")" + db.listatabele.get(0).numeTabel;
				if (tabel.length() > 32)
					tabel = tabel.substring(0, 29) + "...";
			} catch (Exception e) {
			}
			rand += "|" + String.format("%-" + 32 + "s", tabel) + "|   |";

			try {
				String[] data = db.listatabele.get(tabelSelectat).returnareStructura();
				for (int i = 0 + scroolCasuta; i < data.length; i++) {
					int l = lungimeData(db.listatabele.get(tabelSelectat).structura.coloane.get(i).slot.toString());
					if (data[i].length() > l)
						coloane += data[i].substring(0, l - 3) + "..." + "|";
					else
						coloane += String.format("%-" + l + "s", data[i]) + "|";
				}
			} catch (Exception e) {
			}
			if (coloane.length() > 121) {// daca este mai mare atunci il face mai mic ca sa incapa
				String aux = coloane.substring(0, 117);
				aux += "...";
				coloane = aux;
			}
			rand += String.format("%-121s", coloane) + "|";
			System.out.println(rand);
		}

		for (int i = 0; i < 36; i++) {
			String rand = "", tabel = "", coloane = "";
			Casuta[] cas;
			// coloana cu tabel si numarul lui
			try {
				tabel = (i + 1) + ")" + db.listatabele.get(i + 1).numeTabel;
				if (tabel.length() > 32)
					tabel = tabel.substring(0, 29) + "...";

			} catch (Exception e) {
			}
			rand += "|" + String.format("%-" + 32 + "s", tabel) + "|";
			try {
				//cas oreia randul din tabel
				cas = db.listatabele.get(tabelSelectat).returnRand(i + scroolPozition);
				
				coloane += String.format("%" + 3 + "s", i + scroolPozition) + ">";// nr rand
				for (int index = 0 + scroolCasuta; index < cas.length; index++) {
					int l = lungimeData(db.listatabele.get(tabelSelectat).structura.coloane.get(index).slot.toString());
					if (cas[index].toString().length() > l)
						coloane += cas[index].toString().substring(0, l - 3) + "..." + "|";
					else
						coloane += String.format("%-" + l + "s", cas[index].toString()) + "|";
				}
			} catch (Exception e) {
			}

			if (coloane.length() > 125) {// daca este mai mare atunci il face mai mic ca sa incapa
				String aux = coloane.substring(0, 121);
				aux += "...";
				coloane = aux;
			}
			rand += String.format("%-125s", coloane) + "|";
			System.out.println(rand);
		}
		for (int i = 0; i < latime; i++)
			System.out.print("-");
		System.out.println("");
		// debug

	}

	private void render() {
		while (true) {
			if (!bussGlobal.hasError()) {
				switch (afisareDecizie) {
					case 1:
						//pentru alte ferestre viitoare
						afisareDecizie = 0;
						break;
					default:
						aplicatie();
				}
				executaComanda(); // asta este cea ce executa
			} else
				fereastraEroare();

			// curata ecranul
			System.out.print("\033[H\033[2J");
			System.out.flush();
		}
	}

	public void Update() {
		render();
		// Update();
	}

	Display(bussGlobal buss, Database db) {
		this.db = db;
		this.buss = buss;
		Update();
	}
}
