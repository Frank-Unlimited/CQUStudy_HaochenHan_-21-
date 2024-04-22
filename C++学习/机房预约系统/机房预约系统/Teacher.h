#include<iostream>
#pragma once
#include<string>
#include"globalFile.h"
#include<iomanip>
#include"orderFile.h"
#include"Identity.h"
#include<vector>
using namespace std;

class Teacher :public Identity {
public:
	//默认构造
	Teacher();
	//有参构造
	Teacher(int empId, string name, string psw);
	//查看所有预约
	void showAllOrder();
	//菜单界面
	virtual void operMenu();
	//审核预约
	void validOrder();
	//职工号
	int empId;
};
