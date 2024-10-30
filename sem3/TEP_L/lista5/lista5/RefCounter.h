// licznik RefCounter uzywany do zliczania kopii smart pointer

#pragma once
class RefCounter
{
public:
	RefCounter() { count=0; }
	int Add() { return(++count); }
	int Dec() { return(--count); };
	int Get() { return(count); }
private:
	int count;
};//class RefCounter
