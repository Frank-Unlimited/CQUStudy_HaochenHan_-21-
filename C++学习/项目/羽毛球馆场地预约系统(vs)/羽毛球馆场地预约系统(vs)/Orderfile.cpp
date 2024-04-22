#include"Orderfile.h"

Orderfile::Orderfile() {
	ifstream ifs("Order.txt", ios::in);
	string date;
	string court;
	string time;
	string id;
	string name;
	string status;
	vOF.clear();
	while (ifs >> date && ifs >> court && ifs >> time && ifs >> id && ifs >> name && ifs >> status) {
		map<string, string> m;
		m.insert(make_pair("date", date));
		m.insert(make_pair("court", court));
		m.insert(make_pair("time", time));
		m.insert(make_pair("id", id));
		m.insert(make_pair("name", name));
		m.insert(make_pair("status", status));
		this->vOF.push_back(m);
	}
}

void Orderfile::showContent() {
	int num = 0;
	for (vector<map<string, string>>::iterator it = vOF.begin(); it != vOF.end(); it++) {
		this->showInf_All(it);
	}
}

void Orderfile::updateOrder() {
	ofstream ofs("Order.txt", ios::out | ios::trunc);
	for (vector<map<string, string>>::iterator it = this->vOF.begin(); it != this->vOF.end(); it++) {
		ofs << (*it)["date"] << " " << (*it)["court"] << " " << (*it)["time"] << " " << (*it)["id"]
			<< " " << (*it)["name"] << " " << (*it)["status"] << endl;
	}
}

void Orderfile::initOrder() {
	cout << "是否清空所有预约？  1，是  2，否" << endl;
	int cmd;
	cin >> cmd;
	if (cmd != 1) return;
	ofstream ofs("Badminton court.txt", ios::out | ios::trunc);
	for (int i = 0; i < 7; i++) {
		for (int j = 0; j < 6; j++) {
			for (int k = 0; k < 24; k++) {
				ofs << "0 ";
			}
			ofs << endl;
		}
		ofs << endl << endl;
	}
	ofs.close();
	ofs.open("Order.txt", ios::out | ios::trunc);
	ofs.close();
	cout << "清空完毕" << endl;
	system("pause");
	system("cls");
}

void Orderfile::showInf_All(vector<map<string, string>>::iterator it) {
	cout << "星期" << (*it)["date"] << " " << (*it)["court"] << "号场地 " <<
		setiosflags(ios::left) << setw(2) << (*it)["time"] << "-" <<
		setw(2) << atoi((*it)["time"].c_str()) + 1 << "点 预约人：id:" << setw(9) << (*it)["id"] <<
		"姓名："<<setw(6) << (*it)["name"] << " 预约状态：";
	if ((*it)["status"] == "-1") cout << "已取消" << endl;
	else if ((*it)["status"] =="1") cout << "审核中" << endl;
	else if ((*it)["status"] == "2") cout << "未付款" << endl;
	else if ((*it)["status"] == "3") cout << "已付款" << endl;
	else cout << "未通过" << endl;
}

void Orderfile::showInf_Part(vector<map<string, string>>::iterator it) {
	cout << "星期" << (*it)["date"] << " " << (*it)["court"] << "号场地 " <<
		setiosflags(ios::left) << setw(2) << (*it)["time"] << "-" <<
		setw(2) << atoi((*it)["time"].c_str()) + 1 << "点 预约状态：";
	if ((*it)["status"] == "-1") cout << "已取消" << endl;
	else if ((*it)["status"] == "1") cout << "审核中" << endl;
	else if ((*it)["status"] == "2") cout << "未付款" << endl;
	else if ((*it)["status"] == "3") cout << "已付款" << endl;
	else cout << "未通过" << endl;
}