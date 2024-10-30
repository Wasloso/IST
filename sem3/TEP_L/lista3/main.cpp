// klasa main z przyk�adowym zastosowaniem drzewa
// tworzone s� dwa drzewa i program prosi uzytkownika o wprowadzenie ich formu�
// nast�pne tworzone jest trzecie drzewo powsta�e z po��czenia (operatorem "+") poprzednio wspomnianych drzew

#include "MyNode.h";
#include <map>;
#include <iostream>;
#include "MyTree.h";

using namespace std;

int main() {
	MyTree* tree1 = new MyTree();
	tree1->startInterface();
	MyTree tree2;
	tree2.startInterface();
	MyTree tree3 = *tree1 + tree2;
	tree3.startInterface();
	tree1->print();
	tree2.print();
	tree3.print();

	delete tree1;


	return 0;
}