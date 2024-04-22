#include"Boss.h"

Boss::Boss() {

}

Boss::Boss(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
	this->money = 0;
	//��ȡ��Ǯ
	ifstream ifs(name + "Boss_Money.txt", ios::in);
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
}

void Boss::operMenu() {
	cout << endl;
	cout << "1�����ԤԼ" << endl;
	cout << "2���鿴����ԤԼ" << endl;
	cout << "3���������ԤԼ" << endl;
	cout << "4���˻�����" << endl;
	cout << "0��ע��" << endl;
	cout << endl;
	cout << "����������ѡ��";
}

void Boss::validOrder() {FLAG:
	cout << "����˵�ԤԼ��" << endl;
	Orderfile of;
	int NUM = 0;
	int num = 1;
	map<int, int> m_index;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if ((*it)["status"] == "1") {
			cout << num << "��";
			of.showInf_All(it);
			m_index.insert(make_pair(num, NUM));
			num++;
		}
		NUM++;
	}
	int cmd = 0;
	while (1) {
		cout << "��ѡ����˵�ԤԼ���(����0���أ���";
		cin >> cmd;
		if (cmd == 0) {
			system("cls");
			return;
		}
		if (cmd >= 1 && cmd < num) break;
		else cout << "�����������������" << endl;
	}

	Court court;
	int cmd_date = atoi(of.vOF[m_index[cmd]]["date"].c_str());
	int cmd_court = atoi(of.vOF[m_index[cmd]]["court"].c_str());
	int cmd_time = atoi(of.vOF[m_index[cmd]]["time"].c_str());

	cout << endl << "��ѡ��1��ͨ�� 2����ͨ��";
	int cmd2 = 0;
	cin >> cmd2;

	if (cmd2 == 1) {
		of.vOF[m_index[cmd]]["status"] = "2";
		court.court_status[cmd_date][cmd_court][cmd_time] = 2;
	}
		
	else {
		of.vOF[m_index[cmd]]["status"] = "5";
		court.court_status[cmd_date][cmd_court][cmd_time] = 0;
	}
	court.updateFile();
	of.updateOrder();
	cout << "������,�Ƿ������ˣ� 1����  2����" << endl;
	int cmd3 = 0;
	cin >> cmd3;
	if (cmd3 == 1) goto FLAG;
	system("pause");
	system("cls");
}

void Boss::showAllOrder() {
	Orderfile of;
	of.showContent();

	system("pause");
	system("cls");
}

void Boss::getMoney() {

}

void Boss::cleanOrder() {
	Orderfile of;
	of.initOrder();
}

void Boss::showMoney() {
	cout << "��ǰ�˻���[ ";
	cout << this->money;
	cout << "Ԫ ]" << endl;
}

void Boss::showRecord() {
	cout << endl << "���׼�¼:" << endl;
	cout << "--------" << endl;
	ifstream ifs(this->name + "Boss_Money.txt", ios::in);
	string x;
	while (ifs >> x) {
		cout << setiosflags(ios::left)<<setw(8)<<x<<"|"<<endl;
	}
	cout << "--------" << endl;
	cout << endl;
	system("pause");
	system("cls");
}
void Boss::chargeMoney() {
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
	ofstream ofs(this->name + "Boss_Money.txt", ios::out|ios::app);
	ofs << "+" << Money << endl;
	cout << "��ֵ�ɹ���" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}
void Boss::withDraw() {
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
	ofstream ofs(this->name + "Boss_Money.txt", ios::out | ios::app);
	ofs << "-" << Money << endl;
	cout << "���ֳɹ���" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}

void Boss::changeInf() {
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
	vector<Boss>::iterator IT;
	for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
		if ((it->id == this->id)) {
			cout << "ID:" << it->id << " ������" << it->name << " ���룺" << it->pwd << endl;
			IT = it;
		}
	}
	cout << "�������޸ĵ���Ϣ��" << endl;
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
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
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