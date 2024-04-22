#include "Identity.h"
#include<bits/stdc++.h>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Boss:public Identity{
	public:
		Boss();
		Boss(int id,string name,string pwd);
		void operMenu();
		void validOrder();
		void showAllOrder();
		void getMoney();
		int money;
};
