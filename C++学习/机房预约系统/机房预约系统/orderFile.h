#include<iostream>
#include"globalFile.h"
#include<fstream>
#include<map>
#include<string>
#pragma once
using namespace std;

class OrderFile {
public:
	//���캯��
	OrderFile();

	//����ԤԼ��¼
	void updateOrder();

	//��¼ԤԼ����
	int m_Size;

	//��¼����ԤԼ��Ϣ������
	map<int/*ԤԼ����*/, map<string/*����*/, string/*ʵֵ*/>> m_orderData;
};