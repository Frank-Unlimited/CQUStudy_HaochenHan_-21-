#include<iostream>
#include<fstream>
#pragma once
#define FOR(n) for(int i=0;i<n;i++)
#include<map>
#include<vector>
#include<algorithm>
#include<deque>
#include<numeric>
#include<functional>
#include"Identity.h"
#include"User.h"
#include"Manager.h"
#include"Boss.h"
using namespace std;

//管理员界面
void ManagerMenu(Identity*& manager) {
	Manager* man = (Manager*)manager;
	while (1) {
		cout << "欢迎管理员：" << man->name << "!" << endl;
		man->operMenu();
		int cmd = 0;
		cin >> cmd;
		if (cmd == 1) {
			man->addPerson();
		}
		else if (cmd == 2) {
			man->showPerson();
		}
		else if (cmd == 3) {
			man->cleanPerson();
		}
		else if (cmd == 0) {
			delete manager;
			cout << "注销成功" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "输入错误，请重新输入" << endl;
		}
	}
}
//用户界面
void UserMenu(Identity*& user) {
	User* us = (User*)user;
	while (1) {
		cout << "欢迎用户：" << us->name << "!" << endl;
		us->operMenu();
		int cmd;
		cin >> cmd;
		if (cmd == 1) {
			us->applyOrder();
		}
		else if (cmd == 2) {
			us->showMyOrder();
		}
		else if (cmd == 3) {
			us->showAllOrder();
		}
		else if (cmd == 4) {
			us->cancelOrder();
		}
		else if (cmd == 5) {
			us->payMoney();
		}
		else if (cmd == 6) {
			system("cls");
			while (1) {
				cout << "==账户管理==" << endl;
				cout << "1，查询账户余额" << endl;
				cout << "2，充值" << endl;
				cout << "3，提现" << endl;
				cout << "4，查看交易记录" << endl;
				cout << "5，修改账户信息" << endl;
				cout << "0，返回" << endl;
				cout << endl << "请输入选择：";
				int cmd2;
				cin >> cmd2;
				if (cmd2 == 1) {
					us->showMoney();
					system("pause");
					system("cls");
				}
				else if (cmd2 == 2) {
					us->chargeMoney();
				}
				else if (cmd2 == 3) {
					us->withDraw();
				}
				else if (cmd2 == 4) {
					us->showRecord();
				}
				else if (cmd2 == 5) {
					us->changeInf();
				}
				else {
					system("cls");
					break;
				};
			}
		}
		else if (cmd == 0) {
			delete user;
			cout << "欢迎下次使用" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "输入错误，请重新输入" << endl;
		}
	}
}

//老板界面
void BossMenu(Identity*& boss) {
	Boss* bo = (Boss*)boss;
	while (1) {
		cout << "欢迎老板：" << bo->name << "!" << endl;
		bo->operMenu();
		int cmd;
		cin >> cmd;
		if (cmd == 1) {
			bo->validOrder();
		}
		else if (cmd == 2) {
			bo->showAllOrder();
		}
		else if (cmd == 3) {
			bo->cleanOrder();
		}
		else if (cmd == 4) {
			system("cls");
			while (1) {
				cout << "==账户管理==" << endl;
				cout << "1，查看账户余额" << endl;
				cout << "2，查看交易记录" << endl;
				cout << "3，充值" << endl;
				cout << "4，提现" << endl;
				cout << "5，修改账户信息" << endl;
				cout << "0，返回" << endl;
				cout << endl << "请输入选择：";
				int cmd2;
				cin >> cmd2;
				if (cmd2 == 1) {
					bo->showMoney();
					system("pause");
					system("cls");
				}
				else if (cmd2 == 2) {
					bo->showRecord();
				}
				else if (cmd2 == 3) {
					bo->chargeMoney();
				}
				else if(cmd2==4) {
					bo->withDraw();
				}
				else if (cmd2 == 5) {
					bo->changeInf();
				}
				else {
					system("cls");
					break;
				}
			}
		}
		else if (cmd == 0) {
			delete boss;
			cout << "欢迎下次使用" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "输入错误，请重新输入" << endl;
		}
	}
}

//登录界面
void LoginIn(int type) {
	string fileName;
	switch (type) {
	case 1:
		fileName = "Boss.txt";
		break;
	case 2:
		fileName = "User.txt";
		break;
	case 3:
		cout << "请输入编写程序者姓名：" << endl;
		string HHC;
		cin >> HHC;
		if (HHC != "韩昊辰") {
			cout << "错误" << endl;
			system("pause");
			system("cls");
			return;
		}
		Identity* person = NULL;
		person = new Manager(1, "韩昊辰", "1");
		system("cls");
		ManagerMenu(person);
		return;
		break;
	}
	cout << "请输入id：";
	int id;
	cin >> id;
	cout << endl;

	cout << "请输入用户名：";
	string name;
	cin >> name;
	cout << endl;

	cout << "请输入密码：";
	string pwd;
	cin >> pwd;
	cout << endl;

	//开始比对
	ifstream ifs(fileName, ios::in);
	int fId=0;
	string fName="";
	string fPwd="";
	while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
		if (fId == id && fName == name && fPwd == pwd) {
			cout << "验证成功！" << endl;
			system("pause");
			system("cls");
			Identity* person = NULL;
			if (type == 1) {
				person = new Boss(id, name, pwd);
				BossMenu(person);
			}
			else if (type == 2) {
				person = new User(id, name, pwd);
				UserMenu(person);
			}
			else {
				person = new Manager(id, name, pwd);
				ManagerMenu(person);
			};
			return;
		}
	}
	cout << "验证失败！" << endl;
	system("pause");
	system("cls");
}

int main() {
	int cmd = 0;
	while (1) {
		cout << "============欢迎来到重庆大学羽毛球馆线上预约系统============" << endl;
		cout << endl;
		cout << "\t\t------------------------------\t\t" << endl;
		cout << "\t\t|   1，球馆老板登录          |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   2，租借方登录            |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   3，管理员登录            |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   4，查看预约规则          |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   0，退出系统              |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t------------------------------\t\t" << endl;
		cout << endl;
		cout << "请输入选项：";
		cin >> cmd;
		if (cmd == 1) {
			//老板
			LoginIn(1);
		}
		else if (cmd == 2) {
			//租借方
			LoginIn(2);
		}
		else if (cmd == 3) {
			//管理员
			LoginIn(3);
		}
		else if (cmd == 4){
			system("cls");
			cout << "预约规则：" << endl;
			cout << "重庆大学羽毛球馆支持周一至周日全天24小时预订场地，用户在该系统预定球馆时" << endl;
			cout << "需要确定日期、时间和场地编号，预约成功后等待球馆老板审核，球馆老板可选择" << endl;
			cout << "通过与不通过，审核通过的预约会显示未付款状态，等待用户付款，用户付款后场" << endl;
			cout << "场地才算预定成功，用户可取消审核中和未付款的预约" << endl;
			cout << "球馆价格：" << endl;
			cout << "0点—12点：15元/小时" << endl;
			cout << "12点—18点：40元/小时" << endl;
			cout << "18点—24点：60元/小时" << endl;
			system("pause");
			system("cls");
		}
		else if (cmd == 0) {
			//退出
			cout << "欢迎下次使用！" << endl;
			system("pause");
			system("cls");
			return 0;
		}
		else {
			cout << "输入错误，请重新输入" << endl;
		}
	}
}