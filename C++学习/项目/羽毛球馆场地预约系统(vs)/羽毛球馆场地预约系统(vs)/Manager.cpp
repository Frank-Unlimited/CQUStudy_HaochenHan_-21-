#include "Manager.h"

Manager::Manager() {
	this->updateVector();
}

Manager::Manager(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
	this->updateVector();
}

void Manager::operMenu() {
	cout << endl;
	cout << "1�������˺�" << endl;
	cout << "2���鿴�˺�" << endl;
	cout << "3��ɾ���˺�" << endl;
	cout << "0��ע��" << endl;
	cout << endl;
	cout << "����������ѡ��";
}

void Manager::addPerson() {
	ofstream ofs;

	cout << "����������˺ŵ����ͣ�" << endl;
	cout << "1������ϰ�" << endl;
	cout << "2����跽" << endl;

	string fileName;
	string file_money_Name;
	int cmd;
	while (1) {
		cin >> cmd;
		if (cmd == 1) {
			file_money_Name = "Boss";
			fileName = "Boss.txt";
			break;
		}
		else if (cmd == 2) {
			file_money_Name="User";
			fileName = "User.txt";
			break;
		}
		else cout << "�����������������" << endl;
	}
	cout << endl;
	cout << "����id��";
	int id;
	while (1) {
		bool flag = 1;
		cin >> id;
		ifstream ifs(fileName, ios::in);
		int fId;
		string x;
		while (ifs >> fId && ifs >> x && ifs >> x) {
			if (fId == id) {
				cout << "id�ظ������������룡" << endl;
				flag = 0;
			}
		}
		if (flag) break;
	}
	cout << endl;

	cout << "�����û�����";
	string name;
	cin >> name;
	cout << endl;

	cout << "�������룺";
	string pwd;
	cin >> pwd;

	ofs.open(fileName, ios::out | ios::app);
	ofs << id << " " << name << " " << pwd << endl;
	ofs.close();

	ofs.open(name+file_money_Name+"_Money.txt",ios::out);
	ofs << "+100"<<endl;
	ofs.close();

	cout << "��ӳɹ���" << endl;
	this->updateVector();
	system("pause");
	system("cls");
}

void Manager::showPerson() {
	int cmd = 0;
	while (1) {
		cout << "��ѡ��鿴�˺����ͣ�" << endl;
		cout << "1���ϰ�" << endl;
		cout << "2����跽" << endl;
		cin >> cmd;
		if (cmd == 1) {
			int num = 0;
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " ������"
					<< setw(6) << it->name << " ���룺" << it->pwd << endl;
			}
			break;
		}
		else if (cmd == 2) {
			int num = 0;
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " ������"
					<< setw(6) << it->name << " ���룺" << it->pwd << endl;
			}
			break;
		}
		else cout << "�����������������" << endl;
	}
	system("pause");
	system("cls");

}

void Manager::cleanPerson() {
	while (1) {
		cout << "������ɾ�����˺����ͣ�" << endl;
		cout << "1���ϰ�" << endl;
		cout << "2����跽" << endl;
		int cmd = 0;
		cin >> cmd;
		if (cmd == 1) {
			cout << "������ɾ�����ϰ��ţ�" << endl;
			int num = 0;
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " ������"
					<< setw(6) << it->name << " ���룺" << it->pwd << endl;
			}
			int cmd2 = 0;
			cin >> cmd2;
			//cout<<num<<"==="<<endl;
			if (cmd2<0 || cmd2>num) {
				cout << "�������" << endl;
				return;
			}
			vBoss.erase(vBoss.begin() + cmd2 - 1);
			this->updateFile();
			cout << "ɾ���ɹ�" << endl;
			break;
		}
		else if (cmd == 2) {
			cout << "������ɾ������跽��ţ�" << endl;
			int num = 0;
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " ������"
					<< setw(6) << it->name << " ���룺" << it->pwd << endl;
			}
			int cmd2 = 0;
			cin >> cmd2;
			if (cmd2<0 || cmd2>num) {
				cout << "�������" << endl;
				system("pause");
				system("cls");
				return;
			}
			vUser.erase(vUser.begin() + cmd2 - 1);
			this->updateFile();
			cout << "ɾ���ɹ�" << endl;
			break;
		}
		else cout << "�����������������" << endl;
	}
	system("pause");
	system("cls");
}
void Manager::updateFile() {
	//�����ϰ�
	ofstream ofs("Boss.txt", ios::out | ios::trunc);
	for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
		ofs << it->id << " " << it->name << " " << it->pwd << endl;//�����ļ�
	}
	ofs.close();
	//�����û�
	ofs.open("User.txt", ios::out | ios::trunc);
	for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
		ofs << it->id << " " << it->name << " " << it->pwd << endl;//�����ļ�
	}
	ofs.close();
}
void Manager::updateVector() {
	vBoss.clear();
	vUser.clear();
	//��ȡ�ϰ���û���Ϣ
	ifstream ifs;
	//��ȡ�û���Ϣ
	ifs.open("User.txt", ios::in);
	User u;
	while (ifs >> u.id && ifs >> u.name && ifs >> u.pwd) {
		vUser.push_back(u);
	}
	ifs.close();
	//��ȡ�ϰ���Ϣ
	ifs.open("Boss.txt", ios::in);
	Boss b;
	while (ifs >> b.id && ifs >> b.name && ifs >> b.pwd) {
		vBoss.push_back(b);
	}
	ifs.close();
}