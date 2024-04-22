#include"Boss.h"

Boss::Boss() {

}

Boss::Boss(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
	this->money = 0;
	//获取金钱
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
	cout << "1，审核预约" << endl;
	cout << "2，查看所有预约" << endl;
	cout << "3，清空所有预约" << endl;
	cout << "4，账户管理" << endl;
	cout << "0，注销" << endl;
	cout << endl;
	cout << "请输入您的选择：";
}

void Boss::validOrder() {FLAG:
	cout << "待审核的预约：" << endl;
	Orderfile of;
	int NUM = 0;
	int num = 1;
	map<int, int> m_index;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if ((*it)["status"] == "1") {
			cout << num << "，";
			of.showInf_All(it);
			m_index.insert(make_pair(num, NUM));
			num++;
		}
		NUM++;
	}
	int cmd = 0;
	while (1) {
		cout << "请选择审核的预约编号(输入0返回）：";
		cin >> cmd;
		if (cmd == 0) {
			system("cls");
			return;
		}
		if (cmd >= 1 && cmd < num) break;
		else cout << "输入错误，请重新输入" << endl;
	}

	Court court;
	int cmd_date = atoi(of.vOF[m_index[cmd]]["date"].c_str());
	int cmd_court = atoi(of.vOF[m_index[cmd]]["court"].c_str());
	int cmd_time = atoi(of.vOF[m_index[cmd]]["time"].c_str());

	cout << endl << "请选择：1，通过 2，不通过";
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
	cout << "审核完毕,是否继续审核？ 1，是  2，否" << endl;
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
	cout << "当前账户余额：[ ";
	cout << this->money;
	cout << "元 ]" << endl;
}

void Boss::showRecord() {
	cout << endl << "交易记录:" << endl;
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
	cout << "请输入充值金额：" << endl;
	int Money;
	cin >> Money;
	if (Money <= 0) {
		cout << "输入错误！" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "充值操作执行中......" << endl;
	this->money += Money;
	ofstream ofs(this->name + "Boss_Money.txt", ios::out|ios::app);
	ofs << "+" << Money << endl;
	cout << "充值成功！" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}
void Boss::withDraw() {
	cout << "请输入提现金额：" << endl;
	int Money;
	cin >> Money;
	if (Money <= 0) {
		cout << "输入错误！" << endl;
		system("pause");
		system("cls");
		return;
	}
	if (Money > this->money) {
		cout << "余额不足" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "提现操作执行中......" << endl;
	this->money -= Money;
	ofstream ofs(this->name + "Boss_Money.txt", ios::out | ios::app);
	ofs << "-" << Money << endl;
	cout << "提现成功！" << endl;
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
	//获取老板和用户信息
	ifstream ifs;
	//获取用户信息
	ifs.open("User.txt", ios::in);
	User u;
	while (ifs >> u.id && ifs >> u.name && ifs >> u.pwd) {
		vUser.push_back(u);
	}
	ifs.close();
	//获取老板信息
	ifs.open("Boss.txt", ios::in);
	Boss b;
	while (ifs >> b.id && ifs >> b.name && ifs >> b.pwd) {
		vBoss.push_back(b);
	}
	ifs.close();
	/// ///////////////////////////////////////////////////
FLAG2:
	cout << "您目前的账户信息是：";
	vector<Boss>::iterator IT;
	for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
		if ((it->id == this->id)) {
			cout << "ID:" << it->id << " 姓名：" << it->name << " 密码：" << it->pwd << endl;
			IT = it;
		}
	}
	cout << "请输入修改的信息：" << endl;
	cout << "1，ID" << endl;
	cout << "2，姓名" << endl;
	cout << "3，密码" << endl;
	cout << "0，返回" << endl;
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
			cout << "请输入新ID：";
			cin >> Id;
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
				if (IT == it) continue;
				if (Id == it->id) {
					cout << endl << "ID重复，请重新输入！" << endl;
					goto FLAG;
				}
			}
			break;
		}
		IT->id = Id;
	}
	else if (cmd == 2) {
		cout << "请输入新姓名：";
		string Name;
		cin >> Name;
		IT->name = Name;
	}
	else if (cmd == 3) {
		cout << "请输入新密码：";
		string Pwd;
		cin >> Pwd;
		IT->pwd = Pwd;
	}
	cout << "您的新的账户信息为：";
	cout << "ID:" << IT->id << " 姓名：" << IT->name << " 密码：" << IT->pwd << endl;
	system("pause");
	cout << "是否继续修改？1，是 2，否" << endl;
	int select = 0;
	cin >> select;
	if (select == 1) goto FLAG2;
	///////////////////////updateFile////////////////////////////////////
		//更新老板
	ofstream ofs("Boss.txt", ios::out | ios::trunc);
	for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
		ofs << it->id << " " << it->name << " " << it->pwd << endl;//更新文件
	}
	ofs.close();
	//更新用户
	ofs.open("User.txt", ios::out | ios::trunc);
	for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
		ofs << it->id << " " << it->name << " " << it->pwd << endl;//更新文件
	}
	ofs.close();

	/// /// ///////////////////////////////////////////////////////

	cout << "修改成功！";
	system("pause");
	system("cls");
}