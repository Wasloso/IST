// plik przechowuje informacje o samyn drzewie MyTree
// drzewo posiada wskaŸnik do korzenia - pocz¹tku drzewa, oraz do ostatniego operatora i funkcji
// wskazniki do operatora i funkcji s¹ wykorzystywane do przeprowadzenia operacji ³¹czenia dwóch drzew
// drzewo posiada równie¿ mapê wszystkich wykorzystanych w nim zmiennych, która jest potrzebna do obliczenia wartoœci wêz³ów ze zmiennymi

#pragma once
#include "MyNode.h"
#include "constants.h"
#include <string>
#include <map>
#include <sstream>
#include <iostream>
using namespace std;

template <class T>
class MyTree {
private:
    MyNode<T>* root;
    OperatorNode<T>* latestOperator;
    FunctionNode<T>* latestFunction;
    std::map<std::string, T> variableMap;

public:
    MyTree();
    MyTree(MyTree&& other); //konstruktor przenosz¹cy
    ~MyTree();
    void setRoot(MyNode<T>* newRoot);
    T evaluate();
    MyNode<T>* parseExpression(std::string* tokens, int& index, int size);
    std::string* parseToTable(std::string& expression);
    int getExpressionSize(std::string& expression);
    MyTree<T> operator+(MyTree<T>& other);
    void operator=(MyTree<T>& other);
    MyTree<T>& operator=(MyTree<T>&& other);
    void updateVariableMap();
    std::string toString();
    void enter(std::string expression);
    void print();
    void comp(std::string variables = "");
    void vars(int begin = 0);
    void join(std::string expression);
    void startInterface();
};

//konstruktor bezparametrowy
template <class T>
MyTree<T>::MyTree()
{
    root = NULL;
    latestFunction = NULL;
    latestOperator = NULL;
}

//konstruktor przenoszenia
template<class T>
MyTree<T>::MyTree(MyTree&& other)
{
    *this = move(other);
}

//destrktor
template <class T>
MyTree<T>::~MyTree()
{
    delete root;
    // delete latestoperator/function nie jest wymagane, poniewaz usuwaj¹c korzeñ usuwane s¹ równie¿ wszystkie jego podwêz³y rekursywnie   
}

//ustawienie nowego drzewa
template <class T>
void MyTree<T>::setRoot(MyNode<T>* newRoot)
{
    delete root;
    root = newRoot;
}

//obliczenie wartosci drzewa
template <class T>
T MyTree<T>::evaluate()
{
    return root == NULL ? T() : root->evaluate(variableMap);
}

//zmiana wyrazenia (w formie tablicy string) na obiekty MyNode (dla int i double)
template <class T>
MyNode<T>* MyTree<T>::parseExpression(string* tokens, int& index, int size)
{
    //Tutaj wystepuje blad w wyrazeniu
    if (index >= size) {
        NumberNode<T>* numberNode = new NumberNode<T>(1);
        return numberNode;
    }

    // token = nastepna wartosc z tablicy
    string token = tokens[index++];

    // liczby
    if (isdigit(token[0]) || (token[0] == negativeSign && isdigit(token[1]))) {
        T value = stod(token);
        NumberNode<T>* numberNode = new NumberNode<T>(value);
        return numberNode;
    }
    // alfanumeryczne - zmienna lub funkcja
    else if (isalpha(token[0])) {
        if (token == funSin || token == funCos) {
            FunctionNode<T>* funNode = new FunctionNode<T>(token);
            // zaktualizowanie wskaznika do ostatniej funkcji
            latestFunction = funNode;
            MyNode<T>* child = parseExpression(tokens, index, size);
            funNode->setChild(child);
            return funNode;
        }
        //wpisanie nowej (lub nie nowej) zmiennej do variableMap
        variableMap[token];
        VariableNode<T>* variableNode = new VariableNode<T>(token);
        return variableNode;
    }
    // operatory
    else if (token[0] == addSign || token[0] == subSign || token[0] == divSign || token[0] == mulSign) {
        OperatorNode<T>* opNode = new OperatorNode<T>(token[0]);
        // zaktualizowanie wskaznika do ostatniego operatora
        latestOperator = opNode;
        // operator ma 2 podwêzy³y, trzeba je wprowadziæ
        MyNode<T>* left = parseExpression(tokens, index, size);
        MyNode<T>* right = parseExpression(tokens, index, size);
        opNode->setLeftChild(left);
        opNode->setRightChild(right);
        return opNode;
    }
    else return new NumberNode<T>(1);
}

// implementacja przetwarzania ekspresji dla typu string
template <>
MyNode<string>* MyTree<string>::parseExpression(string* tokens, int& index, int size) {

    if (index >= size) {
        NumberNode<string>* numberNode = new NumberNode<string>(defaultNumberNodeString);
        return numberNode;
    }
    string token = tokens[index++];

    if (token[0] == addSign || token[0] == subSign || token[0] == divSign || token[0] == mulSign) {
        OperatorNode<string>* opNode = new OperatorNode<string>(token[0]);
        // zaktualizowanie wskaznika do ostatniego operatora
        latestOperator = opNode;
        // operator ma 2 podwêzy³y, trzeba je wprowadziæ
        MyNode<string>* left = parseExpression(tokens, index, size);
        MyNode<string>* right = parseExpression(tokens, index, size);
        opNode->setLeftChild(left);
        opNode->setRightChild(right);
        return opNode;
    }
    else if (token[0] == quote) {
        token = token.substr(1, token.length() - 2);
        NumberNode<string>* numberNode = new NumberNode<string>(token);
        return numberNode;
    }
    else if (isalpha(token[0])) {
        //wpisanie nowej (lub nie nowej) zmiennej do variableMap
        variableMap[token];
        VariableNode<string>* variableNode = new VariableNode<string>(token);
        return variableNode;
    }
    else return new NumberNode<string>(defaultNumberNodeString);
};


//zmiana wyrazenia na pojedyncze tablice string
template <class T>
string* MyTree<T>::parseToTable(string& expression) {
    // strumien string z naszego expression
    istringstream iss(expression);
    string* tokens = new string[getExpressionSize(expression)];
    string token;
    int index = 0;
    //dopoki expression zawiera wartosci - przypisywanie ich do kolejnych komórek tablicy
    while (iss >> token) {
        tokens[index++] = token;
    }
    return tokens;
}

//obliczenie rozmiaru wyrazenia
template <class T>
int MyTree<T>::getExpressionSize(string& expression)
{
    // strumien string z naszego expression
    istringstream iss(expression);
    string token;
    int size = 0;
    // dopoki s¹ wartosci w strumieniu - zwiekszenie size
    while (iss >> token) {
        size++;
    }
    return size;
}

// dodanie jednego drzewa do drugiego za pomoca funkcji join
template <class T>
MyTree<T> MyTree<T>::operator+(MyTree<T>& other)
{
    MyTree newTree;
    // oba drzewa nie mog¹ byæ puste
    if (root == NULL || other.root == NULL) {
        return newTree;
    }
    string thisExpression = toString();
    string otherExpression = other.toString();
    newTree.enter(thisExpression);
    newTree.join(otherExpression);
    return move(newTree);
}

template <class T>
void MyTree<T>::operator=(MyTree<T>& other)
{
    // nie mozna zrobic "root = other.root" poniewaz wtedy wszystkie zmiany w other beda rowniez widoczne w glownym drzewie
    string otherExpression = other.toString(); // tak wiec trzebastworzyæ g³êbok¹ kopie wszystkich podwêz³ów
    string* otherExpressionTable = parseToTable(otherExpression);
    int index = 0;
    delete root;
    root = parseExpression(otherExpressionTable, index, getExpressionSize(otherExpression));
    delete[] otherExpressionTable;
}

// operator przenoszenia
template<class T>
MyTree<T>& MyTree<T>::operator=(MyTree<T>&& other)
{
    if (this == &other) return *this;
    // usuwamy stary korzen (latestF i latestO usuna sie automatycznie przy usuwaniu korzenia)
    delete root;
    // przypisujemy wskazniki z przenoszonego drzewa do naszego drzewa
    root = other.root;
    latestFunction = other.latestFunction;
    latestOperator = other.latestOperator;
    variableMap = other.variableMap;
    // i ustawiamy wskazniki przenoszenego drzewa na null
    other.root = NULL;
    other.latestFunction = NULL;
    other.latestOperator = NULL;
    
    return *this;
}

// zliczenie zmiennych i wpisanie ich do variableMap
template <class T>
void MyTree<T>::updateVariableMap()
{
    variableMap.clear();
    string thisExpressionString = toString();
    string* tokens = parseToTable(thisExpressionString);
    int thisSize = getExpressionSize(thisExpressionString);
    for (int i = 0; i < thisSize; i++) {
        string token = tokens[i];
        if (isalpha(token[0]) && token != funSin && token != funCos) {
            variableMap[token] = T();
        }
    }
}

template <>
void MyTree<string>::updateVariableMap()
{
    variableMap.clear();
    string thisExpressionString = toString();
    string* tokens = parseToTable(thisExpressionString);
    int thisSize = getExpressionSize(thisExpressionString);
    for (int i = 0; i < thisSize; i++) {
        string token = tokens[i];
        if (isalpha(token[0])) {
            variableMap[token] = emptyString;
        }
    }
}

template <class T>
string MyTree<T>::toString()
{
    return root->toString();
}

//wprowadzenie wyrazenia
template <class T>
void MyTree<T>::enter(string expression) {
    if (expression.size() == 0) {
        cout << emptyExpressionWarning << endl;
        return;
    }
    // nowe drzewo - wyczyszczenie mapy poprzednich zmiennych oraz korzenia
    variableMap.clear();
    delete root;

    string* epxressionTable = parseToTable(expression); // zmiana formjuly na tablice string
    int index = 0;
    int expressionSize = getExpressionSize(expression); // obliczenie wielkosci tablicy
    root = parseExpression(epxressionTable, index, expressionSize);

    // sprawdzenie czy formula byla poprawna (w sensie czy kazdy operator mial odpowiednia ilosc dzieci itp)
    string parsedFormula = toString();
    int evaluatedExpressionSize = getExpressionSize(parsedFormula);
    if (expressionSize != evaluatedExpressionSize) {
        cout << incompleteExpressionWarning << toString() << endl;
    }
    delete[] epxressionTable;
}

//wyswietlenie wyrazenia
template <class T>
void MyTree<T>::print() {
    if (root == NULL) cout << emptyTreeMessage << endl;
    else cout << treeFormulaMessage << toString() << endl;
}

//obliczenie wartosci drzewa
template <class T>
void MyTree<T>::comp(string variables) {
    //puste drzewo
    if (root == NULL) {
        cout << emptyTreeMessage << endl;
        return;
    }

    // jeœli s¹ zmienne w drzewie to trzeba je wczytaæ
    if (variableMap.size() != 0) {
        int size = getExpressionSize(variables);
        // iloœæ wpisanych wyra¿eñ nie zgadza siê z wyrazeniami w drzewie
        if (size < variableMap.size()) {
            vars(size);
            cout << missingVariablesWarning << endl;
            return;
        }
        else if (size > variableMap.size()) {
            cout << tooManyVariablesWarning << endl;
            return;
        }
        string* variablesTable = parseToTable(variables);
        for (int i = 0; i < size; i++) {
            if (isalpha(variablesTable[i][0])) {
                cout << variablesNotNumbers << endl;
                return;
            }
        }
        int index = 0;
        typename std::map<std::string, T>::iterator it;
        for (it = variableMap.begin(); it != variableMap.end(); ++it) {
            variableMap[it->first] = stod(variablesTable[index++]);
        }
        delete[] variablesTable;
    }
    //jesli wystapilo dzielenie przez zero to wartosc == nieskonczonosc
    T val = evaluate();
    if (val == INFINITY) cout << errorDivideByZero << std::endl;
    else cout << treeValueMessage << val << endl;

}

template <>
void MyTree<string>::comp(string variables) {
    //puste drzewo
    if (root == NULL) {
        cout << emptyTreeMessage << endl;
        return;
    }
    if (variableMap.size() != 0) {
        int size = getExpressionSize(variables);
        // iloœæ wpisanych wyra¿eñ nie zgadza siê z wyrazeniami w drzewie
        if (size < variableMap.size()) {
            vars(size);
            cout << missingVariablesWarning << endl;
            return;
        }
        else if (size > variableMap.size()) {
            cout << tooManyVariablesWarning << endl;
            return;
        }
        string* variablesTable = parseToTable(variables);
        for (int i = 0; i < size; i++) {
            if (variablesTable[i][0] != quote) {
                cout << variablesNotNumbers << endl;
                return;
            }
        }
        int index = 0;
        typename std::map<std::string, string>::iterator it;
        for (it = variableMap.begin(); it != variableMap.end(); ++it) {
            variableMap[it->first] = variablesTable[index++];
        }
        delete[] variablesTable;
    }
    string val = evaluate();
    cout << treeValueMessage << val << endl;
}



//Wypisanie wszystkich zmiennych w drzewie
template <class T>
void MyTree<T>::vars(int begin) // argument ojonalny (w celu wypisania nieprzypisanych zmiennych)
{
    if (variableMap.size() == 0) return;
    cout << variablesMessage;
    typename std::map<std::string, T>::iterator it = variableMap.begin();
    // uzywane w przypadku wywolania "comp" jesli wprowadzona zbyt mala ilosc argumentow
    while (begin != 0) {
        ++it;
        begin--;
    }
    for (it; it != variableMap.end(); ++it) {
        std::cout << it->first << space;
    }
    cout << endl;
}

// przylaczenie nowego drzewa
template <class T>
void MyTree<T>::join(std::string expression) {
    //pierw trzeba nowe drzewo stworzyc z formu³y
    MyTree<T>* newTree = new MyTree<T>;
    // ... za pomoca funkcji enter
    newTree->enter(expression);

    // jesli nowe drzewo jest puste to koniec dzialania funkcji
    if (newTree->root == NULL) {
        delete newTree;
        return;
    }
    MyNode<T>* newTreeRoot = newTree->root;
    if (latestOperator != NULL) {
        // zwolnienie pamieci poprzedniego dziecka
        delete latestOperator->getRightChild();
        latestOperator->setRightChild(newTreeRoot);
    }
    //analogicznie w przypadku funNode
    else if (latestFunction != NULL) {
        delete latestFunction->getChild();
        latestFunction->setChild(newTreeRoot);
    }
    // przypadek gdzie drzewo nie mialo zandych operatorow ani funckji - mialo jeden wezel liczbbowy lub zmienna
    else {
        root = newTreeRoot;
    }

    // zmiana ostatniego operatora/funkcji
    if (newTree->latestOperator != NULL) {
        // nowy "ostatni" operator bedzie ostatnim operatorem nowego drzewa
        latestOperator = newTree->latestOperator;
    }
    else if (newTree->latestFunction != NULL) {
        latestFunction = newTree->latestFunction;
    }

    // zmiana wskaznika korzenia nowego drzewa na null umozliwia jego usuniecie
    newTree->root = NULL;
    // zwolenienie pamieci
    delete newTree;
    // aktualizacja mapy zmiennych o potencjalne nowe wartosci z dolaczonego drzewa
    updateVariableMap();
}


// obsluga komand terminala
template <class T>
void MyTree<T>::startInterface()
{
    string input;
    string command;
    string expression;

    while (command != quitCommand) {
        cout << enterPrompt;
        cin >> command;

        //rozpatrzenie wszystkich mozliwych komend
        if (command == enterCommand) {
            getline(cin >> ws, expression);
            enter(expression);
        }
        else if (command == comommand) {
            if (variableMap.size() != 0) {
                getline(cin >> ws, expression);
            }
            comp(expression);
        }
        else if (command == joinCommand) {
            getline(cin >> ws, expression);
            join(expression);
        }
        else if (command == varsCommand) {
            vars();
        }
        else if (command == printCommand) {
            print();
        }
        else if (command == helommand)
        {
            cout << avaliableCommandsMessage << endl;
        }
        else if (command == quitCommand) {
            cout << exitMessage << endl;
        }
        else {
            cerr << invalidCommandWarning << endl;
        }
    }
}


