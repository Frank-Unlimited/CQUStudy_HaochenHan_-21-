#include<iostream>
#include<string>
#include<fstream>
#include"Student.h"
#include"Teacher.h"
#include"Manager.h"
#include"Identity.h"
#include"globalFile.h"
using namespace std;
//�������Ա�Ӳ˵�����
void managerMenu(Identity*& manager) {
	while (1) {
		//���ù���Ա�Ӳ˵�
		manager->operMenu();
		//������ָ��תΪ����ָ�룬���Ե��������������ӿ�
		Manager* man = (Manager*)manager;

		int cmd = 0;
		cin >> cmd;
		if (cmd == 1) {//����˺�
			man->addPerson();
		}
		else if (cmd == 2) {//�鿴�˺�
			man->showPerson();
		}
		else if (cmd == 3) {//�鿴������Ϣ
			man->showComputer();
		}
		else if (cmd == 4) {//���ԤԼ
			man->clearPerson();
		}
		else {//ע����¼
			delete manager;
			cout << "ע���ɹ���" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//����ѧ���Ӳ˵�����
void studentMenu(Identity*& student) {
	while (1) {
		//�����Ӳ˵�����
		student->operMenu();
		//����ָ��ת������ָ��
		Student* stu = (Student*)student;
		
		int cmd = 0;
		cin >> cmd;

		if (cmd == 1) {
			//����ԤԼ
			stu->applyOrder();
		}
		else if (cmd == 2) {
			//�鿴�ҵ�ԤԼ
			stu->showMyOrder();
		}
		else if (cmd == 3) {
			//�鿴����ԤԼ
			stu->showAllOrder();
		}
		else if (cmd == 4) {
			//ȡ��ԤԼ
			stu->cancelOrder();
		}
		else {
			//ע����¼
			delete student;
			cout << "ע���ɹ���" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//�����ʦ�Ӳ˵�����
void teacherMenu(Identity*& teacher) {
	while (1) {
		//�����Ӳ˵�����
		teacher->operMenu();
		//����ָ��ת������ָ��
		Teacher* tea = (Teacher*)teacher;

		int cmd = 0;
		cin >> cmd;

		if (cmd == 1) {
			//�鿴����ԤԼ
			tea->showAllOrder();
		}
		else if (cmd == 2) {
			//���ԤԼ
			tea->validOrder();
		}
		else {
			//ע����¼
			delete teacher;
			cout << "ע���ɹ���" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//��¼����
void LoginIn(string fileName/*�����ļ���*/, int type/*�����������*/) {
	Identity* person = NULL;//����ָ��ָ���������
	
	//���ļ�
	ifstream ifs;
	ifs.open(fileName, ios::in);//�ļ��Ѵ�
	if (!ifs.is_open()) {
		cout << "�ļ�������" << endl;
		ifs.close();
		return;
	}

	//׼�������û���Ϣ
	int id = 0;
	string name;
	string pwd;

	//�ж����
	if (type == 1) {//ѧ�����
		cout << "������ѧ�ţ�" << endl;
		cin >> id;
	}
	else if (type == 2) {//��ʦ���
		cout << "������ְ���ţ�" << endl;
		cin >> id;
	}

	cout << "�������û�����" << endl;
	cin >> name;
	cout << "���������룺" << endl;
	cin >> pwd;

	//�����֤����֤id��name��pwd����ȷ�ԣ�
	if (type == 1) {
		//ѧ�������֤
		int fId;
		string fName;
		string fPwd;
		while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
			//���û�������Ϣ���Ա�
			if (fId == id && fName == name && fPwd == pwd) {
				cout << "ѧ����֤��½�ɹ���" << endl;
				system("pause");
				system("cls");
				person = new Student(id,name,pwd);//�ø���ָ�뿪��һ��������ѧ�������
				//����ѧ������Ӳ˵�
				studentMenu(person);//������ָ�봫��
				return;
			}
		}
	}
	else if (type == 2) {
		//��ʦ�����֤
		int fId;
		string fName;
		string fPwd;
		while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
			//���û�������Ϣ���Ա�
			if (fId == id && fName == name && fPwd == pwd) {
				cout << "��ʦ��֤��½�ɹ���" << endl;
				system("pause");
				system("cls");
				person = new Teacher(id,name,pwd);
				//�����ʦ����Ӳ˵�
				teacherMenu(person);
				return;
			}
		}
	}
	else {
		//����Ա�����֤
		string fName;
		string fPwd;
		while (ifs >> fName && ifs >> fPwd) {
			//���û�������Ϣ���Ա�
			if (fName == name && fPwd == pwd) {
				cout << "����Ա��֤��½�ɹ���" << endl;
				system("pause");
				system("cls");
				person = new Manager(name,pwd);
				//�������Ա����Ӳ˵�
				managerMenu(person);
				return;
			}
		}
	}

	cout << "��֤��¼ʧ�ܣ�" << endl;
	system("pause");
	system("cls");

	return;
}
int main() {
	int cmd = 0;//�����û�ѡ��

	while (1) {
		cout << "=======================��ӭ��������ԤԼϵͳ=======================" << endl;
		cout << endl << "�������������" << endl;
		cout << "\t\t---------------------------\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     1. ѧ������         |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     2. ��ʦ             |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     3. ����Ա           |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     0. �˳�             |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t---------------------------\n";
		cout << "����������ѡ��";

		cin >> cmd;

		switch (cmd) {
		case 1://ѧ��
			LoginIn(STUDENT_FILE, 1);//����ѧ����¼����
			break;
		case 2://��ʦ
			LoginIn(TEACHER_FILE, 2);
			break;
		case 3://����Ա			
			LoginIn(ADMIN_FILE, 3);
			break;
		case 0://�˳�
			cout << "��ӭ��һ��ʹ��" << endl;
			system("pause");
			return 0;
			break;
		default:
			cout << "��������������ѡ��" << endl;
			system("pause");
			system("cls");
			break;
		}
	}
	system("pause");
	return 0;
}