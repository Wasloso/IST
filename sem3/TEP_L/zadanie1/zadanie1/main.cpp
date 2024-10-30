#include <iostream>
#include "MyTable.h"

using namespace std;

//alokacja tablicy o rozmiarze "size" i wypelnienie jej wartoscia 34
void allocTableFill34(int size) {
	if (size <= 0) {
		cout << "Nieporwawna wartosc size" << endl;
		return;
	}
	//alokacja
	int *iTab = new int[size];
	for (int i = 0; i < size; i++) {
		//wypelnienie
		iTab[i] = 34;
	}
	//wyswietlenie
	for (int i = 0; i < size; i++) {
		cout << "Tab[" << i << "] = " << iTab[i] << endl;
	}
	//usuniecie tej tablicy
	delete[] iTab;
	return;
}

//alokacja tablicy 2wymiarowej
bool allocTable(int **&tablePointer, int sizeX, int sizeY) {
	if (sizeX <0 || sizeY <0) return false;
	//alokacja tablicy tablic
	tablePointer = new int* [sizeX];
	for (int i = 0; i < sizeY; i++) {
		//alokacja tablicy w kazdej komorce tablicy tablic
		tablePointer[i] = new int[sizeY];
	}
	return true;
}

//dealokacja tablicy 2wymiarowej
bool deallocTable(int **&tablePointer, int sizeX, int sizeY) {
	if (sizeX <0 || sizeY <0) return false;
	//usuniecie kazdej komorki zawierajaca tablice
	for (int i = 0; i < sizeX; i++) {
		delete[] tablePointer[i];
	}
	//usuniecie wskaznika poczatkowego
	delete[] tablePointer;
	tablePointer = NULL;
	return true;

}

//modyfikacja tablicy poprzez wskaznik
void modTable(MyTable* MyTablePointer, int newSize) {
	MyTablePointer->setNewSize(newSize);
}
//modyfikacja kopi tablicy
void modTable(MyTable MyTable, int newSize) {
	MyTable.setNewSize(newSize);
}


int main() {
	//zadanie 1
	allocTableFill34(10);

	//zadanie 2 / 3
	int sizeX = 5;
	int sizeY = 3;
	int** tablePointer = NULL;


	//spr zadania 2 i 3
	if (allocTable(tablePointer, sizeX, sizeX)) {
		cout << "Tablica zaalokowana pomyslnie" << endl;
		if (deallocTable(tablePointer, sizeX, sizeY))
		{
			cout << "Tablica usunieta pomyslnie" << endl;
		}
		else cout << "Blad w usuwaniu tablicy" << endl;
	}
	else cout << "Blad alokacji tablicy" << endl;
	

	//zadanie 4
	MyTable* tab0 = new MyTable("Tab0",5);	
	MyTable* tab1 = tab0->clone();
	MyTable* tab2 = new MyTable(*tab0);
	MyTable tab3;

	tab0->setName("tab0_mod");
	tab1->setNewSize(0);
	tab1->setName("tab1");

	tab0->printState();
	tab1->printState();
	tab2->printState();

	modTable(tab0,10);
	modTable(*tab1,10);

	tab0->printState();
	tab1->printState();

	delete tab2;
	delete tab0;

	return 0;
}