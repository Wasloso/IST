#include "MyNumber.h"
#include<iostream>
using namespace std;

//konstruktor bezparametryczny
MyNumber::MyNumber() {
    table = NULL;
    tableLength = 0;
    isPositive = true;
}

//konstruktor parametryczny
MyNumber::MyNumber(int value) {
    *this = value;
}


//destruktor
MyNumber::~MyNumber() {
    clear();
}

//funkcja do usuwania 
void MyNumber::clear()
{
    if (table) {
        delete[] table;
        table = nullptr;
        tableLength = 0;
    }
}

//operator przypisania przez int
void MyNumber::operator=(int value)
{
    //usuniecie poprzedni zaalokowanej tablicy
    clear();
    if (value < 0) {
        isPositive = false;
        value *= -1;
    }
    else isPositive = true;

    //szczegolny przypadek dla value = 0;
    if (value == 0) {
        tableLength = 1;
        table = new int[tableLength];
        table[0] = 0;
        return;
    }
    tableLength = getIntLength(value);
    table = new int[tableLength];
    int pos = tableLength - 1,digit;
    while (value != 0) {
        digit = value % 10;
        value /= 10;
        table[pos--] = digit;
    }
}

//operator przypisania
void MyNumber::operator=(const MyNumber& other) {
    //sprawdzenie zeby nie przypisywac do samej siebie
    if (this == &other) {
        return;
    }
    clear();
    //kopiowanie wszyatkich parametrow
    isPositive = other.isPositive;
    tableLength = other.tableLength;
    table = new int[tableLength];
    for (int i = 0; i < tableLength; i++) {
        table[i] = other.table[i];
    }
}

MyNumber const MyNumber::operator+(MyNumber& other) {
    MyNumber result;

    //sprawdzenie znaków
    if (isPositive && other.isPositive) {
        result.isPositive = true;
    }
    else if (!isPositive && !other.isPositive) {
        result.isPositive = false;
    }
    else {
        //porownanie absoultne
        int comp = compareAbsTo(other);
        if (comp == 0) {
            result = 0;
            return result;
        }
        //dodatnia i ujemna
        if (isPositive && !other.isPositive) {
            //ujemna mniejsza absoultnie
            if (comp == 1) {
                MyNumber abs;
                abs = other;
                abs.isPositive = true;
                result = *this - abs;
                result.isPositive = true;
                return result;
            }
            //ujemna wieksza absolutnie
            else {
                MyNumber abs;
                abs = other;
                abs.isPositive = true;
                result = abs - *this;
                result.isPositive = false;
                return result;
            }
        }
        //ujemna i dodatnia
        else {
            //dodatnia mniejsza absolutnie
            if (comp == 1) {
                MyNumber abs;
                abs = *this;
                abs.isPositive = true;
                result = abs - other;
                result.isPositive = false;
                return result;
            }
            //dodatnia wieksza absolutnie
            else {
                MyNumber abs;
                abs = *this;
                abs.isPositive = true;
                result = other-abs;
                result.isPositive = true;
                return result;
            }
        }

    }
   
    //pozycja startowa this
    int thisPos = tableLength - 1;
    //pozycja other
    int otherPos = other.tableLength - 1;
    //najwieksza mozliwa dlugosc tabeli = max(m,n)+1
    int maxLen = std::max(tableLength, other.tableLength) + 1;;
    result.tableLength = maxLen;
    result.table = new int[maxLen];
    // sum - suma czesciowa komorek, carry - przeniesienie, resultPos - pozycja w tablocy wyniku
    int sum = 0, carry = 0, resultPos = maxLen-1;;

    //wypelnienie zerami
    for (int i = 0; i < maxLen; i++) {
        result.table[i] = 0;
    }

    while (resultPos >=0) {
        sum = carry;
        //dodawanie nastepnych komorek 
        if (thisPos >= 0) {
            sum += table[thisPos--];
        }
        if (otherPos >= 0) {
            sum += other.table[otherPos--];
        }
        //dzieleni calkowite sumy = przeniesienie
        carry = sum / 10;
        //suma czesciowa z czesci jednosci
        sum %= 10;
        result.table[resultPos--] = sum;
    }
    //usunienicie nieznaczacych komorek
    shrink(result);

    return result;
}

// operator dodawania MyNumber+int
MyNumber const MyNumber::operator+(const int value) {
    //zmiana int na MyNumber i wywolanie operatora+
    MyNumber other = value;
    return (*this + other);
}

MyNumber const MyNumber::operator-(MyNumber& other) {
    MyNumber result;

    // porownanie absolutne dwoch liczb (1 - pierwsza jest wieksza, 0 - takie same, -1 - pierwsza jest mniejsza absolutnie)
    int compare = compareAbsTo(other);
    //rozwazanie roznyh przypadkow w zaleznosci od znaku liczb
    if (isPositive && other.isPositive) {
        //dodatnia i dodatnia

        //takie same = 0
        if (compare == 0) {
            result = 0;
            return result;
        }
        //dodatnia1 - dodatnia2 = dodatnia (ujemna mniejsza absolutnie)
        else if(compare==1){
            result.isPositive = true;
        }
        //dodatnia1 - dodatnia2 = -|dodatnia2-dodatnia1| (ujemna wieksza absolutnie)
        else {
            result = other - *this;
            result.isPositive = false;
            return result;
        }
    }
    else if (!isPositive && !other.isPositive) {
        //ujemna i ujemna

        //takie same = 0
        if (compare == 0) {
            result = 0;
            return result;
        }
        //ujemna1 - ujemna2 = ujemna (pierwsza jest wieksza absoutnie)
        else if (compare == 1) {
            result = false;
        }
        //ujemna1 - ujmena2 = |ujemna2-ujemna1|
        else {
            result = other - *this;
            result.isPositive = true;
            return result;
        }
    }
    else if (isPositive) {
        //dodatnia-ujemna = dodatnia+(-ujemna)

        //robienie kopii absolutnej drugiej liczby
        MyNumber abs;
        abs = other;
        abs.isPositive = true;
        result = *this + abs;
        result.isPositive = true;
        return result;
    }
    else {
        //ujemna-dodatnia = -(|ujemna|+dodatnia)
        MyNumber abs;
        abs = *this;
        abs.isPositive = true;
        result = abs + other;
        result.isPositive = false;
        return result;
    }

    int thisPos = tableLength - 1;
    int otherPos = other.tableLength - 1;
    int maxLen = std::max(tableLength, other.tableLength);

    result.tableLength = maxLen;
    result.table = new int[maxLen];

    //wypelnienie zerami
    for (int i = 0; i < maxLen; i++) {
        result.table[i] = 0;
    }

    //diff - roznica poszczegolnych komorek, borrow - pozyczenie
    int diff = 0, borrow = 0;
    int resultPos = maxLen - 1;

    while (resultPos >= 0) {
        //thisdDigit, otherDigit - wartosci kolejnych komorek tablic (jesli juz nie istenia to przyjmuja wartosc 0)
        int thisDigit = thisPos >= 0 ? table[thisPos--] : 0;
        int otherDigit = otherPos >= 0 ? other.table[otherPos--] : 0;
        //roznica = T[i]-O[i]-borrow
        int diff = thisDigit - otherDigit - borrow;
        //jesli roznica jest ujemna to wymagane jest pozyczenie
        if (diff < 0) {
            diff += 10;
            borrow = 1;
        }
        else {
            borrow = 0;
        }
        result.table[resultPos--] = diff;
    }
    //skrocenie wyniku o niepotrzebne zera
    shrink(result);

    return result;
}

MyNumber const MyNumber::operator*(MyNumber& other) {
    MyNumber result;

    //sprawdzenie znaku
    if ((isPositive && other.isPositive) || (!isPositive && !other.isPositive)) result.isPositive = true;
    else result.isPositive = false;

    int thisLen = tableLength;
    int otherLen = other.tableLength;

    //maksymalna dlugosc tablicy wyniku = n+m
    int maxLen = thisLen + otherLen;

    int resultPos;
    int thisPos = thisLen - 1;
    int otherPos = otherLen - 1;

    result.tableLength = maxLen;
    result.table = new int[maxLen];

    //product - wynik mnozenia nastepnych komorek, carry - przeniesienie
    int product = 0, carry = 0;

    for (int i = 0; i < maxLen; i++) {
        result.table[i] = 0;
    }

    //mnozenie poszczegolnej komorki z "this" z kazda komorka "other" i odpowiedne pzrypisanie do wyniku
    while (thisPos >= 0) {
        otherPos = otherLen - 1;
        while (otherPos >= 0) {
            resultPos = thisPos + otherPos + 1;
            product = table[thisPos] * other.table[otherPos] + carry + result.table[resultPos];
            carry = product / 10;
            product %= 10;
            result.table[resultPos] = product;
            otherPos--;
        }
        //dodanie pozostalego przeniesienia na nastepne pola
        while (carry != 0) {
            product = carry + result.table[--resultPos];
            carry = product / 10;
            product %= 10;
            result.table[resultPos] = product;
        }
        thisPos--;
    }
    //usuniecie nieznaczacych komorek
    shrink(result);
    
    return result;
}

MyNumber const MyNumber::operator/(MyNumber& other) {
    MyNumber result(0);
    //blad przy dzieleniu przez zero
    if (other == 0) {
        std::cerr << zeroDivisionERR << endl;
        return result;
    }
    //okreslenie znaku
    if ((isPositive && other.isPositive) || (!isPositive && !other.isPositive)) result.isPositive = true;
    else result.isPositive = false;
    //kopia bezwzgledna this i other
    MyNumber thisCopy, otherCopy;
    thisCopy = *this;
    thisCopy.isPositive = true;
    otherCopy = other;
    otherCopy.isPositive = true;
    //odejmowanie other od this dopoki this jest wieksze
    if (thisCopy.compareAbsTo(other) == -1) {
        result = 0;
        result.isPositive = true;
        return result;
    }
    while (thisCopy.compareAbsTo(other) == 1) {
        thisCopy = thisCopy - otherCopy;
        result = result + 1;
    }
    return result;
}

//operator porwnania
bool const MyNumber::operator==(const MyNumber& other)
{
    if (isPositive != other.isPositive) return false;
    return compareAbsTo(other) == 0 ? true : false;
}
bool const MyNumber::operator==(const int value) {
    MyNumber other = value;
    return *this == other;

}


//porownanie wartosci absolutnych 1- wiekszaS, 0- taka sama, -1-mniejsza
int const MyNumber::compareAbsTo(const MyNumber& other) {
    if (tableLength > other.tableLength) return 1;
    if (tableLength < other.tableLength) return -1;
    for (int i = 0; i < tableLength; i++) {
        if (table[i] > other.table[i]) return 1;
        else if (table[i] < other.table[i]) return -1;
    }
    return 0;
}


//metoda usuwaj¹ca puste wartosci z poczatku tablicy
void MyNumber::shrink(MyNumber& result)
{
    int pos = 0;
    int maxLen = result.tableLength;
    while (result.table[pos] == 0) {
        maxLen--;
        pos++;
    }
    if (maxLen != result.tableLength) {
        int* newTable = new int[maxLen];
        for (int i = 0; i < result.tableLength; i++) {
            newTable[i] = result.table[i + pos];
        }
        delete[] result.table;
        result.table = newTable;
        result.tableLength = maxLen;
    }
}

//metoda pomocnicza do obliczania dlugosci inta
int MyNumber::getIntLength(int value) {
    int size = 0;
    while (value != 0) {
        value /= 10;
        size++;
    }
    return size;
}


string MyNumber::toString() {
    //dodatnie nastepnych komorek z table do tekstu wynikowego
    string text = toStringText; 
    if (!isPositive) text += toStringNegativeSign;
    for (int i = 0; i < tableLength; i++) {
        text += table[i]+'0';
    }
    return text;
}
