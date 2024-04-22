#include<iostream>
#include<string>
#include<fstream>
#include"globalFile.h"
#include<vector>
#include"Student.h"
#include"Teacher.h"
#include<algorithm>
#pragma once
#include"Identity.h"
#include"computerRoom.h"
using namespace std;

class Manager :public Identity {
public:
	//Ĭ�Ϲ���
	Manager();
	//�вι���
	Manager(string name, string psw);
	//����˺�
	void addPerson();
	//���ԤԼ��¼
	void clearPerson();
	//�鿴������Ϣ
	void showComputer();
	//�鿴�˺�
	void showPerson();
	//�˵�����
	virtual void operMenu();
	//��ʼ������
	void initVector();
	//��ʼ������
	void initComputer();
	//����ظ�
	bool checkRepeat(int id/*���ѧ��/ְ����*/, int type/*�������*/);
	//ѧ������
	vector<Student> vStu;
	//��ʦ����
	vector<Teacher> vTea;
	//��������
	vector<ComputerRoom> vCom;
};
