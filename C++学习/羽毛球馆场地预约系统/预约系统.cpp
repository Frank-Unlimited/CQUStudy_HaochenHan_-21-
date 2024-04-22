#include<bits/stdc++.h>
#include"Identity.h"
#include"User.h"
#include"Manager.h"
#include"Boss.h"
using namespace std;

//����Ա����
void ManagerMenu(Identity*& manager) {
	Manager* man = (Manager*) manager;
	while(1) {
		cout<<"��ӭ����Ա��"<<man->name<<"!"<<endl;
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
			cout<<"ע���ɹ�"<<endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//�û�����
void UserMenu(Identity*& user){
	User* us = (User*) user;
	while(1){
		cout<<"��ӭ�û���"<<us->name<<"!"<<endl;
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
			cout<<"��ӭ�´�ʹ��"<<endl;
			system("pause");
			system("cls");
			return;
		}
	}
}

//��¼����
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
	cout<<"������id��";
	int id;
	cin>>id;
	cout<<endl;

	cout<<"�������û�����";
	string name;
	cin>>name;
	cout<<endl;

	cout<<"���������룺";
	string pwd;
	cin>>pwd;
	cout<<endl;

	//��ʼ�ȶ�
	ifstream ifs(fileName,ios::in);
	int fId;
	string fName;
	string fPwd;
	while(ifs>>fId&&ifs>>fName&&ifs>>fPwd) {
		if(fId==id&&fName==name&&fPwd==pwd) {
			cout<<"��֤�ɹ���"<<endl;
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
	cout<<"��֤ʧ�ܣ�"<<endl;
	system("pause");
	system("cls");
}

int main() {
	int cmd=0;
	while(1) {
		cout<<"============��ӭ���������ѧ��ë�������ԤԼϵͳ============"<<endl;
		cout<<endl;
		cout<<"\t\t------------------------------\t\t"<<endl;
		cout<<"\t\t|   1������ϰ�              |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   2����跽                |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   3������Ա                |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t|   0���˳�                  |\t\t"<<endl;
		cout<<"\t\t|                            |\t\t"<<endl;
		cout<<"\t\t------------------------------\t\t"<<endl;
		cout<<endl;
		cout<<"��ѡ��������ݣ�";
		cin>>cmd;
		if(cmd==1) {
			//�ϰ�
			LoginIn(1);
		} else if(cmd==2) {
			//��跽
			LoginIn(2);
		} else if(cmd==3) {
			//����Ա
			LoginIn(3);
		} else if(cmd==0) {
			//�˳�
			cout<<"��ӭ�´�ʹ�ã�"<<endl;
			system("pause");
			system("cls");
			return 0;
		} else {
			cout<<"�����������������"<<endl;
		}
	}
}
