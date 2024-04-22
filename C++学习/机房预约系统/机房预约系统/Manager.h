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
	//默认构造
	Manager();
	//有参构造
	Manager(string name, string psw);
	//添加账号
	void addPerson();
	//清空预约记录
	void clearPerson();
	//查看机房信息
	void showComputer();
	//查看账号
	void showPerson();
	//菜单界面
	virtual void operMenu();
	//初始化容器
	void initVector();
	//初始化机房
	void initComputer();
	//检测重复
	bool checkRepeat(int id/*检测学号/职工号*/, int type/*检测类型*/);
	//学生容器
	vector<Student> vStu;
	//老师容器
	vector<Teacher> vTea;
	//机房容器
	vector<ComputerRoom> vCom;
};
