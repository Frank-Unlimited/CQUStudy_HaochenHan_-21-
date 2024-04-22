#include "Identity.h"
#include "User.h"
#include "Boss.h"
#include<bits/stdc++.h>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Manager:public Identity{
	public:
		Manager();
		Manager(int id, string name, string pwd);
		void addPerson();
		void showPerson();
		void cleanPerson();
		void updateFile();
		void updateVector();
		virtual void operMenu();
		vector<User> vUser;
		vector<Boss> vBoss;
		
};
