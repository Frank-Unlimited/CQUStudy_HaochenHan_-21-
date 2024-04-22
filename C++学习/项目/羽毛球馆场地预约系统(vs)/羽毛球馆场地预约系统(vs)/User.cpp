#include "User.h"
#include"Court.h"

User::User() {
	
}

User::User(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
	this->money = 0;
	//��ȡ��Ǯ
	ifstream ifs(name+"User_Money.txt",ios::in);
	string x;
	while (ifs >> x) {
		int M = atoi(x.substr(1, x.size() - 1).c_str());
		if (x[0] == '+') {
			this->money += M;
		}
		else if (x[0] == '-') {
			this->money -= M;
		}
	}
	//cout << this->name<<"��Ǯ��" << this->money << endl;
}

void User::operMenu() {
	cout << endl;
	cout << "1������ԤԼ" << endl;
	cout << "2���鿴�ҵ�ԤԼ" << endl;
	cout << "3���鿴����ԤԼ" << endl;
	cout << "4��ȡ��ԤԼ" << endl;
	cout << "5������" << endl;
	cout << "6���˻�����" << endl;
	cout << "0��ע��" << endl;
	cout << endl;
	cout << "����������ѡ��";
}

void User::applyOrder() {
	Court court;
	while (1) {
		cout << "�Ƿ�鿴��������ʱ�γ���״̬��" << endl;
		cout << "1����" << endl;
		cout << "2����" << endl;
		int cmd;
		cin >> cmd;
		if (cmd == 1) {
			cout << "���ܳ���״̬��" << endl;
			cout << "��ʾ��0��ʾ���ೡ�أ�1��ʾ��ԤԼδ��˳��أ�2��ʾ��ͨ��δ����ĳ��أ�3��ʾ�Ѹ���ĳ���" << endl;
			court.showCourt();
		}
		else if (cmd != 2) {
			cout << "�������" << endl;
			return;
		}
		cout << "������ԤԼʱ��(����0����)��" << endl;
		for (int i = 1; i <= 7; i++) cout << i << ", ����" << i << endl;
		int cmd_date;
		cin >> cmd_date;
		if (cmd_date == 0) {
			system("cls");
			return;
		}
		if (cmd_date >= 1 && cmd_date <= 7) {
			cout << "�����뿪ʼ�����ʱ�䣨0��23�㣩��";
			int cmd_time;
			cin >> cmd_time;
			if (cmd_time < 0 || cmd_time>23) {
				cout << "�������" << endl;
				continue;
			}

			cout << "����" << cmd_time << "�㵽" << cmd_time + 1 << "����ೡ�ر��Ϊ��";
			vector<int> freeCourt;
			for (int i = 1; i <= 6; i++) {
				if (court.court_status[cmd_date][i][cmd_time] == 0) {
					cout << i << " ";
					freeCourt.push_back(i);
				}
			}
			cout << endl;
			cout << "������ԤԼ���ر�ţ�";
			int cmd_court;
			cin >> cmd_court;
			bool flag = 0;
			for (vector<int>::iterator it = freeCourt.begin(); it != freeCourt.end(); it++) {
				if (cmd_court == *it) flag = 1;
			}
			if (flag == 0) {
				cout << "�������" << endl;
				continue;
			}

			court.court_status[cmd_date][cmd_court][cmd_time] = 1;
			court.updateFile();
			ofstream ofs("Order.txt", ios::out | ios::app);
			ofs << cmd_date << " " << cmd_court << " " << cmd_time << " " << this->id << " " << this->name << " 1" << endl;
			cout << "ԤԼ�ɹ��������" << endl;
			system("pause");
			cout << "�Ƿ����ԤԼ��" << endl;
			cout << "1����" << endl;
			cout << "2����" << endl;
			int select = 0;
			cin >> select;
			if (select == 1) continue;
			break;
		}
		else cout << "������������������" << endl;
	}
	system("pause");
	system("cls");
}

void User::showMyOrder() {
	ifstream ifs("Order.txt", ios::in);
	int fDate;
	int fCourt;
	int fTime;
	int fId;
	string fName;
	int fStatus;
	while (ifs >> fDate && ifs >> fCourt && ifs >> fTime && ifs >> fId && ifs >> fName && ifs >> fStatus) {
		if (this->id == fId) {
			cout << "����" << fDate << " " << fCourt << "�ų��� " << setiosflags(ios::left)<<setw(2) << fTime << "-" <<
				setw(2)<<fTime + 1 << "�� ԤԼ״̬��";
			if (fStatus == -1) cout << "��ȡ��" << endl;
			else if (fStatus == 1) cout << "�����" << endl;
			else if (fStatus == 2) cout << "δ����" << endl;
			else if (fStatus == 3) cout << "�Ѹ���" << endl;
			else cout << "δͨ��" << endl;
		}
	}
	system("pause");
	system("cls");
}

void User::showAllOrder() {
	Orderfile of;
	of.showContent();
	
	system("pause");
	system("cls");
}

void User::cancelOrder() {
	Orderfile of;
	cout << endl << "ֻ������к�δ�����ԤԼ��ȡ��������ȡ����ԤԼΪ��" << endl;
	int num = 1;
	int NUM = 0;
	map<int, int> m_index;
	bool flag = 0;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if (((*it)["id"] == (to_string(this->id)) && ((*it)["status"] == "1" || (*it)["status"] == "2"))) {
			cout << num << "������" << (*it)["date"] << " " << (*it)["court"] << "�ų��� "
				<< (*it)["time"] << "��-" << atoi((*it)["time"].c_str()) + 1 << "�� ԤԼ״̬��" <<
				((*it)["status"] == "1" ? "�����" : "δ����") << endl;
			m_index.insert(make_pair(num, NUM));
			num++;
			flag = 1;
		}
		NUM++;
	}
	if (flag == 0) {
		cout << "��" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "��ѡ����Ҫȡ����ԤԼ��" << endl;
	int cmd = 0;
	cin >> cmd;
	if (cmd <= 0 || cmd >= num) {
		cout << "�������" << endl;
		system("pause");
		system("cls");
		return;
	}
	of.vOF[m_index[cmd]]["status"] = "-1";
	//cout << endl << m_index[cmd - 2] << endl << of.vOF[m_index[cmd - 1]]["status"] << endl;
	//of.showContent();
	of.updateOrder();
	Court court;
	int cmd_date = atoi(of.vOF[m_index[cmd]]["date"].c_str());
	int cmd_court = atoi(of.vOF[m_index[cmd]]["court"].c_str());
	int cmd_time = atoi(of.vOF[m_index[cmd]]["time"].c_str());
	court.court_status[cmd_date][cmd_court][cmd_time] = 0;
	court.updateFile();
	cout << "ȡ���ɹ�" << endl;
	system("pause");
	system("cls");
}

void User::payMoney() {
	cout << "�ɸ����ԤԼ���£�" << endl;
	int num = 1;
	int NUM = 0;
	map<int, int> m_index;
	Orderfile of;
	bool flag = 0;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if ((*it)["status"] == "2") {
			cout << num << "��";
			of.showInf_Part(it);
			m_index.insert(make_pair(num, NUM));
			num++;
			flag = 1;
		}
		NUM++;
	}
	if (flag == 0) {
		cout << "��" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "��ѡ����Ҫ����ԤԼ��" << endl;
	int cmd = 0;
	cin >> cmd;

	Court court;
	int cmd_date = atoi(of.vOF[m_index[cmd]]["date"].c_str());
	int cmd_court = atoi(of.vOF[m_index[cmd]]["court"].c_str());
	int cmd_time = atoi(of.vOF[m_index[cmd]]["time"].c_str());

	int price = 0;
	if (cmd_time >= 0 && cmd_time < 12) {
		price = 15;
	}
	else if (cmd_time >= 12 && cmd_time < 18) {
		price = 40;
	}
	else if (cmd_time >= 18 && cmd_time <= 24) {
		price = 60;
	}
	cout << endl << "�۸�: " << price << "Ԫ/Сʱ" << endl << "ȷ�ϸ��" << endl;
	this->showMoney();
	cout << "1��ȷ�� 2������" << endl;

	int cmd_money = 0;
	cin >> cmd_money;
	if (cmd_money != 1) {
		system("cls");
		return;
	}
	if (price > this->money) {
		cout << "���㣡" << endl;
		system("pause");
		system("cls");
		return;
	}

	this->money -= price;
	ofstream ofs("CQUBoss_Money.txt", ios::out|ios::app);
	ofs << "+" << price << endl;
	ofs.close();

	of.vOF[m_index[cmd]]["status"] = "3";
	of.updateOrder();
	court.court_status[cmd_date][cmd_court][cmd_time] = 3;
	court.updateFile();
	cout << "����ɹ�" << endl;
	system("pause");
	system("cls");
}

void User::showMoney() {
	cout << "��ǰ�˻���[ ";
	cout << this->money;
	cout << "Ԫ ]" << endl;
}

void User::chargeMoney() {
	cout << "�������ֵ��" << endl;
	int Money;
	cin >> Money;
	if (Money <= 0) {
		cout << "�������" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "��ֵ����ִ����......" << endl;
	this->money += Money;
	ofstream ofs(this->name + "User_Money.txt", ios::out | ios::app);
	ofs << "+" << Money << endl;
	cout << "��ֵ�ɹ���" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}
void User::withDraw() {
	cout << "���������ֽ�" << endl;
	int Money;
	cin >> Money;
	if (Money <= 0) {
		cout << "�������" << endl;
		system("pause");
		system("cls");
		return;
	}
	if (Money > this->money) {
		cout << "����" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "���ֲ���ִ����......" << endl;
	this->money -= Money;
	ofstream ofs(this->name + "User_Money.txt", ios::out | ios::app);
	ofs << "-" << Money << endl;
	cout << "���ֳɹ���" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}

void User::showRecord() {
	cout << endl << "���׼�¼:" << endl;
	cout << "--------" << endl;
	ifstream ifs(this->name + "User_Money.txt", ios::in);
	string x;
	while (ifs >> x) {
		cout << setiosflags(ios::left) << setw(8) << x << "|" << endl;
	}
	cout << "--------" << endl;
	cout << endl;
	system("pause");
	system("cls");
}


void User::changeInf() {
	////////////////////updateVector/////////////////////////////////
	vector<Boss> vBoss;
	vector<User> vUser;
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
	/// ///////////////////////////////////////////////////
FLAG2:
	cout << "��Ŀǰ���˻���Ϣ�ǣ�";
	vector<User>::iterator IT;
	for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
		if ((it->id == this->id)) {
			cout << "ID:" << it->id << " ������" << it->name << " ���룺" << it->pwd << endl;
			IT = it;
		}
	}
	cout << "�������޸ĵ���Ϣ��"<<endl;
	cout << "1��ID" << endl;
	cout << "2������" << endl;
	cout << "3������" << endl;
	cout << "0������" << endl;
	int cmd;
	cin >> cmd;
	if (cmd != 1 && cmd != 2 && cmd != 3) {
		system("cls");
		return;
	}
	if (cmd == 1) {
		int Id;
		while (1) {
		FLAG:
			cout << "��������ID��";
			cin >> Id;
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
				if (IT == it) continue;
				if (Id == it->id) {
					cout << endl << "ID�ظ������������룡" << endl;
					goto FLAG;
				}
			}
			break;
		}
		IT->id = Id;
	}
	else if (cmd == 2) {
		cout << "��������������";
		string Name;
		cin >> Name;
		IT->name = Name;
	}
	else if (cmd == 3) {
		cout << "�����������룺";
		string Pwd;
		cin >> Pwd;
		IT->pwd = Pwd;
	}
	cout << "�����µ��˻���ϢΪ��";
	cout << "ID:" << IT->id << " ������" << IT->name << " ���룺" << IT->pwd << endl;
	system("pause");
	cout << "�Ƿ�����޸ģ�1���� 2����" << endl;
	int select = 0;
	cin >> select;
	if (select == 1) goto FLAG2;
	///////////////////////updateFile////////////////////////////////////
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

	/// /// ///////////////////////////////////////////////////////

	cout << "�޸ĳɹ���";
	system("pause");
	system("cls");
}
