#pragma once
#include <string>

class MyTable {
public:
    MyTable();
    MyTable(std::string name, int length);
    MyTable(MyTable& pcOther);
    ~MyTable();

    void setName(std::string name);
    bool setNewSize(int length);
    MyTable* clone();
    void printState();

private:
    void init(std::string name, int length);
    std::string tableName;
    int* tablePointer;
    int tableLength;

};
const std::string defaultName = "default name";
const int defaultLength = 5;
const std::string copyAddnotation = "_copy";
