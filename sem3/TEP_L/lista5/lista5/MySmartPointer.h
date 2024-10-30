// implementacja smart pointer

#pragma once
#include "RefCounter.h";

template <class T>
class MySmartPointer
{
public:
	MySmartPointer(T* Pointer);
	MySmartPointer(const MySmartPointer& Other);
	~MySmartPointer();
	void operator=(const MySmartPointer& other);
	T& operator*();
	T* operator->();
private:
	RefCounter* counter;
	T* pointer;
};
// konstruktor, tworzenie nowego RefCounter oraz inkrementacja jego wartosci
template <class T>
MySmartPointer<T>::MySmartPointer(T* Pointer) {
	pointer = Pointer;
	counter = new RefCounter();
	counter->Add();
}
// konstruktor kopiujacy, przypisanie wskaznikow na pointer oraz counter z other oraz inkrementacja RefCounter
template <class T>
MySmartPointer<T>::MySmartPointer(const MySmartPointer& other) {
	pointer = other.pointer;
	counter = other.counter;
	counter->Add();
}
// desktrukot - usuniecie pointer i counter tylko jesli nie ma juz wiecej "kopii" tego smart pointera
template <class T>
MySmartPointer<T>::~MySmartPointer() {
	if (counter->Dec() == 0)
	{
		delete pointer;
		delete counter;
	}
}
// operator kopiujacy
template<class T>
void MySmartPointer<T>::operator=(const MySmartPointer& other) {
	if (this == &other) return;
	// usuniece poprzednio przechowywanych wartosci jesli nie ma wiecej kopii tego smart pointera
	if (counter->Dec() == 0) {
		delete pointer;
		delete counter;
	}

	pointer = other.pointer;
	counter = other.counter;

	counter->Add();
}

// operator zwracajacy wartosc wskaznika
template<class T>
T& MySmartPointer<T>::operator*()
{
	return(*pointer);
}
// operator pozwalajacy na uzycie wskaznika (np. jego metod jesli jest to jakas klasa)
template<class T>
T* MySmartPointer<T>::operator->()
{
	return(pointer);
}
//~MySmartPointer()