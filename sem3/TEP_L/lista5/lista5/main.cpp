// Patryk £uszczek 272707
// 
// 
// przykladwe uzycie smart pointer oraz move semantics

#include <iostream>;
#include "MyTree.h";
#include "MySmartPointer.h";

using namespace std;

int main() {

	// tworzymy inteligentny wskaznik na pamiec zaalokowana dynamicznie int o wartosci 10
	MySmartPointer<int> sp1(new int(10));
	// korzystamy z konstruktora kopiuj¹cego, i kopiujemy wartosci sp1 do sp2
	// w wyniku czego oba wskazniki beda wskazywaly na ta sama komorke pamieci z int 10
	// oraz na ten sam RefCounter, przy czym wartosc RefCounter siê zwiekszy o 1
	MySmartPointer<int> sp2(sp1);
	// teraz dynamicznie tworzymy sp3 (aby mozna bylo go manualnie usunac) i znowu kopiujemy sp1 (ale przy uzyciu sp1, bo wskazuja na to samo)
	MySmartPointer<int>* sp3 = new MySmartPointer<int>(sp2);
	// podwojenie wartosci przechowywanej przez sp1, powoduje rowniez podwojenie w sp2 i sp3 (bo wskazuja na to samo)
	*sp1 = *sp1 * 2;
	// gdy usuniemy sp3, to RefCounter w sp1 i sp2 sie zmniejszy o 1 (jest to ten sam RefCounter)
	delete sp3;
	// gdy wyjdziemy z main() to sp1 i sp2 zostana usuniete, a gdy ostatni smart pointer wskazujacy na nasz int 10 
	// zostanie usuniety, to ta pamiec rowniez zostanie zdealokowana
	// wszystkie te zaleznosci mozna ³atwo zaobserowaæ przy debugowaniu (pokazalem to Panu przy prezentacji listy)


	// uzycie move semantics - tworzymy dwa drzewa tree1 i tree2
	MyTree<int> tree1;
	MyTree<int> tree2;
	// a nastepnie korzystamy z konstruktora przenoszacego
	// fakt, wywolania konstruktora przenoszacego mozna rowniez latwo sprawdzic podczas debugowania
	MyTree<int> tree3(tree1 + tree2);
	// a tak mozna wywolac operator przenoszenia
	tree1 = tree2 + tree3;

	return 0;
}