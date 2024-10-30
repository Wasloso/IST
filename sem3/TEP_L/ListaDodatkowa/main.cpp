#include <iostream>
#include "MyString.h"

using namespace std;

int main()
{
    MyString example1;
    example1 = "Ala ";
    example1 += "ma ";
    MyString example2(example1);
    example2 = example2 + "kota i psa";
    MyString example3;


    cout << example1.ToStandard() << endl;
    cout << example2.ToStandard() << endl;
    cout << example3.ToStandard() << endl;

    cout << example1 << endl;
    cout << example2 << endl;
    cout << example3 << endl;


    return 0;
}
