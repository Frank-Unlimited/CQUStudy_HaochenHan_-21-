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
#include"Identity.h"
#include"Orderfile.h"
#include"Court.h"
#include"User.h"
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Boss :public Identity {
public:
	Boss();
	Boss(int id, string name, string pwd);
	void operMenu();
	void validOrder();
	void showAllOrder();
	void getMoney();
	void cleanOrder();
	void showMoney();
	void showRecord();
	void changeInf();
	void chargeMoney();
	void withDraw();
	int money;
}; 
