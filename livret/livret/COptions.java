package livret;

public class COptions {
    public int typeImprimante;
    public boolean rectoVerso;

    public COptions() {}
    
    public COptions(int typeImprimante, boolean rectoVerso) {
        set(typeImprimante, rectoVerso);
    }   
    
    public void set(int typeImprimante, boolean rectoVerso) {
        this.typeImprimante = typeImprimante;
        this.rectoVerso = rectoVerso;
    }
    
    public String toString() {
        return "[type : " + typeImprimante + " , r-v : " + rectoVerso + "]";
    }
}

