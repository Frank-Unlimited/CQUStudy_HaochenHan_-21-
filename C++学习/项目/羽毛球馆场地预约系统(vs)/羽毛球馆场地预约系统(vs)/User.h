#include<iostream>
#include<fstream>
#include"Orderfile.h"
#include <stdlib.h>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
#include<map>
#include<vector>
#include<algorithm>
#include<deque>
#include<numeric>
#include<functional>
#include<iomanip>
#include"Identity.h"
#include<string>
#include"Boss.h"
//#include"Manager.h"
using namespace std;

class User :public Identity {
public:
	User();
	User(int id, string name, string pwd);
	virtual void operMenu();
	void applyOrder();
	void showMyOrder();
	void showAllOrder();
	void cancelOrder();
	void payMoney();
	void showMoney();
	void showRecord();
	void chargeMoney();
	void withDraw();
	void changeInf();
	int money;
};

