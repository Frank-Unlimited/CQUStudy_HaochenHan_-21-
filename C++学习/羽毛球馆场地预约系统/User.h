#include<bits/stdc++.h>
#pragma once
#include"Identity.h"
#include"Court.h"
using namespace std;

class User:public Identity{
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
		int money;
};


