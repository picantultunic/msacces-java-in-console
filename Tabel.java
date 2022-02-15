import java.util.ArrayList;

public class Tabel {
	//private boolean idlock=false;
	String numeTabel;
	int id,idelemente=0,subtabeleCounter=0;
	boolean locktab=false,activ=false;

	bussGlobal buss=new bussGlobal();
	Structura structura;
	ArrayList<Subtabel> subtabel=new ArrayList<>();
	ArrayList<Relatie> relatiiTab=new ArrayList<>();
	//structura tabelului
	//matricea de valori
	protected int marimeSubtabel=10,lenghtStruct=1;//basic
	class Relatie{
		Tabel tab;// cu ce tabel este conectat
		// aici ar trebuii sa fie adaugat ceva cod
		// ce ii spune tipul de relatie este facut , 
		// chestiile alea cu 1 to mani , 1 to 1 , many to many si prostiilea
		Relatie(Tabel t){
			this.tab=t;
			
		}
	}
	
	
	class Rand{
		int length;
		int id;
		//aici ar trebuii sa fie arraylist
		Casuta[] casuta;
		public void returneazaRand(){}
		Rand(){
			this.id=idelemente;
			idelemente++;
		}
		Rand(Casuta[] c){
			// if(c.length<lenghtStruct)
			// 	this.casuta=c;
			// else{
			// 	this.casuta=new Casuta[lenghtStruct];
			// 	for(int i=0;i<lenghtStruct;i++){
			// 		this.casuta[i]=new Casuta();
			// 		this.casuta[i].set(c[i]);
			// 	}
			// }
			this.casuta=c;
			this.id=idelemente;
			idelemente++;	
		}
		public void initializare(int n){
			this.casuta=new Casuta[n];
		}
		public Casuta[] returnCasuta(){
			return this.casuta;
		}
		@Override
		public String toString() {
			String data="";
			for(int i=0;i<casuta.length-1;i++)
				data+=casuta[i].toString()+",";
			data+=casuta[casuta.length-1].toString();
			return data;
		}
	
	}

	
	class Subtabel{	
		int id;
		ArrayList<Rand> rand=new ArrayList<Rand>(lenghtStruct);
		Subtabel(){
			subtabeleCounter++;
		}
	}
	public void conexitateaTabelelor(Tabel tab,boolean seConecteaza){
		if(seConecteaza){
			relatiiTab.add(new Relatie(tab));
		}else{
			try {
			relatiiTab.remove(tab);
			} catch (Exception e) {}
		}
	}
	public void initializareStructura(String[]lista,String[]numeColoana){
		if(this.structura!=null){
			buss.eroare(14);
			return;
		}
		if(numeColoana.length!=lista.length){
			buss.eroare(12);
			return;
		}
		this.structura=new Structura(numeColoana.length);

		for(int i=0;i<numeColoana.length;i++){
			this.structura.numeColoana.add(new String(numeColoana[i]));
			this.structura.coloane.add(new Casuta());
			this.structura.coloane.get(i).set(lista[i]);
		}
	}
	public void nouaStructura(String[]lista,String[]numeColoana){//ii mai mult pentru default
		nouaStructura(lista, numeColoana,false);
	}
	public void nouaStructura(String[]lista,String[]numeColoana,boolean supraScriere){
		//aici s-ar putea sa existe erori in restructurare
		
		if(numeColoana.length!=lista.length){
			buss.eroare(12);
			return;
		}
		if(structura==null)
			structura=new Structura(lista.length);
		else{
			boolean[] verificare=new boolean[lista.length];
			for(int i=0;i<lista.length&&i<structura.coloane.size();i++)
				if(!lista[i].equals(structura.coloane.get(i).toString()))
					verificare[i]=true;

			if(supraScriere==false)
				for(int i=0;i<lista.length&&i<structura.coloane.size();i++)
				if(verificare[i]){
					buss.eroare(15);
					return;
			}
		}
		
		for(int i=0;i<lista.length;i++){
			structura.numeColoana.add(new String(numeColoana[i]));
			structura.coloane.add(new Casuta());
			structura.coloane.get(i).set(lista[i]);
		}
		//o sa trebuiasca sa adaugi ceva functie
		//ce poate sa transfere datele din old struct in new struct
		//
	}
	public void EroareInitializareStructura(String[]lista,String[]numeColoana){
		// daca nu ai reusit sa initializezi cum trebuie
		if(structura==null){
			buss.eroare(10);
			return;
		}
		if(!structura.eroare||buss.returnEroare()!=11)
			return;
		
		initializareStructura(lista,numeColoana);
	}
	public void redenumireStructura(String[]numeColoana){
		if(structura==null){
			buss.eroare(13);
			return;
		}
		if(numeColoana.length>structura.coloane.size()){
			buss.eroare(12);
			return;
		}
		for(int i=0;i<structura.coloane.size();i++){
			structura.numeColoana.set(i, numeColoana[i]);
		}
	}
	public String[]returnareStructura(){
		String[] data=new String[structura.numeColoana.size()];
		for(int i=0;i<data.length;i++)
			data[i]=new String(structura.numeColoana.get(i));
		return data;
	}
	public String[]returnareStructuraDatatype(){
		String[] data=new String[structura.numeColoana.size()];
		for(int i=0;i<data.length;i++)
			data[i]=new String(structura.coloane.get(i).toString());
		return data;
	}
	public void inchuieTabel(){
		//asta ii daca vrei sa faci un tabel de tip lista si sa 
		//poti avea casute de unde sa selectezi
		//daca dai lock nu mai poti sa modifici structura
		if(this.locktab)
			this.locktab=false;
		else
			this.locktab=true;
	}
	public void addRand(Casuta[]casute){
		// se poate sa fie un bug;
		//trebuie sa verifice daca sunt cum trebuie datatype
		if(structura==null){
			buss.eroare(13);
			return;
		}
		if(subtabel.size()==0){
			buss.eroare(16);
			return;
		}
		
		if(casute.length>structura.lenght){
			Casuta[] aux=new Casuta[structura.lenght];
			for(int i=0;i<structura.lenght;i++)
				aux[i]=casute[i];
			casute=aux;
		}
		verificare.transformareDate(structura, casute);
		if(bussGlobal.hasError())
			return;
		
		if(subtabel.get(subtabel.size()-1).rand.size()>=marimeSubtabel)
			subtabel.add(new Subtabel());
		subtabel.get(subtabel.size()-1).rand.add(new Rand(casute));
	}
	public void addRandfortat(Casuta[]casute){
		// se poate sa fie un bug;
		//trebuie sa verifice daca sunt cum trebuie datatype
		if(structura==null){
			buss.eroare(13);
			return;
		}
		if(subtabel.size()==0){
			buss.eroare(16);
			return;
		}

		if(casute.length>structura.lenght){
			Casuta[] aux=new Casuta[structura.lenght];
			for(int i=0;i<structura.lenght;i++)
				aux[i]=casute[i];
			casute=aux;
		}
		if(subtabel.get(subtabel.size()-1).rand.size()>=marimeSubtabel)
			subtabel.add(new Subtabel());
		subtabel.get(subtabel.size()-1).rand.add(new Rand(casute));
	}
	public void removeRand(int i){
		if(structura==null){
			buss.eroare(13);
			return;
		}
		try {
			Rand r;
			r=subtabel.get(i/marimeSubtabel).rand.get(i%marimeSubtabel);
		} catch (Exception e) {
			buss.eroare(17);	
			return;
		}
		subtabel.get(i/marimeSubtabel).rand.set(i%marimeSubtabel,null);

	}
	public String returnRandString(int i){
		if(structura==null){
			buss.eroare(13);
			return "null struct";
		}
		String data="";
		Rand r;
		try {
			r=subtabel.get(i/marimeSubtabel).rand.get(i%marimeSubtabel);
			data+="("+r.toString()+")";
		} catch (Exception e) {
			data="null rand";
		}
		return data;
	}
	public Casuta[] returnRand(int i){
		if(structura==null){
			buss.eroare(13);
			return null;
		}
		Rand r;
		try {
			r=subtabel.get(i/marimeSubtabel).rand.get(i%marimeSubtabel);
			} catch (Exception e) {
			//buss.eroare(17);	nu ar trebuii , ar trebui sa lasi sa decida utilizatorul 
			return null;
		}
		return r.returnCasuta();
	}
	public void modificaRand(int i,Casuta[]c){
		if(structura==null){
			buss.eroare(13);
			return;
		}
		Rand r;
		try {
			if(subtabel.get(i/marimeSubtabel).rand.get(i%marimeSubtabel)==null){
				subtabel.get(i/marimeSubtabel).rand.set(i%marimeSubtabel,new Rand());
			}
			r=subtabel.get(i/marimeSubtabel).rand.get(i%marimeSubtabel);
			} catch (Exception e) {
			buss.eroare(17);
			return;
		}
		int n;
		if(c.length>structura.lenght)
			n=structura.lenght;
		else 	
			n=c.length;
			
		for(int j=0;j<n;j++)
			r.casuta[j]=c[j];
	}
	Tabel(){
		
	}
	Tabel(String nume,int id){
		this.numeTabel=nume;
		this.id=id;
		subtabel.add(new Tabel.Subtabel());
		activ=true;
		//mai trebuie sa fie adaugat si randuri si altele dar in fine
	}	
	public void setup(String nume,int id)	{
		this.numeTabel=nume;
		this.id=id;
		subtabel=new ArrayList<>();
		subtabel.add(new Tabel.Subtabel());
		activ=true;
	}
}
