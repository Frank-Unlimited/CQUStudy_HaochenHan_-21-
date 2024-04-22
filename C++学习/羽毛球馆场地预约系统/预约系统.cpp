#include<bits/stdc++.h>
#include"Identity.h"
#include"User.h"
#include"Manager.h"
#include"Boss.h"
using namespace std;

//管理员界面
void ManagerMenu(Identity*& manager) {
	Manager* man = (Manager*) manager;
	while(1) {
		cout<<"欢迎管理员："<<man->name<<"!"<<endl;
		man->operMenu();
		int cmd=0;
		cin>>cmd;
		if(cmd==1) {
			man->addPerson();
		} else if(cmd==2) {
			man->showPerson();
		} else if(cmd==3) {
			man->cleanPerson();
		} else if(cmd==0) {
			delete manager;
			cout<<"注销成功"<<endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//用户界面
void UserMenu(Identity*& user){
	User* us = (User*) user;
	while(1){
		cout<<"欢迎用户："<<us->name<<"!"<<endl;
		us->operMenu();
		int cmd;
		cin>>cmd;
		if(cmd==1){
			us->applyOrder();
		}
		else if(cmd==2){
			us->showMyOrder();
		}
		else if(cmd==3){
			us->showAllOrder();
		}
		else if(cmd==4){
			us->cancelOrder();
		}
		else if(cmd==5){
			us->payMoney();
		}
		else if(cmd==6){
			us->showMoney();
		}
		else if(cmd==0){
			delete user;
			cout<<"欢迎下次使用"<<endl;
			system("pause");
			system("cls");
			return;
		}
	}
}

//登录界面
void LoginIn(int type) {
	string fileName;
	switch(type) {
		case 1:
			fileName = "Boss.txt";
			break;
		case 2:
			fileName = "User.txt";
			break;
		case 3:
			fileName="Manager.txt";
			break;
	}
	cout<<"请输入id：";
	int id;
	cin>>id;
	cout<<endl;

	cout<<"请输入用户名：";
	string name;
	cin>>name;
	cout<<endl;

	cout<<"请输入密码：";
	string pwd;
	cin>>pwd;
	cout<<endl;

	//开始比对
	ifstream ifs(fileName,ios::in);
	int fId;
	string fName;
	string fPwd;
	while(ifs>>fId&&ifs>>fName&&ifs>>fPwd) {
		if(fId==id&&fName==name&&fPwd==pwd) {
			cout<<"验证成功！"<<endl;
			system("pause");
			system("cls");
			Identity* person=NULL;
			if(type==1) ;
			else if(type==2){
				person = new User(id,name,pwd);
				UserMenu(person);
			}
			else {
				person = new Manager(id,name,pwd);
				ManagerMenu(person);
			};
			return;
		}
	}
	cout<<"验证失败！"<<endl;
	system("pause");
	system("cls");
}

int main() {
	int cmd=0;
	while(1) {
		cout<<"============欢迎来到重庆大学羽毛球馆线上预约系统============"<<endl;
		cout<<endl;
		cout<<"\t\t------------------------------\t\t"<<endl;
		cout<<"\t\t|   1，球馆老板              |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   2，租借方                |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   3，管理员                |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   0，退出                  |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t------------------------------\t\t"<<endl;
		cout<<endl;
		cout<<"请选择您的身份：";
		cin>>cmd;
		if(cmd==1) {
			//老板
			LoginIn(1);
		} else if(cmd==2) {
			//租借方
			LoginIn(2);
		} else if(cmd==3) {
			//管理员
			LoginIn(3);
		} else if(cmd==0) {
			//退出
			cout<<"欢迎下次使用！"<<endl;
			system("pause");
			system("cls");
			return 0;
		} else {
			cout<<"输入错误，请重新输入"<<endl;
		}
	}
}
