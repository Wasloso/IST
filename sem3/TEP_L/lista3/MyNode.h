// plik przechowuje imformacje o w�z�ach drzewa
// w�z�y zosta�y podzielone na 4 kategorie wykorzystuj�c w�a�ciwo�ci dziediczenia
// klasa MyNode jest abstrakcyjn� klas� i nie jest u�ywana 
// klasa NumberNode -> przechowuje informacje o w�le kt�rego warto�ci� jest sta�a liczba
// klasa VariableNode -> przechowuje informacje o zmiennej, aby obliczy� warto�� takiego w�z�a potrzebna jest mapa zmiennych/warto�ci
// klasa OperatorNode -> przechowuje operator (+,-,*,/) oraz wska�niki do dw�ch podw�z��w, warto�ci� takiego w�z�a jest wi�c operacja przeprowadzzona na podw�z�ach
// klasa FunctionNoe -> przechowuje informacje o funkcji (sinus lub cosinus) oraz o jednym podw�le, warto�� FunctionNode to zaaplikowanie odpowiedniej funkcji na warto�� podw�z�a
// z powy�szego wynika, �e klasy OperatorNode i FunctionNode zespolaj� drzewo, tworzz�c rozwidlenia, a klasy VariableNode i NumberNode s� "li�cmi", ostatnimi w�z�ami w drzewie

// z faktu �e, aby obliczy� warto�� OperatorNode i FunctionNode, potrzebujemy zna� pier warto�� ich "potomk�w" warto�� ca�ego drzewa jest obliczana w 
// spos�b rekursywny pocz�wszy od korzenia.


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
