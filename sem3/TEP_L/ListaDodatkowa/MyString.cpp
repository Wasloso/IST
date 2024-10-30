#include "MyString.h"
#include <iostream>

MyString::MyString() : table(nullptr), length(0) {}


MyString::MyString(const MyString& other) : table(nullptr), length(other.length)
{
	if (length > 0) {
		table = new char[length];
		for (int i = 0; i < length; i++) {
			table[i] = other.table[i];
		}
	}
}

MyString::~MyString()
{
	delete[] table;
}

MyString& MyString::operator=(const MyString& other)
{
	if (this != &other) {
		delete[] table;
		length = other.length;
		if (length > 0) {
			table = new char[length];
			for (int i = 0; i < length; i++) {
				table[i] = other.table[i];
			}
		}
	}
	return *this;
}

MyString MyString::operator+(const MyString& other)
{
	MyString result;
	result.length = length + other.length;
	if (result.length > 0) {
		result.table = new char[result.length];
		int i = 0;
		for (int j = 0; j < length; j++) {
			result.table[i++] = table[j];
		}
		for (int j = 0; j < other.length; j++) {
			result.table[i++] = other.table[j];
		}
	}
	return result;
}

MyString MyString::operator+(const char* c) {
	MyString result;
	result.length = length + strlen(c);
	if (result.length > 0) {
		result.table = new char[result.length];
		int i = 0;
		for (int j = 0; j < length; j++) {
			result.table[i++] = table[j];
		}
		for (int j = 0; j < strlen(c); j++) {
			result.table[i++] = c[j];
		}
	}
	return result;
}

MyString& MyString::operator=(const char* c)
{
	if (length > 0) {
		delete[] table;
	}
	length = strlen(c);
	table = new char[length];
	for (int i = 0; i < length; i++) {
		table[i] = c[i];
	}
	return *this;
}

MyString& MyString::operator+=(const char* c)
{
	*this = *this + c;
	return *this;
}

std::string MyString::ToStandard()
{
	return (this) ? std::string(table, length) : std::string();
}

MyString::operator bool()
{
	return length > 0;
}
