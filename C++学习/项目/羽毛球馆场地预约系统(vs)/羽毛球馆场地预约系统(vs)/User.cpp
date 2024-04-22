#include "User.h"
#include"Court.h"

User::User() {
	
}

User::User(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
	this->money = 0;
	//获取金钱
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
	//cout << this->name<<"的钱：" << this->money << endl;
}

void User::operMenu() {
	cout << endl;
	cout << "1，申请预约" << endl;
	cout << "2，查看我的预约" << endl;
	cout << "3，查看所有预约" << endl;
	cout << "4，取消预约" << endl;
	cout << "5，付款" << endl;
	cout << "6，账户管理" << endl;
	cout << "0，注销" << endl;
	cout << endl;
	cout << "请输入您的选择：";
}

void User::applyOrder() {
	Court court;
	while (1) {
		cout << "是否查看本周所有时段场地状态？" << endl;
		cout << "1，是" << endl;
		cout << "2，否" << endl;
		int cmd;
		cin >> cmd;
		if (cmd == 1) {
			cout << "本周场地状态：" << endl;
			cout << "提示：0表示空余场地，1表示已预约未审核场地，2表示已通过未付款的场地，3表示已付款的场地" << endl;
			court.showCourt();
		}
		else if (cmd != 2) {
			cout << "输入错误" << endl;
			return;
		}
		cout << "请输入预约时间(输入0返回)：" << endl;
		for (int i = 1; i <= 7; i++) cout << i << ", 星期" << i << endl;
		int cmd_date;
		cin >> cmd_date;
		if (cmd_date == 0) {
			system("cls");
			return;
		}
		if (cmd_date >= 1 && cmd_date <= 7) {
			cout << "请输入开始打球的时间（0—23点）：";
			int cmd_time;
			cin >> cmd_time;
			if (cmd_time < 0 || cmd_time>23) {
				cout << "输入错误！" << endl;
				continue;
			}

			cout << "当日" << cmd_time << "点到" << cmd_time + 1 << "点空余场地编号为：";
			vector<int> freeCourt;
			for (int i = 1; i <= 6; i++) {
				if (court.court_status[cmd_date][i][cmd_time] == 0) {
					cout << i << " ";
					freeCourt.push_back(i);
				}
			}
			cout << endl;
			cout << "请输入预约场地编号：";
			int cmd_court;
			cin >> cmd_court;
			bool flag = 0;
			for (vector<int>::iterator it = freeCourt.begin(); it != freeCourt.end(); it++) {
				if (cmd_court == *it) flag = 1;
			}
			if (flag == 0) {
				cout << "输入错误！" << endl;
				continue;
			}

			court.court_status[cmd_date][cmd_court][cmd_time] = 1;
			court.updateFile();
			ofstream ofs("Order.txt", ios::out | ios::app);
			ofs << cmd_date << " " << cmd_court << " " << cmd_time << " " << this->id << " " << this->name << " 1" << endl;
			cout << "预约成功！审核中" << endl;
			system("pause");
			cout << "是否继续预约？" << endl;
			cout << "1，是" << endl;
			cout << "2，否" << endl;
			int select = 0;
			cin >> select;
			if (select == 1) continue;
			break;
		}
		else cout << "输入有误！请重新输入" << endl;
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
			cout << "星期" << fDate << " " << fCourt << "号场地 " << setiosflags(ios::left)<<setw(2) << fTime << "-" <<
				setw(2)<<fTime + 1 << "点 预约状态：";
			if (fStatus == -1) cout << "已取消" << endl;
			else if (fStatus == 1) cout << "审核中" << endl;
			else if (fStatus == 2) cout << "未付款" << endl;
			else if (fStatus == 3) cout << "已付款" << endl;
			else cout << "未通过" << endl;
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
	cout << endl << "只有审核中和未付款的预约可取消，您可取消的预约为：" << endl;
	int num = 1;
	int NUM = 0;
	map<int, int> m_index;
	bool flag = 0;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if (((*it)["id"] == (to_string(this->id)) && ((*it)["status"] == "1" || (*it)["status"] == "2"))) {
			cout << num << "，星期" << (*it)["date"] << " " << (*it)["court"] << "号场地 "
				<< (*it)["time"] << "点-" << atoi((*it)["time"].c_str()) + 1 << "点 预约状态：" <<
				((*it)["status"] == "1" ? "审核中" : "未付款") << endl;
			m_index.insert(make_pair(num, NUM));
			num++;
			flag = 1;
		}
		NUM++;
	}
	if (flag == 0) {
		cout << "无" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "请选择您要取消的预约：" << endl;
	int cmd = 0;
	cin >> cmd;
	if (cmd <= 0 || cmd >= num) {
		cout << "输入错误" << endl;
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
	cout << "取消成功" << endl;
	system("pause");
	system("cls");
}

void User::payMoney() {
	cout << "可付款的预约如下：" << endl;
	int num = 1;
	int NUM = 0;
	map<int, int> m_index;
	Orderfile of;
	bool flag = 0;
	for (vector<map<string, string>>::iterator it = of.vOF.begin(); it != of.vOF.end(); it++) {
		if ((*it)["status"] == "2") {
			cout << num << "，";
			of.showInf_Part(it);
			m_index.insert(make_pair(num, NUM));
			num++;
			flag = 1;
		}
		NUM++;
	}
	if (flag == 0) {
		cout << "无" << endl;
		system("pause");
		system("cls");
		return;
	}
	cout << endl << "请选择您要付款预约：" << endl;
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
	cout << endl << "价格: " << price << "元/小时" << endl << "确认付款？" << endl;
	this->showMoney();
	cout << "1，确认 2，返回" << endl;

	int cmd_money = 0;
	cin >> cmd_money;
	if (cmd_money != 1) {
		system("cls");
		return;
	}
	if (price > this->money) {
		cout << "余额不足！" << endl;
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
	cout << "付款成功" << endl;
	system("pause");
	system("cls");
}

void User::showMoney() {
	cout << "当前账户余额：[ ";
	cout << this->money;
	cout << "元 ]" << endl;
}

void User::chargeMoney() {
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
	ofstream ofs(this->name + "User_Money.txt", ios::out | ios::app);
	ofs << "+" << Money << endl;
	cout << "充值成功！" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}
void User::withDraw() {
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
	ofstream ofs(this->name + "User_Money.txt", ios::out | ios::app);
	ofs << "-" << Money << endl;
	cout << "提现成功！" << endl;
	this->showMoney();
	system("pause");
	system("cls");
}

void User::showRecord() {
	cout << endl << "交易记录:" << endl;
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
	vector<User>::iterator IT;
	for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
		if ((it->id == this->id)) {
			cout << "ID:" << it->id << " 姓名：" << it->name << " 密码：" << it->pwd << endl;
			IT = it;
		}
	}
	cout << "请输入修改的信息："<<endl;
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
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
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
