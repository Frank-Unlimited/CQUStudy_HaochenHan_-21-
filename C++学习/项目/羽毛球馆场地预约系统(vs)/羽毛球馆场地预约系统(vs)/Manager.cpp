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
	cout << "1，创建账号" << endl;
	cout << "2，查看账号" << endl;
	cout << "3，删除账号" << endl;
	cout << "0，注销" << endl;
	cout << endl;
	cout << "请输入您的选择：";
}

void Manager::addPerson() {
	ofstream ofs;

	cout << "请输入添加账号的类型：" << endl;
	cout << "1，球馆老板" << endl;
	cout << "2，租借方" << endl;

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
		else cout << "输入错误，请重新输入" << endl;
	}
	cout << endl;
	cout << "创建id：";
	int id;
	while (1) {
		bool flag = 1;
		cin >> id;
		ifstream ifs(fileName, ios::in);
		int fId;
		string x;
		while (ifs >> fId && ifs >> x && ifs >> x) {
			if (fId == id) {
				cout << "id重复，请重新输入！" << endl;
				flag = 0;
			}
		}
		if (flag) break;
	}
	cout << endl;

	cout << "创建用户名：";
	string name;
	cin >> name;
	cout << endl;

	cout << "创建密码：";
	string pwd;
	cin >> pwd;

	ofs.open(fileName, ios::out | ios::app);
	ofs << id << " " << name << " " << pwd << endl;
	ofs.close();

	ofs.open(name+file_money_Name+"_Money.txt",ios::out);
	ofs << "+100"<<endl;
	ofs.close();

	cout << "添加成功！" << endl;
	this->updateVector();
	system("pause");
	system("cls");
}

void Manager::showPerson() {
	int cmd = 0;
	while (1) {
		cout << "请选择查看账号类型：" << endl;
		cout << "1，老板" << endl;
		cout << "2，租借方" << endl;
		cin >> cmd;
		if (cmd == 1) {
			int num = 0;
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " 姓名："
					<< setw(6) << it->name << " 密码：" << it->pwd << endl;
			}
			break;
		}
		else if (cmd == 2) {
			int num = 0;
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " 姓名："
					<< setw(6) << it->name << " 密码：" << it->pwd << endl;
			}
			break;
		}
		else cout << "输入错误，请重新输入" << endl;
	}
	system("pause");
	system("cls");

}

void Manager::cleanPerson() {
	while (1) {
		cout << "请输入删除的账号类型：" << endl;
		cout << "1，老板" << endl;
		cout << "2，租借方" << endl;
		int cmd = 0;
		cin >> cmd;
		if (cmd == 1) {
			cout << "请输入删除的老板编号：" << endl;
			int num = 0;
			for (vector<Boss>::iterator it = vBoss.begin(); it != vBoss.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " 姓名："
					<< setw(6) << it->name << " 密码：" << it->pwd << endl;
			}
			int cmd2 = 0;
			cin >> cmd2;
			//cout<<num<<"==="<<endl;
			if (cmd2<0 || cmd2>num) {
				cout << "输入错误！" << endl;
				return;
			}
			vBoss.erase(vBoss.begin() + cmd2 - 1);
			this->updateFile();
			cout << "删除成功" << endl;
			break;
		}
		else if (cmd == 2) {
			cout << "请输入删除的租借方编号：" << endl;
			int num = 0;
			for (vector<User>::iterator it = vUser.begin(); it != vUser.end(); it++) {
				cout << ++num << ", Id:" << setiosflags(ios::left) << setw(8) << it->id << " 姓名："
					<< setw(6) << it->name << " 密码：" << it->pwd << endl;
			}
			int cmd2 = 0;
			cin >> cmd2;
			if (cmd2<0 || cmd2>num) {
				cout << "输入错误！" << endl;
				system("pause");
				system("cls");
				return;
			}
			vUser.erase(vUser.begin() + cmd2 - 1);
			this->updateFile();
			cout << "删除成功" << endl;
			break;
		}
		else cout << "输入错误，请重新输入" << endl;
	}
	system("pause");
	system("cls");
}
void Manager::updateFile() {
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
}
void Manager::updateVector() {
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
}