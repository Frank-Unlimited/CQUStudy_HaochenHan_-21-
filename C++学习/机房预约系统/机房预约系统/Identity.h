#include<iostream>
#pragma once
using namespace std; 

//��ݳ������
class Identity {
public:
	//�����˵�
	virtual void operMenu() = 0;//���麯�������������д�ú��������޷���������
	//�û���
	string m_Name;
	//����
	string m_Pwd;
};
