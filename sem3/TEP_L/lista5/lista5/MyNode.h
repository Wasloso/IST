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
#include "constants.h"
using namespace std;


template <class T>
class MyNode
{
public:
	virtual T evaluate(std::map<std::string, T> variableMap);
	virtual std::string toString();
};

template <class T>
class VariableNode : public  MyNode<T> {
private:
	std::string variable;
public:
	VariableNode(std::string var);
	~VariableNode();
	T evaluate(std::map<std::string, T> variableMap) override;
	std::string toString() override;

};

template <class T>
class NumberNode : public MyNode<T> {
private:
	T number;
public:
	NumberNode(T num);
	~NumberNode();
	T evaluate(std::map<std::string, T> variableMap) override;
	std::string toString() override;

};

template <class T>
class OperatorNode : public MyNode<T> {
private:
	char op;
	MyNode<T>* left;
	MyNode<T>* right;
public:
	OperatorNode(char op);
	~OperatorNode();
	MyNode<T>* getLeftChild();
	MyNode<T>* getRightChild();
	void setLeftChild(MyNode<T>* childNode);
	void setRightChild(MyNode<T>* childNode);
	T evaluate(std::map<std::string, T> variableMap) override;
	std::string toString() override;

};

template <class T>
class FunctionNode : public MyNode<T> {
private:
	std::string function;
	MyNode<T>* child;
public:
	FunctionNode(std::string fun);
	~FunctionNode();
	MyNode<T>* getChild();
	void setChild(MyNode<T>* childNode);
	T evaluate(std::map<std::string, T> variableMap) override;
	std::string toString() override;

};

//implementacja

template <class T>
T MyNode<T>::evaluate(std::map<std::string, T> variableMap)
{
	return T();
}

template <class T>
std::string MyNode<T>::toString()
{
	return "";
}

// node ze zmienna
template <class T>
VariableNode<T>::VariableNode(std::string var)
{
	variable = var;
}

template <class T>
VariableNode<T>::~VariableNode() {};

// do obliczenia wartosci w�z�a ze zmienna potrzebna jest mapa, z niej odczytywana jest warto�� zmiennej
template <class T>
T VariableNode<T>::evaluate(std::map<std::string, T> variableMap)
{
	return variableMap[variable];
}

template <>
string VariableNode<string>::evaluate(map<string, string> variableMap) {
	string val = variableMap[variable];
	return val.substr(1, val.length() - 2);
}

template <class T>
std::string VariableNode<T>::toString()
{
	return variable;
}

// node z liczb�
template <class T>
NumberNode<T>::NumberNode(T num)
{
	number = num;
}

template <class T>
NumberNode<T>::~NumberNode() {};

template <class T>
T NumberNode<T>::evaluate(std::map<std::string, T> variableMap)
{
	return number;
}

template <class T>
std::string NumberNode<T>::toString()
{
	return std::to_string(number);
}

template <>
std::string NumberNode<std::string>::toString() {
	return quote + number + quote;
}

// node z operatorem - ma dwojke dzieci
template <class T>
OperatorNode<T>::OperatorNode(char op)
{
	this->op = op;
	left = NULL;
	right = NULL;
}

template <class T>
OperatorNode<T>::~OperatorNode()
{
	delete[] left;
	delete[] right;
}

template <class T>
MyNode<T>* OperatorNode<T>::getLeftChild()
{
	return left;
}

template <class T>
MyNode<T>* OperatorNode<T>::getRightChild()
{
	return right;
}

template <class T>
void OperatorNode<T>::setLeftChild(MyNode<T>* childNode)
{
	if (childNode == NULL)
	{
		left = new NumberNode<T>(T());
		return;
	}
	left = childNode;
}

template <class T>
void OperatorNode<T>::setRightChild(MyNode<T>* childNode)
{
	if (childNode == NULL)
	{
		right = new NumberNode<T>(T());
		return;
	}
	right = childNode;
}

// obliczenie wartosci operatorNode poprzez obliczenie wartosci jego dziecci i wykonanie odpowiedniego dzialania
template <class T>
T OperatorNode<T>::evaluate(std::map<std::string, T> variableMap)
{
	T leftVal = left->evaluate(variableMap);
	T rightVal = right->evaluate(variableMap);

	switch (op)
	{
	case addSign: return leftVal + rightVal;
	case subSign: return leftVal - rightVal;
	case mulSign: return leftVal * rightVal;
	case divSign: {
		if (rightVal == 0)
		{
			return INFINITY;
		}
		return leftVal / rightVal;
	}
	default: return 0;
	};
}

// ewaluacja drzewa dla typu string
template <>
string OperatorNode<string>::evaluate(std::map<std::string, string> variableMap) {
	string leftVal = left->evaluate(variableMap);
	string rightVal = right->evaluate(variableMap);

	switch (op)
	{
	case addSign: return leftVal + rightVal;
	case subSign: {
		int pos = leftVal.rfind(rightVal);
		if (pos != -1) return leftVal.substr(0, pos) + leftVal.substr(pos + rightVal.length());
		else return leftVal;
	}
	case mulSign: {
		int pos = leftVal.find(rightVal[0]);
		while (pos != -1) {
			leftVal = leftVal.replace(pos, 1, rightVal);
			pos = leftVal.find(rightVal[0], pos + rightVal.length());
		}
		return leftVal;
	}
	case divSign: {
		int pos = leftVal.find(rightVal);
		while (pos != -1) {
			leftVal = leftVal.replace(pos, rightVal.length(), rightVal.substr(0, 1));
			pos = leftVal.find(rightVal, pos + 1);
		}
		return leftVal;
	}
	default: return leftVal;
	};
}


template <class T>
std::string OperatorNode<T>::toString()
{
	std::string leftChildString = left->toString();
	std::string rightChildString = right->toString();
	return std::string(1, op) + space + leftChildString + space + rightChildString;
}

// node z funckj� - ma jednego potomka
template <class T>
FunctionNode<T>::FunctionNode(std::string fun)
{
	function = fun;
	child = NULL;
}
template class MyNode<int>;

template <class T>
FunctionNode<T>::~FunctionNode()
{
	delete[] child;
}

template <class T>
MyNode<T>* FunctionNode<T>::getChild()
{
	return child;
}

template <class T>
void FunctionNode<T>::setChild(MyNode<T>* childNode)
{
	if (childNode == NULL)
	{
		childNode = new NumberNode<T>(T());
		return;
	}
	child = childNode;
}

// obliczenie wartosci dziecka i nalozenie odpowiedniej funkcji
template <class T>
T FunctionNode<T>::evaluate(std::map<std::string, T> variableMap)
{
	T childValue = child->evaluate(variableMap);
	// zastosowanie odpowiedniej funkcji 
	if (function == funSin) return sin(childValue);
	if (function == funCos) return cos(childValue);

	return T();
}

template <class T>
std::string FunctionNode<T>::toString()
{
	std::string childString = child->toString();
	return function + space + childString;
}

// drzewo <string> nie posiada funkkcyjnych w�z��w, ale trzeba je zaimplementowac zeby nie bylo bledow ompilatora
template <>
string FunctionNode<string>::evaluate(std::map<std::string, string> variableMap)
{
	return emptyString;
}








