#include"Identity.h"
#include<iostream>
#include<vector>
#include"computerRoom.h"
#include<fstream>
#include"globalFile.h"
#include<string>
#include"orderFile.h"
#include<iomanip>
#pragma once
using namespace std;

class Student :public Identity {
public:
	//Ĭ�Ϲ���
	Student();
	//�вι���
	Student(int id,string name,string pwd);
	//�˵�����
	virtual void operMenu();
	//����ԤԼ
	void applyOrder();
	//�鿴����ԤԼ
	void showMyOrder();
	//�鿴����ԤԼ
	void showAllOrder();
	//ȡ��ԤԼ
	void cancelOrder();
	//ѧ��
	int m_Id;
	//��������
	vector<ComputerRoom> vCom;
};
