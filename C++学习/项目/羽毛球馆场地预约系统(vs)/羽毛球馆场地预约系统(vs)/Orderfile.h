#pragma once
#include<map>
#include<vector>
#include<iostream>
#include<fstream>
#include"Identity.h"
#include<iomanip>
using namespace std;

class Orderfile {
public:
	Orderfile();
	vector<map<string, string>> vOF;
	void showContent();
	void updateOrder();
	void initOrder();
	void showInf_All(vector<map<string,string>>::iterator it);
	void showInf_Part(vector<map<string, string>>::iterator it);
};
