#include<iostream>
#pragma once
using namespace std; 

//身份抽象基类
class Identity {
public:
	//操作菜单
	virtual void operMenu() = 0;//纯虚函数，子类必须重写该函数否则无法创建对象
	//用户名
	string m_Name;
	//密码
	string m_Pwd;
};
