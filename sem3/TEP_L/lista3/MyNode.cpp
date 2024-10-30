#include "MyNode.h"
#include <map>
#include <iostream>


double MyNode::evaluate(std::map<std::string, double> variableMap)
{
	return 0.0;
}

std::string MyNode::toString()
{
	return "";
}

// node ze zmienna
VariableNode::VariableNode(std::string var)
{
	variable = var;
}

VariableNode::~VariableNode() {};

// do obliczenia wartosci wêz³a ze zmienna potrzebna jest mapa, z niej odczytywana jest wartoœæ zmiennej
double VariableNode::evaluate(std::map<std::string, double> variableMap)
{
	return variableMap[variable];
}

std::string VariableNode::toString()
{
	return variable;
}

// node z liczb¹
NumberNode::NumberNode(double num)
{
	number = num;
}

NumberNode::~NumberNode() {};

double NumberNode::evaluate(std::map<std::string, double> variableMap)
{
	return number;
}

std::string NumberNode::toString()
{
	return std::to_string(number);
}

// node z operatorem - ma dwojke dzieci
OperatorNode::OperatorNode(char op)
{
	this->op = op;
	left = NULL;
	right = NULL;
}

OperatorNode::~OperatorNode()
{
	delete[] left;
	delete[] right;
}

MyNode* OperatorNode::getLeftChild()
{
	return left;
}

MyNode* OperatorNode::getRightChild()
{
	return right;
}

void OperatorNode::setLeftChild(MyNode* childNode)
{
	if (childNode == NULL)
	{
		left = new NumberNode(1);
		return;
	}
	left = childNode;
}

void OperatorNode::setRightChild(MyNode* childNode)
{
	if (childNode == NULL)
	{
		right = new NumberNode(1);
		return;
	}
	right = childNode;
}

// obliczenie wartosci operatorNode poprzez obliczenie wartosci jego dziecci i wykonanie odpowiedniego dzialania
double OperatorNode::evaluate(std::map<std::string, double> variableMap)
{
	double leftVal = left->evaluate(variableMap);
	double rightVal = right->evaluate(variableMap);

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

std::string OperatorNode::toString()
{
	std::string leftChildString = left->toString();
	std::string rightChildString = right->toString();
	return std::string(1, op) + " " + leftChildString + " " + rightChildString;
}

// node z funckj¹ - ma jedno dziecko
FunctionNode::FunctionNode(std::string fun)
{
	function = fun;
	child = NULL;
}

FunctionNode::~FunctionNode()
{
	delete[] child;
}

MyNode* FunctionNode::getChild()
{
	return child;
}

void FunctionNode::setChild(MyNode* childNode)
{
	if (childNode == NULL) 
	{
		childNode = new NumberNode(1);
		return;
	}
	child = childNode;
}

// obliczenie wartosci dziecka i nalozenie odpowiedniej funkcji
double FunctionNode::evaluate(std::map<std::string, double> variableMap)
{
	double childValue = child->evaluate(variableMap);
	// zastosowanie odpowiedniej funkcji 
	if (function == funSin) return sin(childValue);
	if (function == funCos) return cos(childValue);

	return 0;
}

std::string FunctionNode::toString()
{
	std::string childString = child->toString();
	return function + " " + childString;
}
