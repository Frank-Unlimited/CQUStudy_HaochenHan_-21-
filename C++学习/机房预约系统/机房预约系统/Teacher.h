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
	//Ĭ�Ϲ���
	Teacher();
	//�вι���
	Teacher(int empId, string name, string psw);
	//�鿴����ԤԼ
	void showAllOrder();
	//�˵�����
	virtual void operMenu();
	//���ԤԼ
	void validOrder();
	//ְ����
	int empId;
};
