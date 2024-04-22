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

//����Ա����
void ManagerMenu(Identity*& manager) {
	Manager* man = (Manager*)manager;
	while (1) {
		cout << "��ӭ����Ա��" << man->name << "!" << endl;
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
			cout << "ע���ɹ�" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "�����������������" << endl;
		}
	}
}
//�û�����
void UserMenu(Identity*& user) {
	User* us = (User*)user;
	while (1) {
		cout << "��ӭ�û���" << us->name << "!" << endl;
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
				cout << "==�˻�����==" << endl;
				cout << "1����ѯ�˻����" << endl;
				cout << "2����ֵ" << endl;
				cout << "3������" << endl;
				cout << "4���鿴���׼�¼" << endl;
				cout << "5���޸��˻���Ϣ" << endl;
				cout << "0������" << endl;
				cout << endl << "������ѡ��";
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
			cout << "��ӭ�´�ʹ��" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "�����������������" << endl;
		}
	}
}

//�ϰ����
void BossMenu(Identity*& boss) {
	Boss* bo = (Boss*)boss;
	while (1) {
		cout << "��ӭ�ϰ壺" << bo->name << "!" << endl;
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
				cout << "==�˻�����==" << endl;
				cout << "1���鿴�˻����" << endl;
				cout << "2���鿴���׼�¼" << endl;
				cout << "3����ֵ" << endl;
				cout << "4������" << endl;
				cout << "5���޸��˻���Ϣ" << endl;
				cout << "0������" << endl;
				cout << endl << "������ѡ��";
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
			cout << "��ӭ�´�ʹ��" << endl;
			system("pause");
			system("cls");
			return;
		}
		else {
			cout << "�����������������" << endl;
		}
	}
}

//��¼����
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
		cout << "�������д������������" << endl;
		string HHC;
		cin >> HHC;
		if (HHC != "��껳�") {
			cout << "����" << endl;
			system("pause");
			system("cls");
			return;
		}
		Identity* person = NULL;
		person = new Manager(1, "��껳�", "1");
		system("cls");
		ManagerMenu(person);
		return;
		break;
	}
	cout << "������id��";
	int id;
	cin >> id;
	cout << endl;

	cout << "�������û�����";
	string name;
	cin >> name;
	cout << endl;

	cout << "���������룺";
	string pwd;
	cin >> pwd;
	cout << endl;

	//��ʼ�ȶ�
	ifstream ifs(fileName, ios::in);
	int fId=0;
	string fName="";
	string fPwd="";
	while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
		if (fId == id && fName == name && fPwd == pwd) {
			cout << "��֤�ɹ���" << endl;
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
	cout << "��֤ʧ�ܣ�" << endl;
	system("pause");
	system("cls");
}

int main() {
	int cmd = 0;
	while (1) {
		cout << "============��ӭ���������ѧ��ë�������ԤԼϵͳ============" << endl;
		cout << endl;
		cout << "\t\t------------------------------\t\t" << endl;
		cout << "\t\t|   1������ϰ��¼          |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   2����跽��¼            |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   3������Ա��¼            |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   4���鿴ԤԼ����          |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t|   0���˳�ϵͳ              |\t\t" << endl;
		cout << "\t\t|                            |\t\t" << endl;
		cout << "\t\t------------------------------\t\t" << endl;
		cout << endl;
		cout << "������ѡ�";
		cin >> cmd;
		if (cmd == 1) {
			//�ϰ�
			LoginIn(1);
		}
		else if (cmd == 2) {
			//��跽
			LoginIn(2);
		}
		else if (cmd == 3) {
			//����Ա
			LoginIn(3);
		}
		else if (cmd == 4){
			system("cls");
			cout << "ԤԼ����" << endl;
			cout << "�����ѧ��ë���֧����һ������ȫ��24СʱԤ�����أ��û��ڸ�ϵͳԤ�����ʱ" << endl;
			cout << "��Ҫȷ�����ڡ�ʱ��ͳ��ر�ţ�ԤԼ�ɹ���ȴ�����ϰ���ˣ�����ϰ��ѡ��" << endl;
			cout << "ͨ���벻ͨ�������ͨ����ԤԼ����ʾδ����״̬���ȴ��û�����û������" << endl;
			cout << "���ز���Ԥ���ɹ����û���ȡ������к�δ�����ԤԼ" << endl;
			cout << "��ݼ۸�" << endl;
			cout << "0�㡪12�㣺15Ԫ/Сʱ" << endl;
			cout << "12�㡪18�㣺40Ԫ/Сʱ" << endl;
			cout << "18�㡪24�㣺60Ԫ/Сʱ" << endl;
			system("pause");
			system("cls");
		}
		else if (cmd == 0) {
			//�˳�
			cout << "��ӭ�´�ʹ�ã�" << endl;
			system("pause");
			system("cls");
			return 0;
		}
		else {
			cout << "�����������������" << endl;
		}
	}
}