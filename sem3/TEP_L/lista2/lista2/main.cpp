//Patryk £uszczek
//lista 2

#include<iostream>
#include "MyNumber.h"



using namespace std;

int main() {
	//Sprawdzenie dzialania operatorow dla wielu przypadkow - liczby dodatnie i ujemne
	MyNumber pos1, pos2, pos3, pos4, pos5;
	pos1 = 12;
	pos2 = 41912;
	pos3 = 3569842;
	pos4 = 1023950200;
	pos5 = 3;
	MyNumber neg1, neg2, neg3, neg4, neg5;
	neg1 = -12312;
	neg2 = -293924;
	neg3 = -2;
	neg4 = -25;
	neg5 = -39210;
	MyNumber res1, res2, res3, res4, res5, res6, res7, res8;
	res1 = pos1 - pos2;
	cout << res1.toString() << endl;
	res2 = pos2 / pos1;
	cout << res2.toString() << endl;
	res3 = neg5 / neg4;
	cout << res3.toString() << endl;
	res4 = pos4 / neg2;
	cout << res4.toString() << endl;
	res5 = pos4 * neg3;
	cout << res5.toString() << endl;
	res6 = neg5 - pos3;
	cout << res6.toString() << endl;
	res7 = neg1 - neg2;
	cout << res7.toString() << endl;
	res8 = pos5 - pos5;
	cout << res8.toString() << endl;

	return 0;
}