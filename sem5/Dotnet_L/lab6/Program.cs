using personTuple = (string name, string surname, int age);
using System;
using System.Drawing;
class Program
{
    static void Main()
    {
        Zad1();
        Zad2();
        Zad3();
        Zad4();
        zad5();
        Zad6();
    }

    static void Zad1(){
        Console.WriteLine("Zadanie 1");
        personTuple person = ("John", "Doe", 30);
        printPerson(person);
        void printPerson(personTuple person){
            Console.WriteLine($"Name: {person.name}, Surname: {person.surname}, Age: {person.age}");
            Console.WriteLine($"Name: {person.Item1}, Surname: {person.Item2}, Age: {person.Item3}");
            var (name, surname, age) = person;
            Console.WriteLine($"Name: {name}, Surname: {surname}, Age: {age}");
        }
    }

    static void Zad2(){
        Console.WriteLine("\nZadanie 2");
        int @class = 1;
        Console.WriteLine(@class);  
    }

    static void Zad3(){
        System.Console.WriteLine("\nZadanie 3");
        char[] array = ['f','a','d','x','z','.','n','e','t'];
        char[] arrayCopy = Array.Empty<char>();
        Console.WriteLine($"Empty array: {string.Join(", ",arrayCopy)}");
        Array.Resize(ref arrayCopy, array.Length);
        Array.Copy(array, arrayCopy, array.Length);
        Console.WriteLine($"Array: {string.Join(", ",array)}, copied array: {string.Join(", ",arrayCopy)}");
        Array.Sort(array);
        Console.WriteLine($"Sorted array: {string.Join(", ",array)}");
        Array.Reverse(array);
        Console.WriteLine($"Reversed array: {string.Join(", ",array)}");
        Console.WriteLine($"Array contains 'x': {Array.Exists(array, element => element == 'x')}");
        Console.WriteLine($"Array contains 'o': {Array.Exists(array, element => element == 'o')}");
    }

    static void Zad4(){
        Console.WriteLine("\nZadanie 4");
        var krotka = new { name = "John", surname = "Doe", age = 30 };
        printKrotka(krotka);
        void printKrotka(dynamic krotka){
            Console.WriteLine($"Name: {krotka.name}, Surname: {krotka.surname}, Age: {krotka.age}");
        }
    }
    static void zad5(){
        Console.WriteLine("\nZadanie 5");
        DrawCard("Ryszard", "Rys");
        DrawCard(firstLine:"Lab 6", secondLine:"Zadanie 5",borderChar:'-',borderThickness:3,minBorderWidth:30);
        DrawCard(".Net",borderChar:'#',borderThickness:1,minBorderWidth:10);

        void DrawCard(string firstLine, string secondLine="Default", char borderChar='X',int borderThickness = 2, int minBorderWidth = 20){
            if(firstLine.Length+borderThickness*2 > minBorderWidth){
                minBorderWidth = firstLine.Length+borderThickness*2;
            }
            if(secondLine.Length+borderThickness*2 > minBorderWidth){
                minBorderWidth = secondLine.Length+borderThickness*2;
            }
            for(int i=0;i<borderThickness*2+2;i++){
                for(int j=0;j<minBorderWidth;j++){
                    if(i==borderThickness  || i==borderThickness+1){
                        if(j<borderThickness || j>=minBorderWidth-borderThickness){
                            Console.Write(borderChar);
                        }
                        else{
                            if(i==borderThickness){
                                int pos = (minBorderWidth-firstLine.Length)/2;
                                if(j>=pos && j<pos+firstLine.Length){
                                    Console.Write(firstLine[j-pos]);
                                }
                                else{
                                    Console.Write(" ");
                                }
                            }
                            else{
                                int pos = (minBorderWidth-secondLine.Length)/2;
                                if(j>=pos && j<pos+secondLine.Length){
                                    Console.Write(secondLine[j-pos]);
                                }
                                else{
                                    Console.Write(" ");
                                }
                            }
                        }
                    }
                    else{
                        Console.Write(borderChar);
                    }
                }
                Console.WriteLine();
            }

        }
    }

    static void Zad6(){
        Console.WriteLine("\nZadanie 6");
        object[] array = [float.MaxValue,1,2,3,4,5,-6.123,1.6,'a','b',"test1","test2","test5",new Point(1,2)];
        var dict = CountMyTypes(array);
        foreach(var element in dict){
            Console.WriteLine($"{element.Key}: {element.Value}");
        }

        static Dictionary<string, int> CountMyTypes(params object[] array)
        {
            var dict = new Dictionary<string, int> { {"LongString", 0}, {"EvenInt", 0}, {"PositiveDouble", 0}, {"Other", 0} };
            foreach (var element in array)
            {
                switch (element)
                {
                    case string s when s.Length >= 5:
                        dict["LongString"] += 1;
                        break;
                    case int i when i % 2 == 0:
                        dict["EvenInt"] += 1;
                        break;
                    case double d when d > 0:
                        dict["PositiveDouble"] += 1;
                        break;
                    default:
                        dict["Other"] += 1;
                        break;
                }
            }
            return dict;
        }
    }

}