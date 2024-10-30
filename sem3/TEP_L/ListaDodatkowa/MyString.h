#pragma once
#include <string>
class MyString
{
private:
	char* table;
	int length;
public:
	MyString();
	MyString(const MyString& other);
	~MyString();
	MyString& operator=(const MyString& other);
	MyString operator+(const MyString& other);
	MyString operator+(const char* c);
	MyString& operator=(const char *c);
	MyString& operator+=(const char* c);
	std::string ToStandard();
	operator bool();
};

