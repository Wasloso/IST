import java.util.Arrays;

public class zad1 {
  public static void main(String[] args) {
    //bazowa posortowana lista - do niej beda dopisywane kolejne elementy
    int[] sortedArray = { 1, 2, 3, 5, 6, 7, 9, 10 };
    //wstawienie elementu "8"
    int[] result1 = insertSorted(sortedArray, 8);
    //wstawienie elementu "0"
    int[] result2 = insertSorted(result1, 0);
    //wstawienie elementu "4"
    int[] result3 = insertSorted(result2, 4);
    //wstawienie elementu "8" po raz drugi
    int[] result4 = insertSorted(result3, 8);
    int[] result5 = insertSorted(result4, 22);


    System.out.println(Arrays.toString(result1));
    System.out.println(Arrays.toString(result2));
    System.out.println(Arrays.toString(result3));
    System.out.println(Arrays.toString(result4));
    System.out.println(Arrays.toString(result5));

  }

  static int[] insertSorted(int[] list,int element){
    int len = list.length;
    //dlugosc nowej listy musi byc o 1 wieksza
    int[] output = new int[len+1];
    int i=0;
    //iterowanie przez kolejne elementy tablicy dopoki sa one mniejsze od elementu do wstawienie
    while(i<len && element>list[i]){
        output[i] = list[i];
        i++;
    }
    //dopisanie elementu na nastepne miejsce oraz skopiowanie pozostalych wartosci z tablicy
    output[i]=element;
    while(i<len){
        output[i+1] = list[i];
        i++;
    }
    return output;
  }
}