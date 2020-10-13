import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;



public class Main {
    
    
    static class Matricula {

    //public int classificacao;
    public String nome;
    public String peroEscola;
    public int renda;
    public String bolsaFamilia;
    public String irmaoMat;
    public String maeFora;
    public int classificao;

    public Matricula(String nome, String peroEscola, int renda, String bolsaFamilia, String irmaoMat, String maeFora) {
        this.nome = nome;
        this.peroEscola = peroEscola;
        this.renda = renda;
        this.bolsaFamilia = bolsaFamilia;
        this.irmaoMat = irmaoMat;
        this.maeFora = maeFora;
    }
      
}
    
    static int compareMatriculas(Matricula m1, Matricula m2){
        if (m1.peroEscola.equals(m2.peroEscola) && (m1.renda == m2.renda) && (m1.bolsaFamilia.equals(m2.bolsaFamilia))
                && (m1.irmaoMat.equals(m2.irmaoMat)) && (m1.maeFora.equals(m2.maeFora))   ){
            return 0;
        }
        else{
            return 1;
        }
        
    }

   static Comparator<Matricula>  myComparator = (Matricula m1, Matricula m2) -> {
        
           
        int result = m2.peroEscola.compareTo(m1.peroEscola);
        if (result == 0){
            result = m1.renda - m2.renda;
        }
        if (result == 0) {
            result = m2.bolsaFamilia.compareTo(m1.bolsaFamilia);
        }
        if (result == 0) {
            result = m2.irmaoMat.compareTo(m1.irmaoMat);
        }
        if (result == 0) {
            result = m2.maeFora.compareTo(m1.maeFora);
        }
        if (result == 0){
            result = m1.nome.compareTo(m2.nome);
            
        }
        return result;
        
    };

    public static void main(String[] args) {
        List<Matricula> matriculas = new ArrayList();
        Scanner reader = new Scanner(System.in);
        

        while (reader.hasNext()) {
            String line = reader.nextLine();
            if ("-1".equals(line)){
                break;
            }
            String[] lido = line.split(";");

            matriculas.add(new Matricula(lido[0], lido[1], Integer.parseInt(lido[2]), lido[3], lido[4], lido[5]));

        }
        
        Collections.sort(matriculas, myComparator);
         
        matriculas.get(0).classificao = 1;
        System.out.println(matriculas.get(0).classificao + " " + matriculas.get(0).nome);
        for (int i = 1; i < matriculas.size(); i++){
            if ((compareMatriculas(matriculas.get(i), matriculas.get(i-1))) == 0){
               matriculas.get(i).classificao = matriculas.get(i-1).classificao;
            }
            else{
                matriculas.get(i).classificao = matriculas.get(i-1).classificao + 1;
            }
          
            
            System.out.println(matriculas.get(i).classificao + " " + matriculas.get(i).nome);
        }
        
        

    }

}
