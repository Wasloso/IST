//Patryk £uszczek
//Lista 1

#include "MyTable.h"
#include <iostream>

using namespace std;

//Konstruktor
MyTable::MyTable() {
	init(defaultName, defaultLength);
	cout << "bezp: " << tableName << endl;
}

//Konstruktor z parametrami
MyTable::MyTable(string name, int length) {
	//blad, nadanie wartosci domyslnej
	if (length < 0) {
		length = defaultLength;
	}
	//funkcja inicjujaca obiekt
	init(name, length);
	cout << "parametr: " << tableName << endl;
}

//Konstruktor kopiujacy
MyTable::MyTable(MyTable& otherTable) {
	init(otherTable.tableName + copyAddnotation, otherTable.tableLength);
	for (int i = 0; i < otherTable.tableLength; i++) {
		//skopiowanie kazdego pola tablicy
		tablePointer[i] = otherTable.tablePointer[i];
	}
	cout << "kopiuj: " << tableName << endl;
}

//Dekstruktor
MyTable::~MyTable() {
	cout << "usuwam: " << tableName << endl;
	delete[] tablePointer;
	tablePointer = NULL;
}

//zmiana nazwy
void MyTable::setName(string name) {
	tableName = name;
}

//zmiana rozmiaru
bool MyTable::setNewSize(int length) {
	if (length < 0) {
		cout << "Nieprawidlowa wartosc length" << endl;
		return false;
	}
	//alokacja nowej tablicy
	int* newTablePointer = new int[length];
	for (int i = 0; i < min(tableLength, length); i++) {
		//przepisanie wartosci z poprzedniej tablicy
		newTablePointer[i] = tablePointer[i];
	}
	//usuniecie starej tablicy oraz zmiana wskaznika
	delete[] tablePointer;
	tablePointer = newTablePointer;
	tableLength = length;
	return true;
}

//metoda kopiujaca
MyTable*  MyTable::clone() {
	MyTable* clonedMyTable = new MyTable(*this);
	return clonedMyTable;
}

//Wyswietlenie stanu
void MyTable::printState()
{
	cout << "MyTable: " << tableName << ", size: " << tableLength << endl;
}

//Funckja nadajaca parametrom odpowiednie wlasciwosci
void MyTable::init(string name, int length) {
	tableName = name;
	tableLength = length;
	tablePointer = new int[tableLength];
}