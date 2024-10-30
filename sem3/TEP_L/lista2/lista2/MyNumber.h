#pragma once
#include<string>
using namespace std;

class MyNumber {
public:
	//kons bezparametrowy
	MyNumber();
	//parametrowy
	MyNumber(int value);
	//destruktor
	~MyNumber();
	//przsypisujacy
	void operator=(int value);
	//przsypisujacy
	void operator=(const MyNumber& other);
	//operator dodawania
	const MyNumber operator+(MyNumber& other);
	//operator dodawania
	const MyNumber operator+(const int value);
	//operator odejmowania
	const MyNumber operator-(MyNumber& other);
	//operator mnozenia
	const MyNumber operator*(MyNumber& other);
	//operator dzielenia
	const MyNumber operator/(MyNumber& other);
	//operator rownosci
	const bool operator==(const MyNumber& other);
	//operator rownosci
	const bool operator==(const int value);
	//porwoannianie wartosci absolutnych
	const int compareAbsTo(const MyNumber& other);
	//funckja skracajaca tabele o niepotrzebne zera
	void shrink(MyNumber& result);
	//funckja usuwajaca tabele
	void clear();
	//funckja zwracajaca dlugosc int
	int getIntLength(int value);
	std::string toString();
private:
	int tableLength;
	int *table;
	bool isPositive;

};

const static string toStringText = "MyNumber: ";
const static string toStringNegativeSign = "-";
const static string zeroDivisionERR= "ERROR: CAN'T DIVIDE BY ZERO";