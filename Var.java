public class Var<T> {
	T data;
	public String toString() {
		String x="";
		x+=data;
		return x;
	}
	Var(){

	}
	Var(T x) {
		this.data=x;
	}
	public void change(T x) {
		this.data=x;
	}
	
}
