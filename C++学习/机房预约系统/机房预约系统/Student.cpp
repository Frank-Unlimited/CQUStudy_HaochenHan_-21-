#include"Student.h"

//默认构造
Student::Student() {

}
//有参构造
Student::Student(int id, string name, string pwd) {
	//初始化学生信息
	this->m_Id = id;
	this->m_Name = name;
	this->m_Pwd = pwd;
	//初始化机房信息
	ifstream ifs(COMPUTER_FILE, ios::in);
	ComputerRoom c;
	while (ifs >> c.m_ComId && ifs >> c.m_MaxNum) {
		vCom.push_back(c);
	}
	ifs.close();

}
//菜单界面
void Student::operMenu() {
	cout << "欢迎学生：" << this->m_Name << "登录！" << endl;
	cout << endl;
	cout << "1.申请预约" << endl;
	cout << "2.查看我的预约" << endl;
	cout << "3.查看所有预约" << endl;
	cout << "4.取消预约" << endl;
	cout << "0.注销登录" << endl;
	cout << endl;
	cout << "请输入您的选择：" << endl;
}
//申请预约
void Student::applyOrder() {
	cout << "机房开放时间为周一至周五" << endl;
	cout << "请输入申请预约的时间：" << endl;
	cout << "1，周一" << endl;
	cout << "2，周二" << endl;
	cout << "3，周三" << endl;
	cout << "4，周四" << endl;
	cout << "5，周五" << endl;
	
	//用于接受用户信息
	int date = 0;//日期
	int interval = 0;//时间段
	int room = 0;//机房编号

	while (1) {
		cin >> date;
		if (date >= 1 && date <= 5) break;
		cout << "输入有误，请重新输入" << endl;
	}

	cout << "请输入申请时间段：" << endl;
	cout << "1，上午" << endl;
	cout << "2，下午" << endl;

	while (1) {
		cin >> interval;
		if (interval >= 1 && interval <= 2) break;
		cout << "输入有误，请重新输入" << endl;
	}

	cout << "请选择机房：" << endl;
	for (int i = 0; i < vCom.size(); i++) {
		cout << vCom[i].m_ComId << "号机房 容量：" << vCom[i].m_MaxNum << endl;
	}

	while (1) {
		cin >> room;
		if (room >= 1 && room <= vCom.size()) break;
		cout << "输入有误，请重新输入" << endl;
	}

	ofstream ofs;
	ofs.open(ORDER_FILE, ios::app);

	ofs << "date:" << date << " ";
	ofs << "interval:" << interval << " ";
	ofs << "studentId:" << this->m_Id << " ";
	ofs << "studentName:" << this->m_Name << " ";
	ofs << "roomId:" << room << " ";
	ofs << "status:" << 1 << endl;

	cout << "预约成功！审核中" << endl;
	
	system("pause");
	system("cls");
}
//查看自身预约
void Student::showMyOrder() {
	OrderFile of;
	if (of.m_Size==0) {
		cout << "无预约记录" << endl;
		system("pause");
		system("cls");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		if (this->m_Id == atoi(of.m_orderData[i]["studentId"].c_str())) {//找到自身预约
			cout << "预约日期：星期" << of.m_orderData[i]["date"] << " ";
			cout << "时间段：" << (of.m_orderData[i]["interval"] == "1" ? "上午" : "下午") << " ";
			cout << "机房号：" << of.m_orderData[i]["roomId"] << " ";
			cout << "预约状态：";
			if(of.m_orderData[i]["status"]=="1") cout << "审核中" << " ";
			else if(of.m_orderData[i]["status"] == "2") cout << "预约成功" << " ";
			else if (of.m_orderData[i]["status"] == "-1") cout << "审核未通过" << " ";
			else cout << "预约已取消" << " ";
			cout << endl;
		}
	}
	system("pause");
	system("cls");
}
//查看所有预约
void Student::showAllOrder() {
	OrderFile of;
	
	if (of.m_Size == 0) {
		cout << "无预约记录" << endl;
		system("pause");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		cout << i + 1 << ", ";
		cout << "预约日期： 周" << of.m_orderData[i]["date"] << " ";
		cout << "时间段：" << (of.m_orderData[i]["interval"]=="1"?"上午":"下午") << " ";
		cout << "学号：" << setiosflags(ios::left) << setw(12) << of.m_orderData[i]["studentId"] << " ";
		cout << "姓名：" << setw(6) << of.m_orderData[i]["studentName"] << " ";
		cout << "机房编号：" << of.m_orderData[i]["roomId"] << " ";
		cout << "审核状态：";
		if (of.m_orderData[i]["status"] == "1") cout << "审核中" << " ";
		else if (of.m_orderData[i]["status"] == "2") cout << "预约成功" << " ";
		else if (of.m_orderData[i]["status"] == "-1") cout << "审核未通过" << " ";
		else if (of.m_orderData[i]["status"] == "0") cout << "预约已取消" << " ";
		cout << endl;
	}

	system("pause");
	system("cls");
}
//取消预约
void Student::cancelOrder() {
	OrderFile of;

	if (of.m_Size == 0) {
		cout << "无预约记录" << endl;
		system("pause");
		system("cls");
		return;
	}

	int index = 1;
	vector<int> v;//存放下标编号
	for (int i = 0; i < of.m_Size; i++) {
		//判断是自身学号
		if (this->m_Id == atoi(of.m_orderData[i]["studentId"].c_str())) {
			//筛选状态(审核中/预约成功)
			if (of.m_orderData[i]["status"] == "1" || of.m_orderData[i]["status"] == "2") {
				v.push_back(i);
				cout << index++ << ", " << "预约日期：星期" << of.m_orderData[i]["date"] << " ";
				cout << "时间段：" << (of.m_orderData[i]["interval"] == "1" ? "上午" : "下午") << " ";
				cout << "机房编号：" << of.m_orderData[i]["roomId"] << " ";
				cout << "审核状态：" << (of.m_orderData[i]["status"] == "1" ? "审核中" : "预约成功") << endl;
			}
		}
	}

		cout << endl<<"审核中或预约成功的记录可以取消，请输入取消的记录(输入0返回)" << endl;
		int cmd = 0;
		while (1) {
			cin >> cmd;
			if (cmd <= v.size() && cmd >= 0) {
				if (cmd == 0) {
					break;
				}
				else {
					of.m_orderData[v[cmd - 1]]["status"] = "0";
					of.updateOrder();//所有数据重写
					cout << "已取消预约" << endl;
					break;
				}
			}
			cout << "输入有误，请重新输入" << endl;
		}

	system("pause");
	system("cls");
}