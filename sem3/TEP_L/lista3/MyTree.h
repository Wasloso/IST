// plik przechowuje informacje o samyn drzewie MyTree
// drzewo posiada wskaŸnik do korzenia - pocz¹tku drzewa, oraz do ostatniego operatora i funkcji
// wskazniki do operatora i funkcji s¹ wykorzystywane do przeprowadzenia operacji ³¹czenia dwóch drzew
// drzewo posiada równie¿ mapê wszystkich wykorzystanych w nim zmiennych, która jest potrzebna do obliczenia wartoœci wêz³ów ze zmiennymi

#pragma once
#include "MyNode.h"
#include <string>
#include <map>

class MyTree {
private:
	MyNode* root;
	OperatorNode* latestOperator;
	FunctionNode* latestFunction;
	std::map<std::string, double> variableMap;

public:
	MyTree();
	~MyTree();
	void setRoot(MyNode* newRoot);
	double evaluate();
	MyNode* parseExpression(std::string* tokens, int& index,int size);
	std::string* parseToTable(std::string& expression);
	int getExpressionSize(std::string& expression);
	MyTree operator+(MyTree& other);
	void operator=(MyTree& other);
	void updateVariableMap();
	std::string toString();
	void enter(std::string expression);
	void print();
	void comp(std::string variables="");
	void vars(int begin = 0);
	void join(std::string expression);
	void startInterface();
};


const static char negativeSign = '-';

const static std::string emptyExpressionWarning = "Empty expression!";
const static std::string incompleteExpressionWarning = "Incorrect expression. The following one will be considered: ";
const static std::string missingVariablesWarning = "havent been initialized!";
const static std::string tooManyVariablesWarning = "Too many arguments!";
const static std::string invalidCommandWarning = "Invalid command. Type \"?\" for list of commands";
const static std::string variablesNotNumbers = "All variables must be numbers!";

const static std::string emptyTreeMessage = "Empty tree";
const static std::string treeFormulaMessage = "Tree formula: ";
const static std::string treeValueMessage = "Value of tree: ";
const static std::string variablesMessage = "Variables: ";
const static std::string exitMessage = "Quitting...";


const static std::string enterPrompt = "Enter a command: ";
const static std::string quitCommand = "quit";
const static std::string compCommand = "comp";
const static std::string varsCommand = "vars";
const static std::string printCommand = "print";
const static std::string enterCommand = "enter";
const static std::string joinCommand = "join";
const static std::string helpCommand = "?";

const static std::string avaliableCommandsMessage = "Avaliable commands:\n   " + enterCommand + " <formula>\n   " + compCommand + " <var1> <var2> ...\n   " + varsCommand + "\n   " + joinCommand + " <formula>\n   " + printCommand + "\n   " + quitCommand;





