#include"Teacher.h"

//默认构造
Teacher::Teacher() {

}
//有参构造
Teacher::Teacher(int empId, string name, string psw) {
	this->empId = empId;
	this->m_Name = name;
	this->m_Pwd = psw;
}
//查看所有预约
void Teacher::showAllOrder() {
	OrderFile of;
	if (of.m_Size == 0) {
		cout << "无预约记录" << endl;
		system("pause");
		return;
	}
	for (int i = 0; i < of.m_Size; i++) {
		cout << i + 1 << ", ";
		cout << "预约日期： 周" << of.m_orderData[i]["date"] << " ";
		cout << "时间段：" << (of.m_orderData[i]["interval"] == "1" ? "上午" : "下午") << " ";
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
//菜单界面
void Teacher::operMenu() {
	cout << "欢迎教师：" << this->m_Name << "登录！" << endl;
	cout << endl;
	cout << "1.查看所有预约" << endl;
	cout << "2.审核预约" << endl;
	cout << "0.注销登录" << endl;
	cout << endl;
	cout << "请输入您的选择：" << endl;
}
//审核预约
void Teacher::validOrder() {
	OrderFile of;

	if (of.m_Size == 0) {
		cout << "无预约记录" << endl;
		system("pause");
		system("cls");
		return;
	}
	int index = 1;
	vector<int> v;//存放下标编号
	cout << "待审核的预约记录如下：" << endl;
	for (int i = 0; i < of.m_Size; i++) {
		if (of.m_orderData[i]["status"] == "1") {
			v.push_back(i);
			cout << index++ << ", " << "预约日期：星期" << of.m_orderData[i]["date"] << " ";
			cout << "时间段：" << (of.m_orderData[i]["interval"] == "1" ? "上午" : "下午") << " ";
			cout << "学生编号：" << setiosflags(ios::left)<<setw(8) << of.m_orderData[i]["studentId"] << " ";
			cout << "学生姓名：" << setiosflags(ios::left) << setw(6) << of.m_orderData[i]["studentName"] << " ";
			cout << "机房编号：" << of.m_orderData[i]["roomId"] << " ";
			cout << "审核状态：审核中" << endl;
		}
	}
	cout << endl << "请输入审核的预约(输入0返回)" << endl;

	int cmd = 0;
	while (1) {
		cin >> cmd;
		if (cmd <= v.size() && cmd >= 0) {
			if (cmd == 0) break;
			else {
				cout << "请输入审核结果" << endl;
				cout << "1，通过" << endl;
				cout << "2，不通过" << endl;
				int cmd2;
				while (1) {
					cin >> cmd2;
					if (cmd2 == 1) {
						of.m_orderData[v[cmd - 1]]["status"] = "2";
						break;
					}
					else if(cmd2 == 2){
						 of.m_orderData[v[cmd - 1]]["status"] = "-1";
						break;
					}
					else cout << "输入错误，请重新输入" << endl;
				}
				of.updateOrder();//所有数据重写
				cout << "审核完毕" << endl;
				break;
			}
		}
		cout << "输入有误，请重新输入" << endl;
	}
	
	system("pause");
	system("cls");
}