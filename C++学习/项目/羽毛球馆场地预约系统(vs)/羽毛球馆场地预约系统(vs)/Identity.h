#include<iostream>
#include<fstream>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
#include<map>
#include<vector>
#include<algorithm>
#include<deque>
#include<numeric>
#include<functional>
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Identity {
public:
	int id;
	string name;
	string pwd;
	virtual void operMenu() = 0;
};
