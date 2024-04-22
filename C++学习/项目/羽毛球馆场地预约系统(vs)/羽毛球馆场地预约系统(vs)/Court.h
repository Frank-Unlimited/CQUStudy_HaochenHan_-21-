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
#include<iomanip>
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Court {
public:
	Court();
	void updateFile();
	void showCourt();
	int court_status[8][8][24];
};