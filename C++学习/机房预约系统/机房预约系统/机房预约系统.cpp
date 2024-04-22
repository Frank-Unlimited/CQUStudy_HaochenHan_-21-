#include<iostream>
#include<string>
#include<fstream>
#include"Student.h"
#include"Teacher.h"
#include"Manager.h"
#include"Identity.h"
#include"globalFile.h"
using namespace std;
//进入管理员子菜单界面
void managerMenu(Identity*& manager) {
	while (1) {
		//调用管理员子菜单
		manager->operMenu();
		//将父类指针转为子类指针，可以调用子类里其他接口
		Manager* man = (Manager*)manager;

		int cmd = 0;
		cin >> cmd;
		if (cmd == 1) {//添加账号
			man->addPerson();
		}
		else if (cmd == 2) {//查看账号
			man->showPerson();
		}
		else if (cmd == 3) {//查看机房信息
			man->showComputer();
		}
		else if (cmd == 4) {//清空预约
			man->clearPerson();
		}
		else {//注销登录
			delete manager;
			cout << "注销成功！" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//进入学生子菜单界面
void studentMenu(Identity*& student) {
	while (1) {
		//调用子菜单界面
		student->operMenu();
		//父类指针转成子类指针
		Student* stu = (Student*)student;
		
		int cmd = 0;
		cin >> cmd;

		if (cmd == 1) {
			//申请预约
			stu->applyOrder();
		}
		else if (cmd == 2) {
			//查看我的预约
			stu->showMyOrder();
		}
		else if (cmd == 3) {
			//查看所有预约
			stu->showAllOrder();
		}
		else if (cmd == 4) {
			//取消预约
			stu->cancelOrder();
		}
		else {
			//注销登录
			delete student;
			cout << "注销成功！" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//进入教师子菜单界面
void teacherMenu(Identity*& teacher) {
	while (1) {
		//调用子菜单界面
		teacher->operMenu();
		//父类指针转成子类指针
		Teacher* tea = (Teacher*)teacher;

		int cmd = 0;
		cin >> cmd;

		if (cmd == 1) {
			//查看所有预约
			tea->showAllOrder();
		}
		else if (cmd == 2) {
			//审核预约
			tea->validOrder();
		}
		else {
			//注销登录
			delete teacher;
			cout << "注销成功！" << endl;
			system("pause");
			system("cls");
			return;
		}
	}
}
//登录功能
void LoginIn(string fileName/*操作文件名*/, int type/*操作身份类型*/) {
	Identity* person = NULL;//父类指针指向子类对象
	
	//读文件
	ifstream ifs;
	ifs.open(fileName, ios::in);//文件已打开
	if (!ifs.is_open()) {
		cout << "文件不存在" << endl;
		ifs.close();
		return;
	}

	//准备接受用户信息
	int id = 0;
	string name;
	string pwd;

	//判断身份
	if (type == 1) {//学生身份
		cout << "请输入学号：" << endl;
		cin >> id;
	}
	else if (type == 2) {//教师身份
		cout << "请输入职工号：" << endl;
		cin >> id;
	}

	cout << "请输入用户名：" << endl;
	cin >> name;
	cout << "请输入密码：" << endl;
	cin >> pwd;

	//身份验证（验证id，name，pwd的正确性）
	if (type == 1) {
		//学生身份验证
		int fId;
		string fName;
		string fPwd;
		while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
			//与用户输入信息做对比
			if (fId == id && fName == name && fPwd == pwd) {
				cout << "学生验证登陆成功！" << endl;
				system("pause");
				system("cls");
				person = new Student(id,name,pwd);//用父类指针开辟一个堆区的学生类对象
				//进入学生身份子菜单
				studentMenu(person);//将父类指针传参
				return;
			}
		}
	}
	else if (type == 2) {
		//教师身份验证
		int fId;
		string fName;
		string fPwd;
		while (ifs >> fId && ifs >> fName && ifs >> fPwd) {
			//与用户输入信息做对比
			if (fId == id && fName == name && fPwd == pwd) {
				cout << "教师验证登陆成功！" << endl;
				system("pause");
				system("cls");
				person = new Teacher(id,name,pwd);
				//进入教师身份子菜单
				teacherMenu(person);
				return;
			}
		}
	}
	else {
		//管理员身份验证
		string fName;
		string fPwd;
		while (ifs >> fName && ifs >> fPwd) {
			//与用户输入信息做对比
			if (fName == name && fPwd == pwd) {
				cout << "管理员验证登陆成功！" << endl;
				system("pause");
				system("cls");
				person = new Manager(name,pwd);
				//进入管理员身份子菜单
				managerMenu(person);
				return;
			}
		}
	}

	cout << "验证登录失败！" << endl;
	system("pause");
	system("cls");

	return;
}
int main() {
	int cmd = 0;//接受用户选择

	while (1) {
		cout << "=======================欢迎来到机房预约系统=======================" << endl;
		cout << endl << "请输入您的身份" << endl;
		cout << "\t\t---------------------------\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     1. 学生代表         |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     2. 老师             |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     3. 管理员           |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t|     0. 退出             |\n";
		cout << "\t\t|                         |\n";
		cout << "\t\t---------------------------\n";
		cout << "请输入您的选择：";

		cin >> cmd;

		switch (cmd) {
		case 1://学生
			LoginIn(STUDENT_FILE, 1);//进入学生登录函数
			break;
		case 2://老师
			LoginIn(TEACHER_FILE, 2);
			break;
		case 3://管理员			
			LoginIn(ADMIN_FILE, 3);
			break;
		case 0://退出
			cout << "欢迎下一次使用" << endl;
			system("pause");
			return 0;
			break;
		default:
			cout << "输入有误，请重新选择" << endl;
			system("pause");
			system("cls");
			break;
		}
	}
	system("pause");
	return 0;
}