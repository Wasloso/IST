// plik przechowuje imformacje o wêz³ach drzewa
// wêz³y zosta³y podzielone na 4 kategorie wykorzystuj¹c w³aœciwoœci dziediczenia
// klasa MyNode jest abstrakcyjn¹ klas¹ i nie jest u¿ywana 
// klasa NumberNode -> przechowuje informacje o wêŸle którego wartoœci¹ jest sta³a liczba
// klasa VariableNode -> przechowuje informacje o zmiennej, aby obliczyæ wartoœæ takiego wêz³a potrzebna jest mapa zmiennych/wartoœci
// klasa OperatorNode -> przechowuje operator (+,-,*,/) oraz wskaŸniki do dwóch podwêz³ów, wartoœci¹ takiego wêz³a jest wiêc operacja przeprowadzzona na podwêz³ach
// klasa FunctionNoe -> przechowuje informacje o funkcji (sinus lub cosinus) oraz o jednym podwêŸle, wartoœæ FunctionNode to zaaplikowanie odpowiedniej funkcji na wartoœæ podwêz³a
// z powy¿szego wynika, ¿e klasy OperatorNode i FunctionNode zespolaj¹ drzewo, tworzz¹c rozwidlenia, a klasy VariableNode i NumberNode s¹ "liœcmi", ostatnimi wêz³ami w drzewie

// z faktu ¿e, aby obliczyæ wartoœæ OperatorNode i FunctionNode, potrzebujemy znaæ pier wartoœæ ich "potomków" wartoœæ ca³ego drzewa jest obliczana w 
// sposób rekursywny pocz¹wszy od korzenia.


#pragma once
#include <string>
#include <map>

class MyNode
{
public:
	virtual double evaluate(std::map<std::string,double> variableMap);
	virtual std::string toString();
};

class VariableNode: public  MyNode{
private:
	std::string variable;
public:
	VariableNode(std::string var);
	~VariableNode();
	double evaluate(std::map<std::string, double> variableMap) override;
	std::string toString() override;

};

class NumberNode : public MyNode {
private:
	double number;
public:
	NumberNode(double num);
	~NumberNode();
	double evaluate(std::map<std::string, double> variableMap) override;
	std::string toString() override;

};

class OperatorNode : public MyNode {
private:
	char op;
	MyNode* left;
	MyNode* right;
public:
	OperatorNode(char op);
	~OperatorNode();
	MyNode* getLeftChild();
	MyNode* getRightChild();
	void setLeftChild(MyNode* childNode);
	void setRightChild(MyNode* childNode);
	double evaluate(std::map<std::string, double> variableMap) override;
	virtual std::string toString() override;

};

class FunctionNode : public MyNode {
private:
	std::string function;
	MyNode* child;
public:
	FunctionNode(std::string fun);
	~FunctionNode();
	MyNode* getChild();
	void setChild(MyNode* childNode);
	double evaluate(std::map<std::string, double> variableMap) override;
	virtual std::string toString() override;

};

const static std::string errorDivideByZero = "Can't divide by 0!";
const static char addSign = '+';
const static char subSign = '-';
const static char divSign = '/';
const static char mulSign = '*';
const static std::string funSin = "sin";
const static std::string funCos = "cos";
