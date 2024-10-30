#include"MyTree.h";
#include"MyNode.h";
#include<iostream>;
#include<sstream>;
using namespace std;


//konstruktor bezparametrowy
MyTree::MyTree()
{
    root = NULL;
    latestFunction = NULL;
    latestOperator = NULL;
}

//destrktor
MyTree::~MyTree()
{
    delete root;
    // delete latestoperator/function nie jest wymagane, poniewaz usuwaj¹c korzeñ usuwane s¹ równie¿ wszystkie jego podwêz³y rekursywnie   
}

//ustawienie nowego drzewa
void MyTree::setRoot(MyNode* newRoot)
{
    delete root;
    root = newRoot;
}

//obliczenie wartosci drzewa
double MyTree::evaluate()
{
    return root == NULL ? 0 : root->evaluate(variableMap);
}

//zmiana wyrazenia (w formie tablicy string) na obiekty MyNode
MyNode* MyTree::parseExpression(string* tokens, int& index, int size)
{
    //Tutaj wystepuje blad w wyrazeniu
    if (index >= size) {
        NumberNode* numberNode = new NumberNode(1);
        return numberNode;
    }

    // token = nastepna wartosc z tablicy
    string token = tokens[index++];

    // liczby
    if (isdigit(token[0]) || (token[0] == negativeSign && isdigit(token[1]))) {
        double value = stod(token);
        NumberNode* numberNode = new NumberNode(value);
        return numberNode;
    }
    // alfanumeryczne - zmienna lub funkcja
    else if (isalpha(token[0])) {
        if (token == funSin || token == funCos) {
            FunctionNode* funNode = new FunctionNode(token);
            // zaktualizowanie wskaznika do ostatniej funkcji
            latestFunction = funNode;
            MyNode* child = parseExpression(tokens, index, size);
            funNode->setChild(child);
            return funNode;
        }
        //wpisanie nowej (lub nie nowej) zmiennej do variableMap
        variableMap[token];
        VariableNode* variableNode = new VariableNode(token);
        return variableNode;
    }
    // operatory
    else if (token[0] == addSign || token[0] == subSign || token[0] == divSign || token[0] == mulSign) {
        OperatorNode* opNode = new OperatorNode(token[0]);
        // zaktualizowanie wskaznika do ostatniego operatora
        latestOperator = opNode;
        // operator ma 2 podwêzy³y, trzeba je wprowadziæ
        MyNode* left = parseExpression(tokens, index, size);
        MyNode* right = parseExpression(tokens, index, size);
        opNode->setLeftChild(left);
        opNode->setRightChild(right);
        return opNode;
    }
    else return new NumberNode(0);
}

//zmiana wyrazenia na pojedyncze tablice string
string* MyTree::parseToTable(string& expression){
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
int MyTree::getExpressionSize(string& expression)
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
MyTree MyTree::operator+(MyTree& other)
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
    return newTree;

}

void MyTree::operator=(MyTree& other)
{
    // nie mozna zrobic "root = other.root" poniewaz wtedy wszystkie zmiany w other beda rowniez widoczne w glownym drzewie
    string otherExpression = other.toString(); // tak wiec trzebastworzyæ g³êbok¹ kopie wszystkich podwêz³ów
    string* otherExpressionTable = parseToTable(otherExpression);
    int index = 0;
    delete root;
    root = parseExpression(otherExpressionTable,index,getExpressionSize(otherExpression));
    delete[] otherExpressionTable;
}

// zliczenie zmiennych i wpisanie ich do variableMap
void MyTree::updateVariableMap()
{
    variableMap.clear();
    string thisExpressionString = toString();
    string* tokens = parseToTable(thisExpressionString);
    int thisSize = getExpressionSize(thisExpressionString);
    for (int i = 0; i < thisSize; i++) {
        string token = tokens[i];
        if (isalpha(token[0]) && token != funSin && token!= funCos) {
            variableMap[token] = 0;
        }
    }
}


string MyTree::toString()
{
    return root->toString();
}

//wprowadzenie wyrazenia
void MyTree::enter(string expression){
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
void MyTree::print() {
    if (root == NULL) cout << emptyTreeMessage<<endl;
    else cout <<treeFormulaMessage<< toString() << endl;
}

//obliczenie wartosci drzewa
void MyTree::comp(string variables){
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
        std::map<std::string, double>::iterator it;
        for (it = variableMap.begin(); it != variableMap.end(); ++it) {
            variableMap[it->first] = stod(variablesTable[index++]);
        }
        delete[] variablesTable;
    }
    //jesli wystapilo dzielenie przez zero to wartosc == nieskonczonosc
    double val = evaluate();
    if(val==INFINITY) cout << errorDivideByZero << std::endl;
    else cout << treeValueMessage << val << endl;
    
 }

//Wypisanie wszystkich zmiennych w drzewie
void MyTree::vars(int begin) // argument opcjonalny (w celu wypisania nieprzypisanych zmiennych)
{
    if (variableMap.size() == 0) return;
    cout << variablesMessage;
    std::map<std::string, double>::iterator it = variableMap.begin();
    // uzywane w przypadku wywolania "comp" jesli wprowadzona zbyt mala ilosc argumentow
    while (begin != 0) {
        ++it;
        begin--;
    }
    for (it; it != variableMap.end(); ++it) {
        std::cout << it->first << " ";
    }
    cout << endl;
}

// przylaczenie nowego drzewa
void MyTree::join(std::string expression){
    //pierw trzeba nowe drzewo stworzyc z formu³y
    MyTree* newTree = new MyTree;
    // ... za pomoca funkcji enter
    newTree->enter(expression);

    // jesli nowe drzewo jest puste to koniec dzialania funkcji
    if (newTree->root == NULL) {
        delete newTree;
        return;
    }
    MyNode* newTreeRoot = newTree->root;
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
    }else if (newTree->latestFunction != NULL) {
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
void MyTree::startInterface()
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
        else if (command == compCommand) {
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
        else if (command == helpCommand)
        {
            cout << avaliableCommandsMessage << endl;
        }
        else if (command == quitCommand) {
            cout<<exitMessage<<endl;
        }
        else {
            cerr <<invalidCommandWarning<< endl;
        }
    }
}


