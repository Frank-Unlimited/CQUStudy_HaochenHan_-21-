#include<iostream>
#include"globalFile.h"
#include<fstream>
#include<map>
#include<string>
#pragma once
using namespace std;

class OrderFile {
public:
	//构造函数
	OrderFile();

	//更新预约记录
	void updateOrder();

	//记录预约条数
	int m_Size;

	//记录所有预约信息的容器
	map<int/*预约条数*/, map<string/*属性*/, string/*实值*/>> m_orderData;
};