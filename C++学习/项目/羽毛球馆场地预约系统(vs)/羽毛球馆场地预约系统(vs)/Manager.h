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
#include"Identity.h"
#include"User.h"
#include"Boss.h"
#include<Windows.h>
#define FOR(n) for(int i=0;i<n;i++)
using namespace std;

class Manager :public Identity {
public:
	Manager();
	Manager(int id, string name, string pwd);
	void addPerson();
	void showPerson();
	void cleanPerson();
	void updateFile();//ͨ��vectorд�ļ�
	void updateVector();//ͨ���ļ�дvector
	virtual void operMenu();
	vector<User> vUser;
	vector<Boss> vBoss;

}; 
