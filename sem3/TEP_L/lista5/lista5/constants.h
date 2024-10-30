#pragma once
#include <string>

const static std::string errorDivideByZero = "Can't divide by 0!";
const static char addSign = '+';
const static char subSign = '-';
const static char divSign = '/';
const static char mulSign = '*';
const static std::string funSin = "sin";
const static std::string funCos = "cos";
const static char negativeSign = '-';
const static std::string slash = "/";
const static std::string space = " ";
const static char quote = '"';
const static std::string emptyString = "";

const static std::string defaultNumberNodeString = "a";
const static int defaultNumberNodeIntDouble = 1;


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
const static std::string comommand = "comp";
const static std::string varsCommand = "vars";
const static std::string printCommand = "print";
const static std::string enterCommand = "enter";
const static std::string joinCommand = "join";
const static std::string helommand = "?";

const static std::string avaliableCommandsMessage = "Avaliable commands:\n   " + enterCommand + " <formula>\n   " + comommand + " <var1> <var2> ...\n   " + varsCommand + "\n   " + joinCommand + " <formula>\n   " + printCommand + "\n   " + quitCommand;


