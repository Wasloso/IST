public class Test2 {
    Integer zawartość = 0;
    static void argNiemodyfikowalny(final Test2 zmienna) {
        zmienna.zawartość = 1;
    }

    static void argModyfikowalny(Test2 zmienna) {
        zmienna.zawartość +=1;
        zmienna = null;
    }

    public static void main(String[] args) {
        Test2 modyfikowalna = new Test2();
        final Test2 niemodyfikowalna = new Test2();

        argModyfikowalny(modyfikowalna);
        System.out.println(modyfikowalna.zawartość);

         argModyfikowalny(modyfikowalna);
        System.out.println(modyfikowalna.zawartość);


        // tutaj wstaw instrukcje
    }
}