class Casuta{
    Var<?> slot;
    public <T> void set(T x) {
        slot=new Var<T>(x);
    }
    public String toString(){
        return slot.toString();
    }
    Casuta(){
    }
}
//singurul motiv pentru care exista acecasta clasa ii ca sa pot avea acces din display
//in database si ca sa pot sa adaug date